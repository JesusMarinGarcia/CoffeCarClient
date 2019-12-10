package es.uma.ingweb.coffeecar.consumers;


import es.uma.ingweb.coffeecar.entities.Announce;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.client.Traverson;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;

import static org.springframework.hateoas.MediaTypes.HAL_JSON;

@Service
public class UserConsumer {
    private static final String URL = "http://localhost:8080/api/users";

    private final RestTemplate restTemplate;
    private final Traverson traverson;

    public UserConsumer(RestTemplate restTemplate, Traverson traverson) {
        this.restTemplate = restTemplate;
        this.traverson = traverson;
    }

    public Optional<User> optionalGetByEmail(String email) {
        Optional<User> response = Optional.empty();
        try {
            User user = getByEmail(email);
            response = Optional.of(user);
        } catch (Exception ignored) {
            System.out.print("not found :(");
        }

        return response;
    }

    public User getByEmail(String email) {
        Traverson.TraversalBuilder traversalBuilder = traverson.follow("users").follow("search").follow("findUserByEmail")
              .withTemplateParameters(Map.of("email", email));

        EntityModel<User> userEntityModel = traversalBuilder.toObject(getEntityModelParameterizedTypeReference());

        User user = Objects.requireNonNull(userEntityModel).getContent();

        String ownedAnnouncesLink = userEntityModel.getLink("ownedAnnounces").map(Link::getHref).get();
        String joinedAnnouncesLink = userEntityModel.getLink("joinedAnnounces").map(Link::getHref).get();

        user.setOwnedAnnounces(getAnnounces(URI.create(ownedAnnouncesLink), "ownedAnnounces"));
        user.setJoinedAnnounces(getAnnounces(URI.create(joinedAnnouncesLink), "joinedAnnounces"));

        user.add(userEntityModel.getLinks());
        return user;
    }

    private List<Announce> getAnnounces(URI url, String relation) {
        Collection<Announce> announces =
                Objects.requireNonNull(new Traverson(url, HAL_JSON).follow("self").toObject(getAnnounceCollectionType())).getContent();
        return new ArrayList<>(announces);
    }

    private ParameterizedTypeReference<CollectionModel<Announce>> getAnnounceCollectionType() {
        return new ParameterizedTypeReference<>() {
        };
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


    private static ParameterizedTypeReference<EntityModel<User>> getEntityModelParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
