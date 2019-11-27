package es.uma.ingweb.coffeecar.entities;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private long id;
    private String name;
    private String password;
    private String mail;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Announce> ownedAnnounces;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Announce> joinedAnnounces;
}
