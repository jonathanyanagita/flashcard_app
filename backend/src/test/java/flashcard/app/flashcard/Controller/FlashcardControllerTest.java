package flashcard.app.flashcard.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardCreateDto;
import flashcard.app.flashcard.Entity.Flashcard;
import flashcard.app.flashcard.Repository.UserRepository;
import flashcard.app.flashcard.Service.FlashcardService;
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

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(FlashcardController.class)
public class FlashcardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    FlashcardService flashcardService;

    @MockitoBean
    UserRepository userRepository;

    @MockitoBean
    TokenService tokenService;

    @Test
    @WithMockUser
    void addFlashcard_WhenValidRequest_ShouldReturnSavedFlashcardAndOk() throws Exception{
        UUID deckId =  UUID.randomUUID();
        FlashcardCreateDto dto = new FlashcardCreateDto("Front", "Back","frontimage.png","backimage.png");
        Flashcard newFlashcard = Flashcard.builder().front(dto.front()).verse(dto.verse()).frontImage(dto.frontImage()).backImage(dto.backImage()).build();

        when(flashcardService.addFlashcard(deckId, dto)).thenReturn(newFlashcard);

        mockMvc.perform(MockMvcRequestBuilders.post("/flashcards/add/{deckId}", deckId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.front").value("Front"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.verse").value("Back"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.frontImage").value("frontimage.png"));

        verify(flashcardService).addFlashcard(deckId, dto);
    }
}
