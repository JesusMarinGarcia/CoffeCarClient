package es.uma.ingweb.coffeecar.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.springframework.hateoas.EntityModel;

import java.util.List;

@Data
@NoArgsConstructor
@JsonSerialize
@AllArgsConstructor
@Builder
public class User extends EntityModel<User> {
    private long id;
    private String name;
    private String email;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Announce> ownedAnnounces;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Announce> joinedAnnounces;
}
