package cl.duoc.rodrcruz.usersellermodule.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private int id;
    private String name;
    private String lastname;
    private Integer age;
    private String email;
    private String phone;
    private LocalDate registrationDate;
    private String roleName;
}
