package cl.duoc.rodrcruz.usersellermodule.controller;

import cl.duoc.rodrcruz.usersellermodule.controller.request.UserRequest;
import cl.duoc.rodrcruz.usersellermodule.controller.response.UserResponse;
import cl.duoc.rodrcruz.usersellermodule.repository.UserDB;
import cl.duoc.rodrcruz.usersellermodule.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest request) {
        UserDB nuevo= userService.registerUser(request);
        UserResponse userResponse = convertToUserResponse(nuevo);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);

    }
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserDB> usersDB = userService.findAllUsers();
        List<UserResponse> userResponses = usersDB.stream().map(this::convertToUserResponse).collect(Collectors.toList());
        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Integer id, @RequestBody UserRequest request) {
        try {
            UserDB updatedUserDB = userService.updateUser(id, request);
            UserResponse userResponse = convertToUserResponse(updatedUserDB);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("Usuario no encontrado")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                UserResponse errorResponse = new UserResponse();
                errorResponse.setName(e.getMessage());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
        }
    }
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id) {
        try {
            UserDB userDB = userService.getUserById(id);
            UserResponse userResponse = convertToUserResponse(userDB);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    private UserResponse convertToUserResponse(UserDB user) {
        if (user == null) {
            return null;
        }
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setLastname(user.getLastname());
        response.setAge(user.getAge());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRegistrationDate(user.getRegistrationDate());
        response.setRoleName(user.getRole() != null ? user.getRole().getName() : null);
        return response;
    }
}
