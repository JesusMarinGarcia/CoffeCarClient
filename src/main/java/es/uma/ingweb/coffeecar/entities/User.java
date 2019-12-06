package es.uma.ingweb.coffeecar.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.List;

@Data
@JsonSerialize
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private long id;
    private String name;
    private String email;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Announcement> ownedAnnouncements;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Announcement> joinedAnnouncements;
}
