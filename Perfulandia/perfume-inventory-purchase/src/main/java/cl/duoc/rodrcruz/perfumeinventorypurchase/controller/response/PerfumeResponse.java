package cl.duoc.rodrcruz.perfumeinventorypurchase.controller.response;
import lombok.*;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PerfumeResponse {
    private Integer id;
    private String name;
    private String brand;
    private Integer size;
}
