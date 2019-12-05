package es.uma.ingweb.coffeecar.entities;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private long id;
    private String name;
    private String mail;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Announcement> ownedAnnouncements;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Announcement> joinedAnnouncements;
}
