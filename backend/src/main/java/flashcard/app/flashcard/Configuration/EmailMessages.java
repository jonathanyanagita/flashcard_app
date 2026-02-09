package flashcard.app.flashcard.Configuration;

import flashcard.app.flashcard.Dto.EmailContentDto;

public class EmailMessages {

    public static EmailContentDto registration(String token) {
        return new EmailContentDto(
                "Confirm your email address!",
                "Hello,\n\nThanks for joining! Use this token to confirm your account:\n" + token
        );
    }

    public static EmailContentDto forgotPassword(String token) {
        return new EmailContentDto(
                "Email recovery!",
                "Hello,\n\nUse the token below to create a new password:\n" + token + "\n\nToken valid for 3 hours"
        );
    }
}
