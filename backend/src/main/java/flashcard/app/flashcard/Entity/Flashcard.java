package flashcard.app.flashcard.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "flashcards")
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "front", length = 1000)
    private String front;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "verse", length = 1000)
    private String verse;

    @Column(name = "date")
    private LocalDate localDate;

    @Column(name = "front_image")
    private String frontImage;

    @Column(name = "back_image")
    private String backImage;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;

    private int boxLevel = 1;

    private LocalDate nextReviewDate = LocalDate.now();

    private LocalDate lastReviewDate = LocalDate.now();

    public Flashcard(Long id, String front, String verse, String frontImage, String backImage){
        this.id = id;
        this.front = front;
        this.verse = verse;
        this.frontImage = frontImage;
        this.backImage = backImage;
    }

}
