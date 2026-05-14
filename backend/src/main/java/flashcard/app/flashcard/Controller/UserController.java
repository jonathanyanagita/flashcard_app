package flashcard.app.flashcard.Controller;

import flashcard.app.flashcard.Dto.UserDtos.*;
import flashcard.app.flashcard.Entity.User;
import flashcard.app.flashcard.Mapper.UserMapper;
import flashcard.app.flashcard.Repository.UserRepository;
import flashcard.app.flashcard.Service.TokenService;
import flashcard.app.flashcard.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@Tag(name = "User", description = "Endpoints for user registration, authentication, and management.")
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
    @Operation(summary = "Register a new user", description = "Registers a new user and sends a confirmation email.")
    public ResponseEntity<?> register(@Valid @RequestBody UserCreateDto userCreateDto) {
            userService.registerUser(userCreateDto);
            return ResponseEntity.ok().build();
    }

    @PutMapping("/register/resend")
    @Operation(summary = "Resend confirmation email", description = "Resends the confirmation email to the user.")
    public ResponseEntity<?> resendEmail(@RequestBody ResendEmailDto resendEmailDto){
        userService.resendEmail(resendEmailDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/register/confirm")
    @Operation(summary = "Confirm email", description = "Confirms the user's email using the provided token.")
    public ResponseEntity<?> confirmEmail(@RequestParam String token) {
            userService.confirmEmail(token);
            return ResponseEntity.ok("E-mail confirmed!");
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates the user and returns a JWT token.")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        var usernamepassword = new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.password());
        var auth = this.authenticationManager.authenticate(usernamepassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PutMapping("/forgot")
    @Operation(summary = "Forgot password", description = "Initiates the forgot password process by sending a reset email to the user.")
    public ResponseEntity<?> forgotPassword(@Valid @RequestParam String email) {
        userService.forgotPassword(email);
        return ResponseEntity.ok("Check your email!");
    }

    @PutMapping("/newpassword")
    @Operation(summary = "Set new password", description = "Sets a new password for the user using the provided token.")
    public ResponseEntity<?> newPassword(@Valid @RequestBody NewPasswordDto newPasswordDto) {
        userService.newPassword(newPasswordDto.token(),newPasswordDto.password());
        return ResponseEntity.ok("New Password!");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a user's information by their unique ID.")
    public ResponseEntity<UserGetDto> getUserById(@PathVariable("id") UUID id) {
        Optional<User> userOptional = userService.getUserById(id);

        return userService
                .getUserById(id)
                .map(user -> {
                    UserGetDto userGetDto = userMapper.toUserGetDto(user);
                    return ResponseEntity.ok(userGetDto);
        }).orElseGet ( () -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by ID", description = "Deletes a user from the system using their unique ID.")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") UUID id){
        Optional<User> userOptional = userService.getUserById(id);

        if(userOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        userService.deleteUser(userOptional.get());
        return ResponseEntity.noContent().build();
    }
}
