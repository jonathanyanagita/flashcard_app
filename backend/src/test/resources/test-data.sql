INSERT INTO users (id, email, password)
VALUES ('0987e654-e89b-12d3-a456-426614174000', 'student1@test.com', 'pass');
INSERT INTO users (id, email, password)
VALUES ('0987e654-e89b-12d3-a456-426614174001', 'student2@test.com', 'pass');

INSERT INTO decks (id, title, user_id)
VALUES ('54a964bd-9967-4adc-85d7-f00548492431', 'French Vocabulary', '0987e654-e89b-12d3-a456-426614174000');
INSERT INTO decks (id, title, user_id)
VALUES (random_uuid(), 'Biology 101', '0987e654-e89b-12d3-a456-426614174000');
INSERT INTO decks (id, title, user_id)
VALUES (random_uuid(), 'Modern History', '0987e654-e89b-12d3-a456-426614174001');

INSERT INTO flashcards (id, front, verse, created_date, front_image, back_image, deck_id, box_level, next_review_date, last_review_date)
VALUES (random_uuid(), 'Bonjour', 'Hello', CURRENT_DATE, 'france_flag.png', 'person_waving.png', '54a964bd-9967-4adc-85d7-f00548492431', 1, CURRENT_DATE, CURRENT_DATE);
INSERT INTO flashcards (id, front, verse, created_date, front_image, back_image, deck_id, box_level, next_review_date, last_review_date)
VALUES (random_uuid(), 'What is the powerhouse of the cell?', 'Mitochondria', CURRENT_DATE, 'mitochondria_diagram.png', NULL, '54a964bd-9967-4adc-85d7-f00548492431', 2, DATEADD('DAY', 2, CURRENT_DATE), CURRENT_DATE);
INSERT INTO flashcards (id, front, verse, created_date, front_image, back_image, deck_id, box_level, next_review_date, last_review_date)
VALUES (random_uuid(), 'Year of the Moon Landing', '1969', CURRENT_DATE, 'moon.jpg', NULL, '54a964bd-9967-4adc-85d7-f00548492431', 1, CURRENT_DATE, CURRENT_DATE);
INSERT INTO flashcards (id, front, verse, created_date, front_image, back_image, deck_id, box_level, next_review_date, last_review_date)
VALUES (random_uuid(), 'How to declare a constant in Java?', 'final keyword', CURRENT_DATE, NULL, NULL, '54a964bd-9967-4adc-85d7-f00548492431', 3, DATEADD('DAY', 7, CURRENT_DATE), CURRENT_DATE);