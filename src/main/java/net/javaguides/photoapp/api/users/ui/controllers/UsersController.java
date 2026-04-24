package net.javaguides.photoapp.api.users.ui.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javaguides.photoapp.api.users.service.UsersService;
import net.javaguides.photoapp.api.users.shared.UserDto;
import net.javaguides.photoapp.api.users.ui.model.CreateUserRequestModel;
import net.javaguides.photoapp.api.users.ui.model.CreateUserResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UsersController
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 12/04/2026 - 13:50
 * @since 1.17
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private final Environment environment;

    private final UsersService usersService;

    @GetMapping("/status/check")
    public String status() {
        log.info("🔄 - Checking the status of the users microservice...");
        
        return "Working on port " + environment.getProperty("local.server.port");
    }

    @PostMapping(
            consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody final CreateUserRequestModel createUserRequestModel) {
        log.info("📥 - Creating a new user...");

        // Implementation for creating a user would go here
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        final UserDto userDto = usersService.createUser(modelMapper.map(createUserRequestModel, UserDto.class));
        log.info("✅ - User creation process completed for email: {}", userDto.getEmail());

        return new  ResponseEntity<>(modelMapper.map(userDto, CreateUserResponseModel.class), HttpStatus.CREATED);
    }
}
