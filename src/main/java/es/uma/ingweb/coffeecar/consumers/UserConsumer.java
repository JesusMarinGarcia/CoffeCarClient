package es.uma.ingweb.coffeecar.consumers;

import es.uma.ingweb.coffeecar.RestTemplateProxy;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserConsumer {
    private static final String URL = "http://localhost:8080/users";
    private static final String GET_ALL_USERS_URL = "http://localhost:8080/users";
    private static final String GET_USER_BY_EMAIL_URL = "http://localhost:8080/users/search/findUserByEmail?email={email}";

    private final RestTemplate restTemplate;
    private final RestTemplateProxy restTemplateProxy;


    public UserConsumer(RestTemplate restTemplate, RestTemplateProxy restTemplateProxy) {
        this.restTemplate = restTemplate;
        this.restTemplateProxy = restTemplateProxy;
    }

    public List<User> getAll() {
        final ResponseEntity<PagedModel<User>> usersResponse = restTemplate
              .exchange(GET_ALL_USERS_URL, HttpMethod.GET, null,
                    getParameterizedTypeReference()
              );
        return new ArrayList<>((usersResponse.getBody()).getContent());
    }

    public Optional<User> optionalGetByEmail(String email) {
        return restTemplateProxy.getForEntity(GET_USER_BY_EMAIL_URL, User.class, email)
              .map(HttpEntity::getBody).orElseThrow(NullPointerException::new);
    }*/

    public User getByEmail(String email) {
        return restTemplateProxy.getForEntity(
              GET_USER_BY_EMAIL_URL,
              User.class,
              email).getBody();
    }

    public void create(User user) {

        restTemplateProxy.exchange(URL, HttpMethod.POST, new HttpEntity<>(user), getParameterizedTypeReference());
    }

    public void edit(User user) {
        restTemplateProxy.exchange(URL, HttpMethod.PUT, new HttpEntity<>(user), getParameterizedTypeReference());
    }

    public void delete(User user) {
        restTemplateProxy.exchange(URL, HttpMethod.DELETE, new HttpEntity<>(user), getParameterizedTypeReference());
    }

    private static ParameterizedTypeReference<PagedModel<User>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
