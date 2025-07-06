package cl.duoc.rodrcruz.perfumeinventorypurchase.controller.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PerfumeRequest {
    private String name;
    private String brand;
    private Integer size;
}
