package es.uma.ingweb.coffeecar.consumers;

import es.uma.ingweb.coffeecar.RestTemplateProxy;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        return new ArrayList<>((Objects.requireNonNull(usersResponse.getBody())).getContent());
    }

    public Optional<User> optionalGetByEmail(String email) {
        return restTemplateProxy
                .getForEntity(
                        GET_USER_BY_EMAIL_URL,
                        User.class,
                        email
                ).map(ResponseEntity::getBody);
    }

    public User getByEmail(String email) {
        ResponseEntity<EntityModel<User>> userResponseEntity = restTemplate
                .exchange(
                        GET_USER_BY_EMAIL_URL,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<EntityModel<User>>() {
                        },
                        email);

        return setURIs(userResponseEntity.getBody());
    }

    public User setURIs(EntityModel<User> resourceUser){
        User user = Objects.requireNonNull(resourceUser).getContent();
        Optional<Link> teacherLink = Objects.requireNonNull(resourceUser).getLink("self");
        Optional<Link> teacherOwnedAnnounces = Objects.requireNonNull(resourceUser).getLink("ownedAnnounces");
        Optional<Link> teacherJoinedAnnounces = Objects.requireNonNull(resourceUser).getLink("joinedAnnounces");

        Objects.requireNonNull(user).setSelfURI(teacherLink.map(Link::getHref).get());
        Objects.requireNonNull(user).setOwnedAnnouncementsURI(teacherOwnedAnnounces.map(Link::getHref).get());
        Objects.requireNonNull(user).setOwnedAnnouncementsURI(teacherJoinedAnnounces.map(Link::getHref).get());

        return user;
    }

    public void create(User user) {
        restTemplate.postForLocation(URL, user);
    }

    public void edit(User user) {
        restTemplate.put(URL, user, User.class);
    }

    public void delete(User user) {
        restTemplate.delete(URL, user, User.class);
    }

    private static ParameterizedTypeReference<PagedModel<User>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
