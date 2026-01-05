## Flashcard App

<b>Technology Stack</b><br>
Frontend: React Native (Expo)<br>
Backend: Java 21, Spring Boot<br>
Database: MySQL / Amazon RDS<br>
Authentication: JWT (Spring Security)<br>
Storage: Firebase<br>
OS: Android<br>

<b>Functional Requirements</b><br>
FR-01 – User Registration<br>
Users must be able to create an account using: email and password. System must validate email format and password strength.

FR-02 – User Authentication<br>
Users must be able to log in using email and password. Protected endpoints must require authentication.

FR-03 - Password Recovery<br>
Users must be able to request a password reset using their email. The system must validate a recovery token and allow the user to define a new password.

FR-04 – Session Persistence<br>
Users must remain logged in after closing the app. 

FR-05 – User Delete<br>
Users must be able to delete their account

FR-06 – Create Deck<br>
Users must be able to create a deck with a name.

FR-07 – View Decks<br>
Users must be able to see a list of their decks. Each deck must show the total number of flashcards and number of flashcards due for review

FR-08 – Update Deck<br>
Users must be able to edit deck name.

FR-09 – Delete Deck<br>
Users must be able to delete a deck. Deleting a deck must also delete its flashcards.

FR-10 – Create Flashcard<br>
Users must be able to create flashcards inside a deck. Each flashcard must contain a front text and a back text, with optional image. Initial box level (default: 1)

FR-11 – Edit Flashcard<br>
Users must be able to update front/back text and image.

FR-12 – Delete Flashcard<br>
Users must be able to delete a flashcard from a deck.

FR-13 – Spaced Repetition Boxes<br>
Flashcards must be organized into box levels. Each box defines a review interval: Box 1: every day; Box 2: every 2 days; Box 3: every 4 days; Box 4: every 7 days; Box 5: every 15 days.

FR-14 – Review Session<br>
Users must be able to flip flashcards and mark flashcards if remembered or not.

FR-15 – Review Result Handling<br>
If user mark as remember, flashcard moves to the next box level. If not, flashcard moves back to box level 1. Next review date must be recalculated.

<b>Non-Functional Requirements</b><br>
NFR 01 - Security<br>
Passwords must be hashed using BCrypt. JWT tokens must expire. Users must only access their own decks and flashcards.

NFR 02 - Usability<br>
UI must be responsive. One-handed usage friendly. Dark mode support

<b>Future Enhancements</b><br>
Shared decks (Library)<br>
Rich text formatting<br>
Import/export decks<br>
