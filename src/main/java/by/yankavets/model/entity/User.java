package by.yankavets.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "users")
@Entity
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString(exclude = "sessions")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<SessionEntity> sessions;

    @OneToMany(mappedBy= "user",fetch = FetchType.LAZY)
    private List<Location> locations;


}
