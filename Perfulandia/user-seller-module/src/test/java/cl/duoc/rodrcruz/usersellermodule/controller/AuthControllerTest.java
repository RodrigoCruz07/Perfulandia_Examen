package cl.duoc.rodrcruz.usersellermodule.controller;

import cl.duoc.rodrcruz.usersellermodule.controller.request.AuthRequest;
import cl.duoc.rodrcruz.usersellermodule.controller.request.RegisterRequest;
import cl.duoc.rodrcruz.usersellermodule.controller.response.AuthResponse;
import cl.duoc.rodrcruz.usersellermodule.repository.RoleDB;
import cl.duoc.rodrcruz.usersellermodule.repository.SellerDB;
import cl.duoc.rodrcruz.usersellermodule.repository.UserDB;
import cl.duoc.rodrcruz.usersellermodule.security.JwtUtil;
import cl.duoc.rodrcruz.usersellermodule.service.SellerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;



@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = AuthController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class
        })
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private SellerService sellerService;

    private AuthRequest authRequestOk;
    private AuthResponse authResponseOk;
    private RegisterRequest registerRequestOk;
    private SellerDB registeredSellerDB;
    @BeforeEach
    void setUp() {
        authRequestOk = new AuthRequest("testuser@example.com", "password123");
        authResponseOk = new AuthResponse("mocked.jwt.token", "Login exitoso");

        registerRequestOk = new RegisterRequest();
        registerRequestOk.setName("Nuevo");
        registerRequestOk.setLastname("Vendedor");
        registerRequestOk.setEmail("nuevoseller@example.com");
        registerRequestOk.setPassword("sellerPass");
        registerRequestOk.setPhone("123456789");
        registerRequestOk.setAge(30);
        RoleDB sellerRole = new RoleDB(2, "VENDEDOR");
        registeredSellerDB = new SellerDB();
        registeredSellerDB.setId(101);
        registeredSellerDB.setName(registerRequestOk.getName());
        registeredSellerDB.setLastname(registerRequestOk.getLastname());
        registeredSellerDB.setEmail(registerRequestOk.getEmail());
        registeredSellerDB.setPassword(registerRequestOk.getPassword());
        registeredSellerDB.setPhone(registerRequestOk.getPhone());
        registeredSellerDB.setAge(registerRequestOk.getAge());
        registeredSellerDB.setRole(sellerRole);
    }

    @Test
    void authenticateUser()throws Exception {
        // Given
        Authentication authenticationMock = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authenticationMock);

        UserDetails userDetails = new User(authRequestOk.getUsername(), authRequestOk.getPassword(), Collections.emptyList());
        when(authenticationMock.getPrincipal()).thenReturn(userDetails);
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn(authResponseOk.getToken());

        // When
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequestOk))
                        .with(csrf()))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(authResponseOk.getToken()))
                .andExpect(jsonPath("$.message").value(authResponseOk.getMessage()));
    }


    @Test
    void authenticateUser_badCredentials() throws Exception {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));
        AuthRequest invalidAuthRequest = new AuthRequest("wronguser@example.com", "wrongpassword");

        // When
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAuthRequest))
                        .with(csrf()))
                // Then
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.jwt").doesNotExist())
                .andExpect(jsonPath("$.message").value("Email o contraseña inválidos"));
    }

    @Test
    void authenticateUser_genericError() throws Exception {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Algún error inesperado en la autenticación."));

        // When
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequestOk))
                        .with(csrf()))
                // Then
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.jwt").doesNotExist())
                .andExpect(jsonPath("$.message").value("Error de autenticación: Algún error inesperado en la autenticación."));
    }
    @Test
    void registerSeller()throws Exception  {
        // Given
        when(sellerService.registerNewSeller(any(RegisterRequest.class)))
                .thenReturn(registeredSellerDB);

        // When
        mockMvc.perform(post("/api/auth/register/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestOk))
                        .with(csrf()))
                // Then
                .andExpect(status().isCreated())
                .andExpect(content().string("Vendedor '" + registeredSellerDB.getEmail() +
                        "' registrado exitosamente con rol '" + registeredSellerDB.getRole().getName() +
                        "'. Contraseña generada: " + registeredSellerDB.getPassword()));
    }
    @Test
    void registerSeller_responseStatusException() throws Exception {
        // Given
        String errorMessage = "El email 'nuevoseller@example.com' ya está registrado.";
        when(sellerService.registerNewSeller(any(RegisterRequest.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, errorMessage));

        // When
        mockMvc.perform(post("/api/auth/register/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestOk))
                        .with(csrf()))
                // Then
                .andExpect(status().isConflict())
                .andExpect(content().string(errorMessage));
    }
    @Test
    void registerSeller_genericError() throws Exception {
        // Given
        String errorMessage = "Error interno del servidor al procesar el registro.";
        when(sellerService.registerNewSeller(any(RegisterRequest.class)))
                .thenThrow(new RuntimeException(errorMessage));

        // When
        mockMvc.perform(post("/api/auth/register/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestOk))
                        .with(csrf()))
                // Then
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al registrar vendedor: " + errorMessage));
    }
}