package es.uma.ingweb.coffeecar.consumers;

import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UserConsumer {
    private static final String GET_ALL_USERS_URL = "http://localhost:8080/users";

    private final RestTemplate template;

    public UserConsumer(RestTemplate template) {
        this.template = template;
    }

    public List<User> getAll() {
        final ResponseEntity<PagedModel<User>> usersResponse = template
              .exchange(GET_ALL_USERS_URL, HttpMethod.GET, null,
                    getParameterizedTypeReference()
                    );
        return new ArrayList<>(Objects.requireNonNull(usersResponse.getBody()).getContent());
    }

    private static ParameterizedTypeReference<PagedModel<User>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {};
    }
}
