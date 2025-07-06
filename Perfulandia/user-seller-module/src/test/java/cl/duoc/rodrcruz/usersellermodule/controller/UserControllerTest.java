package cl.duoc.rodrcruz.usersellermodule.controller;

import cl.duoc.rodrcruz.usersellermodule.controller.request.UserRequest;
import cl.duoc.rodrcruz.usersellermodule.controller.response.UserResponse;
import cl.duoc.rodrcruz.usersellermodule.repository.RoleDB;
import cl.duoc.rodrcruz.usersellermodule.repository.UserDB;
import cl.duoc.rodrcruz.usersellermodule.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private UserRequest testUserRequest;
    private UserDB testUserDB;
    private UserResponse testUserResponse;
    private RoleDB testRoleDB;

    @BeforeEach
    void setUp() {
        testRoleDB = new RoleDB(1, "USUARIO");

        testUserRequest = new UserRequest("Nuevo", "Usuario", 25, "nuevo.usuario@test.com", "123456789", "USUARIO");

        testUserDB = new UserDB(1, testUserRequest.getName(), testUserRequest.getLastname(), testUserRequest.getAge(),
                testUserRequest.getEmail(), testUserRequest.getPhone(),
                LocalDate.now(),
                testRoleDB);

        testUserResponse = new UserResponse(
                testUserDB.getId(),
                testUserDB.getName(),
                testUserDB.getLastname(),
                testUserDB.getAge(),
                testUserDB.getEmail(),
                testUserDB.getPhone(),
                testUserDB.getRegistrationDate(),
                testUserDB.getRole().getName()
        );
    }
    @Test
    void register_ok() {
        // Given
        given(userService.registerUser(any(UserRequest.class))).willReturn(testUserDB);

        // When
        ResponseEntity<UserResponse> response = userController.register(testUserRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        UserResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getId());
        assertEquals("Nuevo", body.getName());
        assertEquals("nuevo.usuario@test.com", body.getEmail());
        assertEquals("USUARIO", body.getRoleName());

        verify(userService, times(1)).registerUser(any(UserRequest.class));
    }


    @Test
    void getAllUsers() {
        // Given
        UserDB user2DB = new UserDB(2, "Otro", "User", 30, "otro@test.com", "987654321", LocalDate.now(), new RoleDB(2, "VENDEDOR"));
        List<UserDB> userDBList = Arrays.asList(testUserDB, user2DB);

        UserResponse user2Response = new UserResponse(
                user2DB.getId(),
                user2DB.getName(),
                user2DB.getLastname(),
                user2DB.getAge(),
                user2DB.getEmail(),
                user2DB.getPhone(),
                user2DB.getRegistrationDate(),
                user2DB.getRole().getName()
        );
        List<UserResponse> expectedResponses = Arrays.asList(testUserResponse, user2Response);

        given(userService.findAllUsers()).willReturn(userDBList);

        // When
        ResponseEntity<List<UserResponse>> response = userController.getAllUsers();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        List<UserResponse> body = response.getBody();
        assertNotNull(body);
        assertEquals(expectedResponses.size(), body.size());
        assertEquals(expectedResponses.get(0).getId(), body.get(0).getId());
        assertEquals(expectedResponses.get(1).getEmail(), body.get(1).getEmail());

        assertEquals(expectedResponses.get(0).getLastname(), body.get(0).getLastname());
        assertEquals(expectedResponses.get(1).getPhone(), body.get(1).getPhone());

        verify(userService, times(1)).findAllUsers();
    }

    @Test
    void updateUser() {
        // Given
        Integer userId = 1;
        UserRequest updatedRequest = new UserRequest("Updated", "User", 30, "updated@test.com", "99999", "ADMIN");
        UserDB updatedUserDB = new UserDB(userId, updatedRequest.getName(), updatedRequest.getLastname(), updatedRequest.getAge(),
                updatedRequest.getEmail(), updatedRequest.getPhone(),
                LocalDate.now(), new RoleDB(2, updatedRequest.getRoleName()));

        UserResponse expectedResponse = new UserResponse(
                userId,
                updatedRequest.getName(),
                updatedRequest.getLastname(),
                updatedRequest.getAge(),
                updatedRequest.getEmail(),
                updatedRequest.getPhone(),

                updatedUserDB.getRegistrationDate(),
                updatedRequest.getRoleName()
        );

        given(userService.updateUser(eq(userId), any(UserRequest.class))).willReturn(updatedUserDB);

        // When
        ResponseEntity<UserResponse> response = userController.updateUser(userId, updatedRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        UserResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(expectedResponse.getId(), body.getId());
        assertEquals(expectedResponse.getName(), body.getName());
        assertEquals(expectedResponse.getEmail(), body.getEmail());
        assertEquals(expectedResponse.getRoleName(), body.getRoleName());

        assertEquals(expectedResponse.getLastname(), body.getLastname());
        assertEquals(expectedResponse.getAge(), body.getAge());
        assertEquals(expectedResponse.getPhone(), body.getPhone());
        assertEquals(expectedResponse.getRegistrationDate(), body.getRegistrationDate());

        verify(userService, times(1)).updateUser(eq(userId), any(UserRequest.class));
    }
    @Test
    void updateUser_notFound() {
        // Given
        Integer nonExistentId = 999;
        UserRequest request = testUserRequest;

        given(userService.updateUser(eq(nonExistentId), any(UserRequest.class)))
                .willThrow(new RuntimeException("Usuario no encontrado con ID: " + nonExistentId));

        // When
        ResponseEntity<UserResponse> response = userController.updateUser(nonExistentId, request);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        assertNull(response.getBody());

        verify(userService, times(1)).updateUser(eq(nonExistentId), any(UserRequest.class));
    }
    @Test
    void updateUser_genericError_badRequest() {
        // Given
        Integer userId = 1;
        UserRequest request = testUserRequest;
        String errorMessage = "Error interno del servicio: No se pudo procesar la solicitud.";

        given(userService.updateUser(eq(userId), any(UserRequest.class)))
                .willThrow(new RuntimeException(errorMessage));

        // When
        ResponseEntity<UserResponse> response = userController.updateUser(userId, request);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        UserResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(errorMessage, body.getName());

        verify(userService, times(1)).updateUser(eq(userId), any(UserRequest.class));
    }

    @Test
    void getUserById() {
        // Given
        Integer userId = 1;
        given(userService.getUserById(eq(userId))).willReturn(testUserDB);

        // When
        ResponseEntity<UserResponse> response = userController.getUserById(userId);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        UserResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(testUserResponse.getId(), body.getId());
        assertEquals(testUserResponse.getName(), body.getName());
        assertEquals(testUserResponse.getEmail(), body.getEmail());
        assertEquals(testUserResponse.getRoleName(), body.getRoleName());

        assertEquals(testUserResponse.getLastname(), body.getLastname());
        assertEquals(testUserResponse.getAge(), body.getAge());
        assertEquals(testUserResponse.getPhone(), body.getPhone());
        assertEquals(testUserResponse.getRegistrationDate(), body.getRegistrationDate());

        verify(userService, times(1)).getUserById(eq(userId));
    }
    @Test
    void getUserById_notFound() {
        // Given
        Integer nonExistentId = 999;
        given(userService.getUserById(eq(nonExistentId)))
                .willThrow(new RuntimeException("Usuario no encontrado con ID: " + nonExistentId));

        // When
        ResponseEntity<UserResponse> response = userController.getUserById(nonExistentId);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        assertNull(response.getBody());

        verify(userService, times(1)).getUserById(eq(nonExistentId));
    }
    @Test
    void deleteUser() {
        // Given
        Integer userId = 1;
        when(userService.deleteUser(eq(userId))).thenReturn(true);

        // When
        ResponseEntity<Void> response = userController.deleteUser(userId);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
        assertNull(response.getBody());

        verify(userService, times(1)).deleteUser(eq(userId));
    }
    @Test
    void deleteUser_notFound() {
        // Given
        Integer nonExistentId = 999;
        when(userService.deleteUser(eq(nonExistentId))).thenReturn(false);

        // When
        ResponseEntity<Void> response = userController.deleteUser(nonExistentId);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        assertNull(response.getBody());

        verify(userService, times(1)).deleteUser(eq(nonExistentId));
    }
}