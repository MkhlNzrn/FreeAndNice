package com.example.freeandnice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Schema(description = "Запрос на регистрацию")
public class SignUpRequest {

    @Schema(description = "Адрес электронной почты", example = "jondoe@gmail.com")
    @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
    @NotBlank(message = "Адрес электронной почты не может быть пустым")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String email;

    @Schema(description = "Имя пользователя", example = "JonDoe")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String username;

    @Schema(description = "Имя", example = "Jon")
    @Size(min = 2, max = 100, message = "Имя должно содержать от 2 до 100 символов")
    @NotBlank(message = "Имя не может быть пустым")
    private String firstName;

    @Schema(description = "Отчество", example = "Middle")
    @Size(max = 100, message = "Отчество не может содержать более 100 символов")
    private String middleName;

    @Schema(description = "Фамилия", example = "Doe")
    @Size(min = 2, max = 100, message = "Фамилия должна содержать от 2 до 100 символов")
    @NotBlank(message = "Фамилия не может быть пустой")
    private String lastName;

    @Schema(description = "Адрес", example = "123 Main St")
    @Size(min = 5, max = 255, message = "Адрес должен содержать от 5 до 255 символов")
    @NotBlank(message = "Адрес не может быть пустым")
    private String address;

    @Schema(description = "Номер телефона", example = "+1234567890")
    @Size(min = 10, max = 15, message = "Номер телефона должен содержать от 10 до 15 символов")
    @NotBlank(message = "Номер телефона не может быть пустым")
    private String phoneNumber;

    @Schema(description = "Пароль", example = "my_secret_password")
    @Size(min = 8, max = 255, message = "Пароль должен содержать от 8 до 255 символов")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

    @Schema(description = "Подтверждение пароля", example = "my_secret_password")
    @Size(min = 8, max = 255, message = "Подтверждение пароля должно содержать от 8 до 255 символов")
    @NotBlank(message = "Подтверждение пароля не может быть пустым")
    private String confirmPassword;

    @Schema(description = "Подписка на новости", example = "true")
    private Boolean newsSubscribed;

    @Schema(description = "Аватар", example = "avatar.jpg")
    private String avatar;
}
