package cl.duoc.rodrcruz.usersellermodule.controller.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String name;
    private String lastname;
    private Integer age;
    private String email;
    private String phone;
    private String roleName;
}
