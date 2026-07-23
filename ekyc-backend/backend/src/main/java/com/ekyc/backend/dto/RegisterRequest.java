package com.ekyc.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Username khong duoc de trong")
    @Size(min = 4, max = 50, message = "Username phai tu 4-50 ky tu")
    private String username;

    @NotBlank(message = "Password khong duoc de trong")
    @Size(min = 6, message = "Password phai co it nhat 6 ky tu")
    private String password;

    @NotBlank(message = "Email khong duoc de trong")
    @Email(message = "Email khong dung dinh dang")
    private String email;
}