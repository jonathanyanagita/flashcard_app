package flashcard.app.flashcard.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import flashcard.app.flashcard.Dto.DeckDtos.DeckCreateDto;
import flashcard.app.flashcard.Dto.DeckDtos.DeckEditDto;
import flashcard.app.flashcard.Dto.DeckDtos.DeckListDto;
import flashcard.app.flashcard.Entity.Deck;
import flashcard.app.flashcard.Entity.User;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

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

        mockMvc.perform(MockMvcRequestBuilders.post("/decks/add")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(deckId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("My New Deck"));

        verify(deckService).addDeck(any(DeckCreateDto.class));
    }

    @Test
    @WithMockUser
    void listDecks_WhenValidRequest_ShouldReturnAllDecks() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        List<DeckListDto> list = List.of(
                new DeckListDto(UUID.randomUUID(), "Deck Test 1"),
                new DeckListDto(UUID.randomUUID(), "Deck Test 2"));

        when(deckService.listDecks(any(UUID.class))).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/decks/list/{userId}", userId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Deck Test 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Deck Test 2"));

        verify(deckService).listDecks(userId);
    }

    @Test
    @WithMockUser
    void deleteDeck_WhenValidRequest_ShouldReturnNoContent() throws Exception {
        UUID deckId = UUID.randomUUID();

        doNothing().when(deckService).deleteDeck(any(UUID.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/decks/delete/{deckId}", deckId)
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser
    void editDeck_WhenValidRequest_ShouldReturnOk() throws Exception{
        UUID deckId = UUID.randomUUID();
        DeckEditDto dto = new DeckEditDto("Edited Title");
        Deck editedDeck = new Deck();
        editedDeck.setId(deckId);
        editedDeck.setTitle("Edited Title");

        when(deckService.editDeckTitle(deckId, dto)).thenReturn(editedDeck);

        mockMvc.perform(MockMvcRequestBuilders.put("/decks/edit/{deckId}", deckId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Edited Title"));

        verify(deckService).editDeckTitle(deckId, dto);
    }
}
