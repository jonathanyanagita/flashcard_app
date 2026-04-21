package flashcard.app.flashcard.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardResponseDto;
import flashcard.app.flashcard.Repository.UserRepository;
import flashcard.app.flashcard.Service.FlashcardService;
import flashcard.app.flashcard.Service.StudyService;
import flashcard.app.flashcard.Service.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

@WebMvcTest(StudyController.class)
public class StudyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    StudyService studyService;

    @MockitoBean
    FlashcardService flashcardService;

    @MockitoBean
    UserRepository userRepository;

    @MockitoBean
    TokenService tokenService;

    @Test
    @WithMockUser
    void getDueFlashcards_WhenValid_ShouldReturnDtosAndOk() throws Exception{
        UUID deckId = UUID.randomUUID();
        List<FlashcardResponseDto> dtos = List.of(
                new FlashcardResponseDto(UUID.randomUUID(),"Front 1","Back1","image.png",null),
                new FlashcardResponseDto(UUID.randomUUID(),"Front 2","Back2",null,"image.png"));

        Mockito.when(studyService.getDueFlashcards(deckId)).thenReturn(dtos);

        mockMvc.perform(MockMvcRequestBuilders.get("/study/get/{deckId}", deckId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].front").value("Front 1"));
    }
}
