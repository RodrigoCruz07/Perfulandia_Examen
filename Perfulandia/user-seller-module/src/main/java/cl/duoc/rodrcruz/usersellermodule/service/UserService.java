package cl.duoc.rodrcruz.usersellermodule.service;

import cl.duoc.rodrcruz.usersellermodule.controller.request.UserRequest;
import cl.duoc.rodrcruz.usersellermodule.repository.RoleDB;
import cl.duoc.rodrcruz.usersellermodule.repository.RoleJpaRepository;
import cl.duoc.rodrcruz.usersellermodule.repository.UserDB;
import cl.duoc.rodrcruz.usersellermodule.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private RoleJpaRepository roleJpaRepository;


    public UserDB registerUser(UserRequest user) {
        List<UserDB> found = userJpaRepository.findByNameOrEmail(user.getName(), user.getEmail());
        if (!found.isEmpty()) {
            return found.get(0);
        }
        UserDB newUser = new UserDB();
        newUser.setName(user.getName());
        newUser.setLastname(user.getLastname());
        newUser.setAge(user.getAge());
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhone());
        newUser.setRegistrationDate(LocalDate.now());
        Optional<RoleDB> defaultRoleOptional = roleJpaRepository.findByName("USUARIO");
        if (defaultRoleOptional.isPresent()) {
            newUser.setRole(defaultRoleOptional.get());
        }
        return userJpaRepository.save(newUser);
    }

    public UserDB getUserById(Integer id) {
        return userJpaRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }
    public UserDB updateUser(Integer id,UserRequest user) {
        UserDB found = userJpaRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        if (user.getName() != null && !user.getName().isEmpty()) {
            found.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            found.setEmail(user.getEmail());
        }
        if (user.getLastname() != null && !user.getLastname().isEmpty()) {
            found.setLastname(user.getLastname());
        }
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            found.setPhone(user.getPhone());
        }
        if (user.getAge() != null) {
            found.setAge(user.getAge());
        }
        if (user.getRoleName() != null && !user.getRoleName().isEmpty()) {
            Optional<RoleDB> newRole = roleJpaRepository.findByName(user.getRoleName());
            if (newRole.isPresent()) {
                found.setRole(newRole.get());
            }

        }
        return userJpaRepository.save(found);
    }
    public boolean deleteUser(Integer id) {
        UserDB found = userJpaRepository.findById(id).orElse(null);
        if (found != null) {
            userJpaRepository.delete(found);
            return true;
        }
        return false;

    }
    public List<UserDB> findAllUsers() {
        return userJpaRepository.findAll();
    }}

