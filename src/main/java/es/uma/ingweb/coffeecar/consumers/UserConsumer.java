package es.uma.ingweb.coffeecar.consumers;

import es.uma.ingweb.coffeecar.RestTemplateProxy;
import es.uma.ingweb.coffeecar.entities.Announcement;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class UserConsumer {
    private static final String URL = "http://localhost:8080/users";
    private static final String GET_ALL_USERS_URL = "http://localhost:8080/users";
    private static final String GET_USER_BY_EMAIL_URL = "http://localhost:8080/users/search/findUserByEmail?email={email}";

    private final RestTemplate restTemplate;
    @Autowired
    private static AnnouncementConsumer announcementConsumer;
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
        return setParams(new ArrayList<>((Objects.requireNonNull(usersResponse.getBody())).getContent()));
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
        EntityModel<User> userEntityModel = userResponseEntity.getBody();
        User user = Objects.requireNonNull(userEntityModel).getContent();
        Objects.requireNonNull(user).add(userEntityModel.getLinks());
        return setParams(user);

    }

    public User getUser(String uri){
        ResponseEntity<EntityModel<User>> userResponseEntity = restTemplate
                .exchange(
                        uri,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<EntityModel<User>>() {
                        });
        EntityModel<User> userEntityModel = userResponseEntity.getBody();
        User user = Objects.requireNonNull(userEntityModel).getContent();
        Objects.requireNonNull(user).add(userEntityModel.getLinks());
        return setParams(user);
    }


    public List<User> getPassengers(String uri){
        return
                setParams(restTemplateProxy.exchange(
                        uri,
                        HttpMethod.GET,
                        null,
                        getParameterizedTypeReference())
                        .map(HttpEntity::getBody).map(CollectionModel::getContent)
                        .map(Collection::stream).map(content -> content.collect(toList()))
                        .orElse(Collections.emptyList()));
    }

    private static User setParams(User user){
        user.setOwnedAnnouncements(
                announcementConsumer
                        .getOwnedAnnouncements(
                                user
                                        .getLink("ownedAnnouncements")
                                        .map(Link::getHref).get()));
        user.setJoinedAnnouncements(
                announcementConsumer
                        .getJoinedAnnouncements
                                (user
                                        .getLink("joinedAnnouncements")
                                        .map(Link::getHref).get()));
        return user;
    }

    private List<User> setParams(List<User> users){
        return users.stream().map(UserConsumer::setParams).collect(Collectors.toList());
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
