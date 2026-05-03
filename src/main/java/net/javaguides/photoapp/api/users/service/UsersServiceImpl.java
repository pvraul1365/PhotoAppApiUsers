package net.javaguides.photoapp.api.users.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javaguides.photoapp.api.users.data.UserEntity;
import net.javaguides.photoapp.api.users.data.UsersRepository;
import net.javaguides.photoapp.api.users.shared.UserDto;
import net.javaguides.photoapp.api.users.ui.model.AlbumResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    private final RestTemplate restTemplate;
    private final Environment environment;

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
        log.info("✅ - User created successfully with email: {}", userDto.getEmail());

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
    public UserDto getUserByUserId(final String userId) {
        log.info("🔍 - Fetching user details for userId: {}", userId);
        final UserEntity userEntity = usersRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with id: " + userId);
        }

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        log.info("🔍 - Fetching albums for userId: {} from Albums Microservice", userId);
        String albumsUrl = String.format(environment.getProperty("albums.url"), userId);
        ResponseEntity<List<AlbumResponseModel>> albumListResponse = restTemplate.exchange(albumsUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AlbumResponseModel>>() {
        });
        final List<AlbumResponseModel> albumsList = albumListResponse.getBody();
        userDto.setAlbums(albumsList);

        log.info("✅ - User details found for userId: {}", userId);
        return userDto;
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
