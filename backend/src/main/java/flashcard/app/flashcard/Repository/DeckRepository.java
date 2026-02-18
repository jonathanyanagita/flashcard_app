package flashcard.app.flashcard.Repository;

import flashcard.app.flashcard.Dto.DeckDtos.DeckListDto;
import flashcard.app.flashcard.Entity.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeckRepository extends JpaRepository<Deck, UUID> {

    @Query("SELECT new flashcard.app.flashcard.Dto.DeckDtos.DeckListDto(d.id, d.title) FROM Deck d WHERE d.user.id = :id")
    List<DeckListDto> deckList(@Param("id") UUID id);

}
