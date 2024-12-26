package by.yankavets.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Data
@RequiredArgsConstructor
public class UserLoginDto {

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email is not valid")
    String email;

    @Size(min = 8, max = 64, message = "Password should be between 8 and 30 characters")
    @NotBlank(message = "Password should not be empty!")
    String password;
}
