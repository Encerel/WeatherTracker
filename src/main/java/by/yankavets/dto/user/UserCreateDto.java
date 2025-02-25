package by.yankavets.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateDto {

    @NotBlank(message = "Name should not be empty!")
    @Pattern(regexp = "^[\\p{L}\\d\\s]{2,32}$", message = "Name should be between 2 and 32 characters")
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email is not valid")
    private String email;

    @Size(min = 8, max = 64, message = "Password should be between 8 and 64 characters")
    @NotBlank(message = "Password should not be empty!")
    private String password;

    @Size(min = 8, max = 64, message = "Password should be between 8 and 64 characters")
    @NotBlank(message = "Confirmed password should not be empty!")
    private String confirmedPassword;

}

