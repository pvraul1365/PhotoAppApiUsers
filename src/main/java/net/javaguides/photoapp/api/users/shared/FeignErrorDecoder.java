package net.javaguides.photoapp.api.users.shared;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * FeignErrorDecoder
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 03/05/2026 - 16:02
 * @since 1.17
 */
@Component
@RequiredArgsConstructor
public class FeignErrorDecoder implements ErrorDecoder {

    private final Environment environment;

    @Override
    public Exception decode(String methodKey, Response response) {

        switch (response.status()) {
            case 400:
                break;
            case 404:
                if (methodKey.contains("getAlbums")) {
                    return new ResponseStatusException(HttpStatusCode.valueOf(response.status()),
                            environment.getProperty("albums.exception.albums-not-found"));
                }
                break;
            default:
                return new Exception(response.reason());
        }

        return null;
    }

}