package flashcard.app.flashcard.Controller;

import flashcard.app.flashcard.Dto.UserDtos.*;
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserCreateDto userCreateDto) {
            userService.registerUser(userCreateDto);
            return ResponseEntity.ok().build();
    }

    @PutMapping("/register/resend")
    public ResponseEntity<?> resendEmail(@RequestBody ResendEmailDto resendEmailDto){
        userService.resendEmail(resendEmailDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/register/confirm")
    public ResponseEntity<?> confirmEmail(@RequestParam String token) {
            userService.confirmEmail(token);
            return ResponseEntity.ok("E-mail confirmed!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        var usernamepassword = new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.password());
        var auth = this.authenticationManager.authenticate(usernamepassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PutMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@Valid @RequestParam String email) {
        userService.forgotPassword(email);
        return ResponseEntity.ok("Check your email!");
    }

    @PutMapping("/newpassword")
    public ResponseEntity<?> newPassword(@Valid @RequestBody NewPasswordDto newPasswordDto) {
        userService.newPassword(newPasswordDto.token(),newPasswordDto.password());
        return ResponseEntity.ok("New Password!");
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
}
