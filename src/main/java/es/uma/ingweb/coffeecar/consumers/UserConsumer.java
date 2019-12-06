package es.uma.ingweb.coffeecar.consumers;

import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserConsumer {
    private static final String URL = "http://localhost:8080/users";
    private static final String GET_ALL_USERS_URL = "http://localhost:8080/users";
    private static final String GET_USER_BY_EMAIL_URL = "http://localhost:8080/users/search/findUserByEmail?email={email}";

    private final RestTemplate restTemplate;
    private final AnnouncementConsumer announcementConsumer;

    public UserConsumer(RestTemplate restTemplate, AnnouncementConsumer announcementConsumer) {
        this.restTemplate = restTemplate;
        this.announcementConsumer = announcementConsumer;
    }

    public List<User> getAll() {
        final ResponseEntity<PagedModel<User>> usersResponse = restTemplate
              .exchange(GET_ALL_USERS_URL, HttpMethod.GET, null,
                    getParameterizedTypeReference()
                    );
        return new ArrayList<>(Objects.requireNonNull(usersResponse.getBody()).getContent());
    }
    public User getByEmail(String email){
        ResponseEntity<User> user=null;
        try {
            user = restTemplate
                    .getForEntity(
                            GET_USER_BY_EMAIL_URL,
                            User.class,
                            email
                    );
        } catch (HttpClientErrorException ex)   {
            if (ex.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw ex;
            }
        }
        return user!=null ? completeUser(Objects.requireNonNull(user.getBody())) : null;
    }

    private User completeUser(User user){
        user.setOwnedAnnouncements(announcementConsumer.getByDriver(user.getEmail()));
        user.setJoinedAnnouncements(announcementConsumer.getByPassenger(user.getEmail()));
        return user;
    }

    public void create(User user){
        restTemplate.postForEntity(URL, user, User.class);
    }
    public void edit(User user){
        restTemplate.put(URL, user, User.class);
    }
    public void delete(User user){
        restTemplate.delete(URL, user, User.class);
    }

    private static ParameterizedTypeReference<PagedModel<User>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {};
    }
}
