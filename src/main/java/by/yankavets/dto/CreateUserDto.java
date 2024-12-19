package by.yankavets.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@RequiredArgsConstructor
public class CreateUserDto {

    @NotBlank(message = "Name should not be empty!")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{2,32}$\n", message = "Name should be between 2 and 32 characters")
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email is not valid")
    private String email;

    @Size(min = 8, max = 64, message = "Password should be between 8 and 30 characters")
    @NotBlank(message = "Password should not be empty!")
    private String password;

    @Size(min = 8, max = 64, message = "Name should be between 8 and 30 characters")
    @NotBlank(message = "Confirmed password should not be empty!")
    private String confirmedPassword;

}

