package com.ekyc.backend.client;

import com.ekyc.backend.dto.ai.CccdExtractAiResponse;
import com.ekyc.backend.dto.ai.FaceVerifyAiResponse;
import com.ekyc.backend.exception.AiServiceException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.PrematureCloseException;

import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
public class AiServiceClient {

    private final WebClient aiServiceWebClient;

    public CccdExtractAiResponse extractCccd(byte[] imageBytes, String filename) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", asResource(imageBytes, filename)).contentType(MediaType.IMAGE_JPEG);

        return callAiService(() ->
                aiServiceWebClient.post()
                        .uri("/ocr/extract-cccd")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .body(BodyInserters.fromMultipartData(builder.build()))
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, this::handleAiError)
                        .bodyToMono(CccdExtractAiResponse.class)
                        .block()
        );
    }

    public FaceVerifyAiResponse verifyFace(byte[] cccdBytes, byte[] selfieBytes) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("cccd_image", asResource(cccdBytes, "cccd.jpg")).contentType(MediaType.IMAGE_JPEG);
        builder.part("selfie_image", asResource(selfieBytes, "selfie.jpg")).contentType(MediaType.IMAGE_JPEG);

        return callAiService(() ->
                aiServiceWebClient.post()
                        .uri("/face/verify")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .body(BodyInserters.fromMultipartData(builder.build()))
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, this::handleAiError)
                        .bodyToMono(FaceVerifyAiResponse.class)
                        .block()
        );
    }

    /**
     * Boc goi tat ca cuoc goi sang AI Service, phan biet ro:
     * - Loi nghiep vu tu chinh AI (vi du: khong detect duoc mat) -> AiServiceException voi message ro rang
     * - Loi ket noi/timeout (Python service tat, mang loi) -> AiServiceException voi message chung
     */
    private <T> T callAiService(java.util.function.Supplier<T> call) {
        try {
            return call.get();
        } catch (AiServiceException e) {
            throw e;
        } catch (WebClientRequestException e) {
            throw new AiServiceException(
                    "Khong the ket noi toi AI Service. Vui long kiem tra AI Service co dang chay khong.");
        } catch (Exception e) {
        // PrematureCloseException va TimeoutException deu co the nam sau trong
        // chuoi nguyen nhan (cause chain) cua exception bao ngoai tu Reactor,
        // nen kiem tra bang instanceof thay vi khai bao rieng tung loai
            Throwable cause = e.getCause();
            if (cause instanceof PrematureCloseException || cause instanceof TimeoutException) {
                throw new AiServiceException(
                        "Mat ket noi toi AI Service hoac xu ly qua lau. Vui long kiem tra AI Service co dang chay khong.");
            }
            throw new AiServiceException("Loi khong xac dinh khi goi AI Service: " + e.getMessage());
        }
    }

    private ByteArrayResource asResource(byte[] bytes, String filename) {
        return new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return filename;
            }
        };
    }

    private Mono<? extends Throwable> handleAiError(
            org.springframework.web.reactive.function.client.ClientResponse response) {
        return response.bodyToMono(String.class)
                .map(body -> new AiServiceException("Loi tu AI Service: " + body));
    }
}