package com.ekyc.backend.client;

import com.ekyc.backend.dto.ai.CccdExtractAiResponse;
import com.ekyc.backend.dto.ai.FaceVerifyAiResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AiServiceClient {

    private final WebClient aiServiceWebClient;

    public CccdExtractAiResponse extractCccd(byte[] imageBytes, String filename) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", asResource(imageBytes, filename)).contentType(MediaType.IMAGE_JPEG);

        return aiServiceWebClient.post()
                .uri("/ocr/extract-cccd")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleAiError)
                .bodyToMono(CccdExtractAiResponse.class)
                .block();
    }

    public FaceVerifyAiResponse verifyFace(byte[] cccdBytes, byte[] selfieBytes) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("cccd_image", asResource(cccdBytes, "cccd.jpg")).contentType(MediaType.IMAGE_JPEG);
        builder.part("selfie_image", asResource(selfieBytes, "selfie.jpg")).contentType(MediaType.IMAGE_JPEG);

        return aiServiceWebClient.post()
                .uri("/face/verify")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleAiError)
                .bodyToMono(FaceVerifyAiResponse.class)
                .block();
    }

    private ByteArrayResource asResource(byte[] bytes, String filename) {
        return new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return filename;
            }
        };
    }

    private reactor.core.publisher.Mono<? extends Throwable> handleAiError(
            org.springframework.web.reactive.function.client.ClientResponse response) {
        return response.bodyToMono(String.class)
                .map(body -> new IllegalArgumentException("Loi tu AI Service: " + body));
    }
}