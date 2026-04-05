package flashcard.app.flashcard.Repository;

import flashcard.app.flashcard.Dto.DeckDtos.DeckListDto;
import flashcard.app.flashcard.Entity.Deck;
import flashcard.app.flashcard.Entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

@DataJpaTest
@ActiveProfiles("test")
class DeckRepositoryTest {

        @Autowired
        private DeckRepository deckRepository;

        @Autowired
        private TestEntityManager entityManager;

        @Test
        void deckList_ShouldReturnCorrectDtos() {
            User user = new User();
            user.setEmail("test@email.com");
            user.setPassword("1234");
            entityManager.persist(user);

            Deck deck = new Deck();
            deck.setTitle("Integration Test Deck");
            deck.setUser(user);
            Deck deck2 = new Deck();
            deck2.setTitle("Integration Test Deck 2");
            deck2.setUser(user);
            entityManager.persist(deck);
            entityManager.persist(deck2);
            entityManager.flush();

            List<DeckListDto> results = deckRepository.deckList(user.getId());

            Assertions.assertThat(results).isNotEmpty();
            Assertions.assertThat(results.getFirst().title()).isEqualTo("Integration Test Deck");
            Assertions.assertThat(results.get(1).title()).isEqualTo("Integration Test Deck 2");
        }

    @Test
    @Sql("/test-data.sql")
    void deckList_ShouldReturnCorrectDtosFromSqlFile() {
        UUID user1Id = UUID.fromString("0987e654-e89b-12d3-a456-426614174000");

        List<DeckListDto> results = deckRepository.deckList(user1Id);

        Assertions.assertThat(results).hasSize(2);
        Assertions.assertThat(results).extracting(DeckListDto::title).containsExactlyInAnyOrder("French Vocabulary", "Biology 101");
    }
    }