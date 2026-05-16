package net.javaguides.photoapp.api.users.data;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.javaguides.photoapp.api.users.ui.model.AlbumResponseModel;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "albums-ws")
public interface AlbumsServiceClient {

    @GetMapping("/users/{id}/albums")
    @CircuitBreaker(name="albums-ws", fallbackMethod="getAlbumsFallback")
    public List<AlbumResponseModel> getAlbums(@PathVariable String id);

    default List<AlbumResponseModel> getAlbumsFallback(@PathVariable String id, Throwable exception) {
        System.out.println("Param = " + id);
        System.out.println("⚠️ - Albums service is currently unavailable. Returning fallback response.");
        System.out.println("❌ - Exception took place: " + exception.getMessage());

        return new ArrayList<>();
    }
}
