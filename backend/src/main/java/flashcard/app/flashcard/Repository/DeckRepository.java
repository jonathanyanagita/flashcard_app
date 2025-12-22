package flashcard.app.flashcard.Repository;

import flashcard.app.flashcard.Entity.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeckRepository extends JpaRepository<Deck,Long> {


}
