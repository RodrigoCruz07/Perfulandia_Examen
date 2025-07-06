package cl.duoc.rodrcruz.usersellermodule.service;

import cl.duoc.rodrcruz.usersellermodule.controller.request.RegisterRequest;
import cl.duoc.rodrcruz.usersellermodule.repository.RoleDB;
import cl.duoc.rodrcruz.usersellermodule.repository.RoleJpaRepository;
import cl.duoc.rodrcruz.usersellermodule.repository.SellerDB;
import cl.duoc.rodrcruz.usersellermodule.repository.SellerJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;




@Service
public class SellerService {
    @Autowired
    private SellerJpaRepository sellerJpaRepository;
    @Autowired
    private RoleJpaRepository roleJpaRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public SellerDB registerNewSeller(RegisterRequest registerRequest) {
        if (sellerJpaRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: ¡El email ya está en uso!");
        }


        String rawPasswordFromRequest = registerRequest.getPassword();

        String encodedPassword = passwordEncoder.encode(rawPasswordFromRequest);

        RoleDB sellerRole = roleJpaRepository.findByName(registerRequest.getRoleName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: ¡Rol '" + registerRequest.getRoleName() + "' no encontrado!"));

        SellerDB newSeller = new SellerDB();
        newSeller.setName(registerRequest.getName());
        newSeller.setLastname(registerRequest.getLastname());
        newSeller.setAge(registerRequest.getAge());
        newSeller.setEmail(registerRequest.getEmail());
        newSeller.setPhone(registerRequest.getPhone());
        newSeller.setAddress(registerRequest.getAddress());
        newSeller.setPassword(encodedPassword);
        newSeller.setRole(sellerRole);

        return sellerJpaRepository.save(newSeller);
    }


}
