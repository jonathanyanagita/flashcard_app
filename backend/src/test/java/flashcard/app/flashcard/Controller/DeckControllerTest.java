package flashcard.app.flashcard.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import flashcard.app.flashcard.Dto.DeckDtos.DeckCreateDto;
import flashcard.app.flashcard.Entity.Deck;
import flashcard.app.flashcard.Repository.UserRepository;
import flashcard.app.flashcard.Service.DeckService;
import flashcard.app.flashcard.Service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(DeckController.class)
public class DeckControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    DeckService  deckService;

    @MockitoBean
    UserRepository  userRepository;

    @MockitoBean
    TokenService  tokenService;

    @Test
    @WithMockUser
    void addDeck_WhenValidRequest_ShouldReturnSavedDeck() throws Exception {
        UUID userId = UUID.randomUUID();
        DeckCreateDto dto = new DeckCreateDto(userId, "My New Deck");
        UUID deckId = UUID.randomUUID();
        Deck savedDeck = new Deck();
        savedDeck.setId(deckId);
        savedDeck.setTitle("My New Deck");

        when(deckService.addDeck(any(DeckCreateDto.class))).thenReturn(savedDeck);

        mockMvc.perform(post("/decks/add")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(deckId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("My New Deck"));

        verify(deckService).addDeck(any(DeckCreateDto.class));
    }
}
