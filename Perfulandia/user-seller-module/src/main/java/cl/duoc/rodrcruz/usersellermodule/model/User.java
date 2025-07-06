package cl.duoc.rodrcruz.usersellermodule.model;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private int id;

    private String name;

    private String lastname;

    private int age;

    private String email;

    private String phone;

    private Date registrationDate;


}
