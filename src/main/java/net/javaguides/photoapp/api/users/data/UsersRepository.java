package net.javaguides.photoapp.api.users.data;

import org.springframework.data.repository.CrudRepository;

/**
 * UsersRepository
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 18/04/2026 - 16:04
 * @since 1.17
 */
public interface UsersRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

}
