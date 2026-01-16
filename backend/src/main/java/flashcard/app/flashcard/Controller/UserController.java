package flashcard.app.flashcard.Controller;

import flashcard.app.flashcard.Dto.DeckCreateDto;
import flashcard.app.flashcard.Dto.UserCreateDto;
import flashcard.app.flashcard.Dto.UserGetDto;
import flashcard.app.flashcard.Entity.Deck;
import flashcard.app.flashcard.Entity.User;
import flashcard.app.flashcard.Repository.UserRepository;
import flashcard.app.flashcard.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserCreateDto dto) {
        User user = dto.newUserMapper();
        userService.saveUser(user);
        // URI uri = http://localhost:8080/decks/{id}
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping({"/{id}"})
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
}
