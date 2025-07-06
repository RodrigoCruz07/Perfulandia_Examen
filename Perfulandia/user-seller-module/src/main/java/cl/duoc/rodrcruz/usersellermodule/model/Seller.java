package cl.duoc.rodrcruz.usersellermodule.model;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Seller {
    private int id;

    private String name;

    private String lastname;

    private int age;

    private String address;

    private String phone;

    private String email;

    private String role;



}
