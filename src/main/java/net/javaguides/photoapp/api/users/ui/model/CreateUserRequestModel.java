package net.javaguides.photoapp.api.users.ui.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * CreateUserRequestModel
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 18/04/2026 - 10:39
 * @since 1.17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequestModel {

    @NotNull(message="First name cannot be null")
    @Size(min=2, message="First name must be at least 2 characters long")
    private String firstName;

    @NotNull(message="Last name cannot be null")
    @Size(min=2, message="Last name must be at least 2 characters long")
    private String lastName;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, max=16, message = "Password must be between 8 and 16 characters long")
    private String password;

}
