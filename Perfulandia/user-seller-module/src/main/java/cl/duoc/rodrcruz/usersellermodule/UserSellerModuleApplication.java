package cl.duoc.rodrcruz.usersellermodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@SpringBootApplication
@EnableJpaRepositories(basePackages = "cl.duoc.rodrcruz.usersellermodule.repository")
public class UserSellerModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserSellerModuleApplication.class, args);
    }



}
