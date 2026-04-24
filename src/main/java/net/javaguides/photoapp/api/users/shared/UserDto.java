package net.javaguides.photoapp.api.users.shared;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserDto
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 18/04/2026 - 11:08
 * @since 1.17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private String userId;
    private String encryptedPassword;
}
