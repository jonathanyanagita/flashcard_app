Flashcard App

Technology Stack
Frontend: React Native (Expo)
Backend: Java 21, Spring Boot
Database: MySQL / Amazon RDS
Authentication: JWT (Spring Security)
Storage: Firebase
OS: Android

Functional Requirements
FR-01 – User Registration
Users must be able to create an account using: email and password. System must validate email format and password strength.

FR-02 – User Authentication
Users must be able to log in using email and password. Protected endpoints must require authentication.

FR-03 - Password Recovery
Users must be able to request a password reset using their email. The system must validate a recovery token and allow the user to define a new password.

FR-04 – Session Persistence
Users must remain logged in after closing the app. 

FR-05 – User Delete
Users must be able to delete their account

FR-06 – Create Deck
Users must be able to create a deck with a name.

FR-07 – View Decks
Users must be able to see a list of their decks. Each deck must show the total number of flashcards and number of flashcards due for review

FR-08 – Update Deck
Users must be able to edit deck name.

FR-09 – Delete Deck
Users must be able to delete a deck. Deleting a deck must also delete its flashcards.

FR-10 – Create Flashcard
Users must be able to create flashcards inside a deck. Each flashcard must contain a front text and a back text, with optional image. Initial box level (default: 1)

FR-11 – Edit Flashcard
Users must be able to update front/back text and image.

FR-12 – Delete Flashcard
Users must be able to delete a flashcard from a deck.

FR-13 – Spaced Repetition Boxes
Flashcards must be organized into box levels. Each box defines a review interval: Box 1: every day; Box 2: every 2 days; Box 3: every 4 days; Box 4: every 7 days; Box 5: every 15 days

FR-14 – Review Session
Users must be able to flip flashcards and mark flashcards if remembered or not.

FR-15 – Review Result Handling
If user mark as remember, flashcard moves to the next box level. If not, flashcard moves back to box level 1. Next review date must be recalculated.

Non-Functional Requirements
NFR 01 - Security
Passwords must be hashed using BCrypt. JWT tokens must expire. Users must only access their own decks and flashcards.

NFR 02 - Usability
UI must be responsive. One-handed usage friendly. Dark mode support

Future Enhancements
Shared decks (Library)
Rich text formatting
Import/export decks
