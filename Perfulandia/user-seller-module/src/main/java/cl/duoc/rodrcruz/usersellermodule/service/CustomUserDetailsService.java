package cl.duoc.rodrcruz.usersellermodule.service;
import cl.duoc.rodrcruz.usersellermodule.repository.SellerDB;
import cl.duoc.rodrcruz.usersellermodule.repository.SellerJpaRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;


import java.util.Collections;

@Service("sellerUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    @Autowired
    private SellerJpaRepository sellerJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        SellerDB seller = sellerJpaRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Vendedor no encontrado con email: " + email));
        String retrievedPassword = seller.getPassword();

        String plainTextPasswordForTest = "password";
        BCryptPasswordEncoder tempEncoder = new BCryptPasswordEncoder();
        boolean matchesDirectly = tempEncoder.matches(plainTextPasswordForTest, retrievedPassword);

        String roleNameWithPrefix = "ROLE_" + seller.getRole().getName().toUpperCase();
        GrantedAuthority authority = new SimpleGrantedAuthority(roleNameWithPrefix);

        logger.info("Usuario '{}' cargado con rol: {}", seller.getEmail(), roleNameWithPrefix);


        return new User(seller.getEmail(), seller.getPassword(), Collections.singletonList(authority));
    }
}