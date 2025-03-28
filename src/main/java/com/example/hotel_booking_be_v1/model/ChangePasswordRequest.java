package com.example.hotel_booking_be_v1.model;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "Mật khẩu cũ không được để trống.")
    private String oldPassword;

    @NotBlank(message = "Mật khẩu mới không được để trống.")
    private String newPassword;
}
