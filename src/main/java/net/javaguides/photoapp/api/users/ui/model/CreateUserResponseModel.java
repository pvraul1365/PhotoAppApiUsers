package net.javaguides.photoapp.api.users.ui.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CreateUserResponseModel
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 18/04/2026 - 16:32
 * @since 1.17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserResponseModel {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;

}
