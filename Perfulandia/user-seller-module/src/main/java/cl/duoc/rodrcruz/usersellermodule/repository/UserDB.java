package cl.duoc.rodrcruz.usersellermodule.repository;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class  UserDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name="name", nullable = false, length = 50)
    private String name;

    @Column(name="lastname", nullable = false, length = 50)
    private String lastname;
    @Column(name = "age", nullable = false)
    private Integer age;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "phone", nullable = false)
    private String phone;
    @Column(name = "regDate", nullable = false)
    private LocalDate registrationDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private RoleDB role;


}
