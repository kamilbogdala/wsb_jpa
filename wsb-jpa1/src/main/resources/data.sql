INSERT INTO address (id, city, address_line1, address_line2, postal_code)
VALUES
    (1, 'city', 'xx', 'yy', '62-030'),
    (2, 'Wroclaw', 'ul. Trzebnicka', '78/9', '52-225'),
    (3, 'Krakow', 'ul. Dluga', '10/22', '31-146'),
    (4, 'Gdansk', 'ul. Internetowa', '57', '80-298');

INSERT INTO doctor (id, first_name, last_name, telephone_number, email, doctor_number, specialization, address_id)
VALUES
    (1, 'Pablo', 'Baginski', '111222333', 'pbaginski@gmail.com', 'D0010', 'Radiolog', 2),
    (2, 'Jan', 'Nowak', '444555666', 'jnowak@gmail.com', 'D0020', 'Kardiolog', 3),
    (3, 'Andrzej', 'Kowalski', '777888999', 'skowalski@gmail.com', 'D0030', 'Okulista', 4);

INSERT INTO patient (id, first_name, last_name, telephone_number, email, patient_number, date_of_birth, address_id, weight)
VALUES
    (1, 'Kamil', 'Stoch', '123456789', 'kstoch@gmail.com', 'P0001', '1989-05-15', 2, 98),
    (2, 'Piotr', 'Żyła', '214365879', 'pzula@gmail.com', 'P0002', '1994-11-01', 3, 76),
    (3, 'Maciej', 'Kot', '918273645', 'mkot@gmail.com', 'P0003', '2001-01-01', 4, 53);

INSERT INTO visit (id, description, time, doctor_id, patient_id)
VALUES
    (1, 'Badanie USG', '2024-12-01 12:45:00', 1, 1),
    (2, 'Echo serca', '2024-12-02 11:15:00', 2, 2),
    (3, 'Badanie wzroku', '2024-12-03 08:30:00', 3, 3);

INSERT INTO medical_treatment (id, description, type, visit_id)
VALUES
    (1, 'Badanie ultrasonograficzne (USG) wykonywane przy pomocy ultrasonografu.', 'Diagnostyczne', 1),
    (2, 'Elektrokardiogram (EKG) - zarejestrowana elektryczna aktywność serca.', 'Diagnostyczne', 2),
    (3, 'Coroczna kontrola wzrokuuu.', 'Diagnostyczne', 3);
