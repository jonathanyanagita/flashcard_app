package flashcard.app.flashcard.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardCreateDto;
import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardEditDto;
import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardResponseDto;
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

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
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

    @Test
    @WithMockUser
    void deleteFlashcard_WhenValidRequest_ShouldReturnNoContent() throws Exception{
        UUID flashcardId = UUID.randomUUID();

        doNothing().when(flashcardService).deleteFlashcard(flashcardId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/flashcards/delete/{flashcardID}", flashcardId)
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(flashcardService).deleteFlashcard(flashcardId);
    }

    @Test
    @WithMockUser
    void editFlashcard_WhenValidRequest_ShouldReturnOk() throws Exception{
        UUID flashcardId = UUID.randomUUID();
        FlashcardEditDto dto = new FlashcardEditDto("Front","Back",null,null);

        doNothing().when(flashcardService).editFlashcard(flashcardId, dto);

        mockMvc.perform(MockMvcRequestBuilders.put("/flashcards/edit/{flashcardId}", flashcardId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                        .andExpect(MockMvcResultMatchers.status().isOk());

        verify(flashcardService).editFlashcard(flashcardId, dto);
    }

    @Test
    @WithMockUser
    void getFlashcards_WhenValidRequest_ShouldReturnListOfFlashcardsAndOk() throws Exception{
        UUID deckId = UUID.randomUUID();
        List<FlashcardResponseDto> list = List.of(
                new FlashcardResponseDto(UUID.randomUUID(), "Front 1", "Back 1", "frontimage1.png", "backimage.png"),
                new FlashcardResponseDto(UUID.randomUUID(), "Front 2", "Back 2", "frontimage2.png", "backimage.png")
        );

        when(flashcardService.getFlashcards(deckId)).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/flashcards/get/{deckId}", deckId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].front").value("Front 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].frontImage").value("frontimage1.png"));

        verify(flashcardService).getFlashcards(deckId);
    }
}
