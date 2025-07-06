package cl.duoc.rodrcruz.usersellermodule.config;
import cl.duoc.rodrcruz.usersellermodule.security.JwtAuthEntryPoint;
import cl.duoc.rodrcruz.usersellermodule.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthEntryPoint unauthorizedHandler;
    private final JwtAuthFilter jwtAuthFilter;

    @Autowired
    @Qualifier("sellerUserDetailsService")
    private UserDetailsService sellerUserDetailsService;

    public SecurityConfig(JwtAuthEntryPoint unauthorizedHandler, JwtAuthFilter jwtAuthFilter) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtAuthFilter = jwtAuthFilter;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(sellerUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize


                        .requestMatchers("/h2-console/**").permitAll()

                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/purchases/**").permitAll()
                        .requestMatchers("/api/inventory/**").permitAll()
                        .requestMatchers("/api/perfumes/**").permitAll()
                        .anyRequest().authenticated()
                );


        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);



        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));


        return http.build();
    }
}
