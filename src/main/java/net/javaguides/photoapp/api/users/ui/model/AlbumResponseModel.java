package net.javaguides.photoapp.api.users.ui.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AlbumResponseModel
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 02/05/2026 - 14:31
 * @since 1.17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumResponseModel {

    private String albumId;
    private String userId;
    private String name;
    private String description;

}
