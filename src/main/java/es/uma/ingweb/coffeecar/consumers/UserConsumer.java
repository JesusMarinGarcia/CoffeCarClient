package es.uma.ingweb.coffeecar.consumers;

import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserConsumer {
    private static final String URL = "http://localhost:8080/users";
    private static final String GET_ALL_USERS_URL = "http://localhost:8080/users/search/findAll";
    private static final String GET_USER_BY_EMAIL_URL = "http://localhost:8080/users/search/findByMail";

    @Autowired
    private RestTemplate restTemplate;

    public List<User> getAll() {
        final ResponseEntity<PagedModel<User>> usersResponse = restTemplate
              .exchange(GET_ALL_USERS_URL, HttpMethod.GET, null,
                    getParameterizedTypeReference()
                    );
        return new ArrayList<>(Objects.requireNonNull(usersResponse.getBody()).getContent());
    }
    public User getByEmail(String email){
        ResponseEntity<User> user = restTemplate
                .getForEntity(
                        GET_USER_BY_EMAIL_URL.concat("?email="+email), User.class
                );
        return user.getBody();
    }

    private void create(User user){
        restTemplate.postForEntity(URL, user, User.class);
    }
    private void edit(User user){
        restTemplate.put(URL, user, User.class);
    }
    private void delete(User user){
        restTemplate.delete(URL, user, User.class);
    }

    private static ParameterizedTypeReference<PagedModel<User>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {};
    }
}
