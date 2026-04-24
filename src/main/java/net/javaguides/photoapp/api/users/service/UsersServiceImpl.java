package net.javaguides.photoapp.api.users.service;

import java.util.ArrayList;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javaguides.photoapp.api.users.data.UserEntity;
import net.javaguides.photoapp.api.users.data.UsersRepository;
import net.javaguides.photoapp.api.users.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * UsersServiceImpl
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 18/04/2026 - 11:06
 * @since 1.17
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto userDetails) {
        log.info("🔄 - Starting the user creation process for email: {}", userDetails.getEmail());
        userDetails.setUserId(UUID.randomUUID().toString());

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

        userEntity = usersRepository.save(userEntity);

        final UserDto userDto = modelMapper.map(userEntity, UserDto.class);
        log.info("✅✅ - User created successfully with email: {}", userDto.getEmail());

        return userDto;
    }

    @Override
    public UserDto getUserDetailsByEmail(final String email) {
        log.info("🔍 - Fetching user details for email: {}", email);

        final UserEntity userEntity = usersRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        log.info("✅ - User details found for email: {}", email);

        return new ModelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = usersRepository.findByEmail(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        return new User(userEntity.getEmail(),
                userEntity.getEncryptedPassword(),
                true, true, true, true,
                new ArrayList<>());
    }
}
