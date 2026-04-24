package net.javaguides.photoapp.api.users.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import net.javaguides.photoapp.api.users.service.UsersService;
import net.javaguides.photoapp.api.users.shared.UserDto;
import net.javaguides.photoapp.api.users.ui.model.LoginRequestModel;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * AuthenticationFilter
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 19/04/2026 - 16:22
 * @since 1.17
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UsersService usersService;
    private final Environment environment;

    public AuthenticationFilter(UsersService usersService,
                                Environment environment,
                                AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
        this.usersService = usersService;
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestModel creds = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequestModel.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );

        } catch (IOException e)  {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        final String username = ((User) authResult.getPrincipal()).getUsername();
        final UserDto userDetails = usersService.getUserDetailsByEmail(username);
        final String tokenSecret = environment.getProperty("token.secret");
        final byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        final SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        Instant now = Instant.now();
        final String token = Jwts.builder()
                .setSubject(userDetails.getEmail())
                .setExpiration(Date.from(now.plusMillis(environment.getProperty("token.expiration_time", Long.class))))
                .setIssuedAt(Date.from(now))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        response.addHeader("token", token);
        response.addHeader("userId", userDetails.getUserId()




        );
    }
}
