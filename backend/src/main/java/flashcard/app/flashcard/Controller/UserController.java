package flashcard.app.flashcard.Controller;

import flashcard.app.flashcard.Dto.LoginResponseDto;
import flashcard.app.flashcard.Dto.UserCreateDto;
import flashcard.app.flashcard.Dto.UserGetDto;
import flashcard.app.flashcard.Entity.User;
import flashcard.app.flashcard.Mapper.UserMapper;
import flashcard.app.flashcard.Repository.UserRepository;
import flashcard.app.flashcard.Service.TokenService;
import flashcard.app.flashcard.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final TokenService tokenService;
    private final UserMapper userMapper;

    public UserController(UserService userService, AuthenticationManager authenticationManager, UserRepository userRepository, TokenService tokenService, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserCreateDto userCreateDto){
        var usernamepassword = new UsernamePasswordAuthenticationToken(userCreateDto.email(), userCreateDto.password());
        var auth = this.authenticationManager.authenticate(usernamepassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserCreateDto userCreateDto) {
            userService.registerUser(userCreateDto);
            return ResponseEntity.ok().build();
    }

    @PutMapping("/register/confirm")
    public ResponseEntity<?> confirmEmail(@RequestParam String token) {
            userService.confirmEmail(token);
            return ResponseEntity.ok("E-mail confirmed!");
    }

    @PutMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@Valid @RequestParam String email) {

        userService.forgotPassword(email);
        return ResponseEntity.ok("Check your email!");

    }

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserCreateDto dto) {
        User user = userMapper.toEntity(dto);
        userService.saveUser(user);
        // URI uri = http://localhost:8080/decks/{id}
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetDto> getUserById(@PathVariable("id") String id) {
        var userId = UUID.fromString(id);
        Optional<User> userOptional = userService.getUserById(userId);

        return userService
                .getUserById(userId)
                .map(user -> {
                    UserGetDto userGetDto = userMapper.toUserGetDto(user);
                    return ResponseEntity.ok(userGetDto);
        }).orElseGet ( () -> ResponseEntity.notFound().build());

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
