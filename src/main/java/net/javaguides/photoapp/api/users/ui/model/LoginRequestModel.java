package net.javaguides.photoapp.api.users.ui.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoginRequestModel
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 19/04/2026 - 16:20
 * @since 1.17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestModel {

    private String email;
    private String password;

}
