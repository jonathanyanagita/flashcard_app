package flashcard.app.flashcard.Controller;

import flashcard.app.flashcard.Dto.UserCreateDto;
import flashcard.app.flashcard.Dto.UserGetDto;
import flashcard.app.flashcard.Entity.User;
import flashcard.app.flashcard.Repository.UserRepository;
import flashcard.app.flashcard.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    public UserController(UserService userService, AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserCreateDto userCreateDto) {
        try {
            userService.registerUser(userCreateDto);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserCreateDto dto) {
        User user = dto.newUserMapper();
        userService.saveUser(user);
        // URI uri = http://localhost:8080/decks/{id}
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetDto> getUserById(@PathVariable("id") String id) {
        var userId = UUID.fromString(id);
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserGetDto dto = new UserGetDto(
                    user.getId(),user.getEmail(),user.getPassword()
            );
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") String id){
        var userId = UUID.fromString(id);
        Optional<User> userOptional = userService.getUserById(userId);

        if(userOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        userService.deleteUser(userOptional.get());

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@Valid @PathVariable("id") String id, @RequestBody UserCreateDto dto) {
        var userId = UUID.fromString(id);
        Optional<User> userOptional = userService.getUserById(userId);

            if(userOptional.isEmpty()){
                return ResponseEntity.notFound().build();
            }

            var user = userOptional.get();
            user.setEmail(dto.email());
            user.setPassword(dto.password());

            userService.updateUser(user);

        return ResponseEntity.noContent().build();
    }
}
