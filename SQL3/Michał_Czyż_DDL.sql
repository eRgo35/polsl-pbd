-- Podstawy Baz Danych
-- Temat: SQL DDL
-- Przygotował: Michał Czyż
-- Grupa: 6
-- Sekcja: 13
-- Data wykonania: 25.04.2024


-- Zadanie 2

CREATE TABLE zesp1 (
    nr_zesp SMALLINT(6) PRIMARY KEY,
    nazwa_zep CHAR(30),
    nr_prac_kz INT(11),
    nr_int SMALLINT(6)
) Engine = InnoDB;

CREATE TABLE prac1(
    nr_prac INT(11) PRIMARY KEY,
    plec CHAR(1) NOT NULL,
    data_ur DATETIME NOT NULL,
    nazwisko CHAR(15),
    nr_zesp SMALLINT(6)   
) ENGINE = InnoDB;
CREATE INDEX nr_zesp_index ON prac1(nr_zesp);

-- Zadanie 3

INSERT INTO zesp1
SELECT * FROM ZESPOLY;

INSERT INTO prac1
SELECT * FROM PRACOWNICY;

-- Zadanie 4

ALTER TABLE zesp1
ADD CONSTRAINT FK_Zesp1
FOREIGN KEY(nr_prac_kz) 
REFERENCES prac1(nr_prac)
ON DELETE RESTRICT;

ALTER TABLE prac1
ADD CONSTRAINT FK_Prac1
FOREIGN KEY(nr_zesp) 
REFERENCES zesp1(nr_zesp)
ON DELETE SET NULL;

/*
      zesp1                      prac1
 ---------------             -------------
|  nr_zesp  k   |--    -----|  nr_prac k  |
|  nazwa_zesp   | |    |    |  plec       |
|  nr_prac_kz fk|<------    |  data_ur    |
|  nr_int       | |         |  nazwisko   |
 ---------------  --------->|  nr_zesp fk |
                             -------------
*/

-- Zadanie 5

-- Wprowadzanie wartości pustych

INSERT INTO zesp1 VALUES (NULL, 'ZESPOL', NULL, NULL);
-- próba wstawienia pustego zespołu
-- Column 'nr_zesp' cannot be NULL
-- Czyli klucz główny wymusza, że wartość nie może być NULL

INSERT INTO prac1 VALUES (NULL, NULL, NULL, 'Kowalski', NULL);
-- wstawinie nowego pracownika
-- klucz głowny nie może być NULL

INSERT INTO prac1 VALUES (0, NULL, NULL, 'Kowalski', NULL);
-- proba wstawienia nowego pracownika o nr 0, spełnia klucz główny
-- ale nie spełniony jest warunek plec NOT NULL

INSERT INTO prac1 VALUES (1, 'M', NULL, 'Kowalski', NULL);
-- to samo tylko teraz data_ur nie może być null

-- Wprowadzenie powtarzających się wartości

INSERT INTO prac1 VALUES (1, 'M', '2000-01-11', 'Kowalski', NULL);
-- klucz główny ma tą samą wartość co istniejący już rekord,
-- nie mogą istnieć duplikaty

INSERT INTO zesp1 VALUES (11, 'ZESPOL', NULL, NULL);
-- próba wstawienia duplikatu klucza
-- Duplicate entry for key PRIMARY
-- Klucz główny wymusza, że wprowadzany numer musi być unikalny

-- Usuwanie, wstawianie i modyfikacja

DELETE FROM prac1 WHERE nr_prac LIKE 41;
-- nie ma problemu z usuwaniem pracownika

INSERT INTO prac1 VALUES (100, 'M', '2000-01-11', 'KOWALSKI', 1);
-- dodanie nowego pracownika nie ma problemu

UPDATE prac1 SET nazwisko = 'NOWAK' WHERE nr_prac LIKE 100;
-- zmiana nazwiska z kowalskiego na nowaka działa

UPDATE prac1 SET nr_prac = 101 WHERE nr_prac LIKE 100;
-- nawet możliwa jest zmiana numeru pracownika, o ile nie jest duplikatem

DELETE FROM zesp1 WHERE nr_zesp LIKE 1;
-- usunięcie zespołu 1 wstawa NULL w miejsce nr_zesp w tabeli prac1

UPDATE zesp1 SET nr_zesp = 10 WHERE nr_zesp LIKE 2;
-- modyfikacja klucza głównego nr_zesp nie wykonuje się pomyślnie, ponieważ mamy RESTRICT

UPDATE zesp1 SET nazwa_zesp = 'ZMIANA ZESPOLU' WHERE nr_zesp LIKE 2;
-- modyfikacja innych wartości niż klucz główny przechodzi

UPDATE zesp1 SET nr_prac_kz = 1000 WHERE nr_zesp LIKE 2;
-- natomiast modyfkacja nr pracownika kierującego zespołem, nie jest możliwa z powodu na CONSTRAINT RESTRICT 

-- Zadanie 6
-- 6.a.
CREATE USER userX@localhost
GRANT SELECT ON pracownicy 
TO 'userX'@'localhost';

-- 6.b.
SELECT * FROM pracownicy;
-- polecenie wykonuje się prawidłowo

DELETE FROM pracownicy;
-- DELETE command denied to user
-- Nie ma uprawnień do usuwania

SELECT * FROM tematy;
-- SELECT command denied to user
-- Nie ma uprawnień do wyświetlania / dostępu

-- 6.c
GRANT DELETE 
ON zespoly 
TO 'userX'@'localhost';

-- 6.d
SELECT * FROM zespoly;
-- userX nie ma uprawnień do wyświetlania tabeli zespoly

-- 6.e
GRANT SELECT 
ON zespoly 
TO 'userX'@'localhost';

-- 6.f
DELETE FROM zespoly WHERE nr_zesp LIKE 1;
-- rekord usunął się pomyślnie, ponieważ już wcześniej nadaliśmy GRANT DELETE

-- 6.g
CREATE VIEW stud AS
SELECT s.nr_stud, s.nazwisko, AVG(o.ocena) 
FROM studenci s 
INNER JOIN oceny o 
ON s.nr_stud = o.nr_stud 
GROUP BY s.nr_stud, s.nazwisko;

GRANT SELECT 
ON stud 
TO 'userX'@'localhost';

-- 6.h

SELECT * FROM stud;
-- wyświetla się prawidłowo

SELECT * FROM studenci;
-- wyświetla brak uprawnień

UPDATE stud
SET nazwisko = 'GREK'
WHERE nazwisko LIKE 'TUREK';
-- brak uprawnień do modyfikacji

-- 6.i

GRANT UPDATE 
ON stud 
TO 'userX'@'localhost';

-- 6.j

UPDATE studenci
SET nazwisko = 'GREK'
WHERE nazwisko LIKE 'TUREK';
-- brak uprawnień do modyfikacji tabeli studenci

-- 6.k

UPDATE stud
SET nazwisko = 'GREK'
WHERE nazwisko LIKE 'TUREK';
-- View is not updatable
-- Nie można modyfikować widoku
-- agregacja danych

CREATE VIEW stud2 AS
SELECT nr_stud, nazwisko
FROM studenci;

UPDATE stud2
SET nazwisko = 'GREK'
WHERE nazwisko LIKE 'TUREK';
-- nazwisko zmieniło się pomyślnie ponieważ nie ma agregacji danych

GRANT SELECT, UPDATE
ON stud2
TO 'userX'@'localhost';

-- 6.l

UPDATE stud2
SET nazwisko = 'BELG'
WHERE nazwisko LIKE 'GREK';
-- wykonało się pomyślnie ponieważ użytkownik ma dostęp

-- 6.m
CREATE USER pomX@localhost;

GRANT SELECT
ON przedmioty
TO 'pomX'@'localhost';

-- 6.n
SELECT * FROM przedmioty;

GRANT SELECT
ON przedmioty
TO 'userX'@'localhost'
-- brak uprawnień do aby nadać uprawnienia innemu użytkownikowi,
-- wymagane zezwolenie roota

-- 6.o

GRANT SELECT
ON przedmioty
TO 'pomX'@'localhost' 
WITH GRANT OPTION;

-- 6.p

GRANT SELECT
ON przedmioty
TO 'userX'@'localhost'
-- uprawnienia zastosowały się pomyślnie

-- 6.q

SELECT * FROM przedmioty
-- wyświetla się prawidłowo ponieważ nastąpiła delegacja uprawnień

