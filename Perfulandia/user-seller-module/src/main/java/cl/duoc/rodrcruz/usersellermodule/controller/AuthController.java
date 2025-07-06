package cl.duoc.rodrcruz.usersellermodule.controller;
import cl.duoc.rodrcruz.usersellermodule.controller.request.AuthRequest;
import cl.duoc.rodrcruz.usersellermodule.controller.request.RegisterRequest;
import cl.duoc.rodrcruz.usersellermodule.controller.response.AuthResponse;
import cl.duoc.rodrcruz.usersellermodule.repository.SellerDB;
import cl.duoc.rodrcruz.usersellermodule.security.JwtUtil;
import cl.duoc.rodrcruz.usersellermodule.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private SellerService sellerService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest .getUsername(), authRequest.getPassword())
            );

            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            final String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new AuthResponse(jwt, "Login exitoso"));

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AuthResponse(null, "Email o contraseña inválidos"), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(new AuthResponse(null, "Error de autenticación: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register/seller")
    public ResponseEntity<String> registerSeller(@RequestBody RegisterRequest registerRequest) {
        try {
            SellerDB newSeller = sellerService.registerNewSeller(registerRequest);
            return new ResponseEntity<>("Vendedor '" + newSeller.getEmail() + "' registrado exitosamente con rol '" + newSeller.getRole().getName() + "'. Contraseña generada: " + newSeller.getPassword(), HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>("Error al registrar vendedor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
