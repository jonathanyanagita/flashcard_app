package flashcard.app.flashcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FlashcardApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlashcardApplication.class, args);
	}

}
