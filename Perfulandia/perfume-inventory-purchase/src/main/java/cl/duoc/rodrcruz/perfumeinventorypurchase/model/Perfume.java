package cl.duoc.rodrcruz.perfumeinventorypurchase.model;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Perfume {

    private int id;
    private String name;
    private String brand;
    private int size;
}
