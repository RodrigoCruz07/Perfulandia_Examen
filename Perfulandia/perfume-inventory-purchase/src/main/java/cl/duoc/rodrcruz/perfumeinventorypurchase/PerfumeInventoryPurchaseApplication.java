package cl.duoc.rodrcruz.perfumeinventorypurchase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PerfumeInventoryPurchaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerfumeInventoryPurchaseApplication.class, args);

    }

}
