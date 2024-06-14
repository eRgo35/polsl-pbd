# Laboratorium z Podstaw Baz Danych

## Temat: Triggery

### Data wykonania ćwiczenia: 20.06.2024

### Przygotowali: Michał Czyż, Dawid Głąb

### Grupa 6, Sekcja 13

## 3.1. Poleceniem create table utworzyć tabelę o następującej strukturze
  
```sql
Osoby (imie varchar(15), nazwisko varchar(15), PESEL varchar(11), data_ur timestamp)
```

```sql
create table Osoby (imie varchar(15), nazwisko varchar(15), PESEL varchar(11), data_ur timestamp);
```

Polecenie wykonało się pomyślnie.

## 3.2. Utworzyć dodatkową tabelę Pracownicy, wykorzystując polecenie
  
```sql
create table Pracownicy (nr_prac integer, nr_zesp integer, pensja real) INHERITS (Osoby)
```

Polecenie wykonało się pomyślnie.

## 3.3. Wpisać 3 rekordy do tabeli Osoby

```txt
Jan     Nowak       11111111111    1988-01-01
Adam    Kowalski    22222222222    1989-10-01
Anna    Krol        33333333333    1990-10-15
```

```sql
insert into Osoby values ('Jan', 'Nowak', '11111111111', '1988-01-01'), ('Adam', 'Kowalski', '22222222222', '1989-10-01'), ('Anna', 'Krol', '33333333333', '1990-10-15');
```

Polecenie wykonało się prawidłowo.

## 3.4. Wpisać 2 rekordy do tabeli Pracownicy

```txt
Tomasz    Wicek     44444444444    1978-12-12    1    10    2500
Maria     Bialek    55555555555    1980-12-12    2    10    2000
```

```sql
insert into Pracownicy values ('Tomasz', 'Wicek', '44444444444', '1978-12-12', 1, 10, 2500), ('Maria', 'Bialek', '55555555555', '1980-12-12', 2, 10, 2000);
```

Polecenie wykonało się pomyślnie.

## 3.5. Wyświetlić (poleceniem select) dane o tabelach Osoby i Pracownicy wpisane do tabeli systemowej (zwanej też często perspektywą systemową) pg_tables, dodając frazę

```sql
... where tablename = 'osoby' or tablename = 'pracownicy'
```

```sql
select * from pg_tables where tablename = 'osoby' or tablename = 'pracownicy';
```

Wykonane polecenie zwraca informacje o tabelach, kto jest ich właścicielem oraz czy istnieją jakieś reguły, indeksy, wyzwalacze, albo zabezpeiczenia.

Uwaga! pg_tables to tabela systemowa, która już istnieje w systemie Postgres i którą system Postgres
utrzymuje, więc nie ma potrzeby jej osobnego tworzenia. Można przeglądać jej zawartość poleceniem
SELECT.

## 3.6. Wyświetlić nazwy i typy atrybutów tabeli Osoby

```sql
select pa.attname, pt.typname
from pg_class pc, pg_attribute pa, pg_type pt
where pc.relname='osoby' and pc.oid =pa.attrelid and pt.oid = pa.atttypid;
```

|    | attname | typname |
| -- | ------- | ------- |
|  1 | tableoid | oid |
|  2 | cmax | cid |
|  3 | xmax | xid |
|  4 | cmin | cid |
|  5 | xmin | xid |
|  6 | ctid | tid |
|  7 | imie | varchar |
|  8 | nazwisko | varchar |
|  9 | pesel | varchar |
| 10 | data_ur | timestamp |

## 3.7. Wyświetlić wartości niejawnej kolumny tableoid tabeli Pracownicy

```sql
select tableoid from Pracownicy;
```

|   | tableoid |
| - | -------- |
| 1 | 16388    |
| 2 | 16388    |

## 3.8. Wyświetlić wartości niejawnej kolumny tableoid tabeli Osoby. Co daje się zauważyć?

```sql
select tableoid from Osoby;
```

|   | tableoid |
| - | -------- |
| 1 | 16385    |
| 2 | 16385    |
| 3 | 16385    |
| 4 | 16388    |
| 5 | 16388    |

Można zauważyć, iż każda tabela ma swój unikalny identyfikator przez co, gdy tworzone są nowe rekordy w tabeli, a są one dziedziczone po innej tabeli, można je rozróżnić od siebie. Tutaj można zauważyć, że 3 dodane wiersze do tabeli osoby i 2 dodane wiersze z tabeli pracownicy są rozróżnianie.

## 3.9. Potwierdzić swoje wcześniejsze obserwacje wyświetlając wszystkie dane wpisane do tabeli Osoby

```sql
select tableoid, * from Osoby;
```

Wcześniejsza teza, zgadza się z rzeczywistością.

| tableoid | imie | nazwisko | pesel | data_ur |
| -------- | ---- | ------- | ----- | ------- |
| 16385    | Jan  | Nowak    | 11111111111 | 1988-01-01 00:00:00 |
| 16385    | Adam  | Kowalski | 22222222222 | 1989-10-01 00:00:00 |
| 16385    | Anna  | Krol     | 33333333333 | 1990-10-15 00:00:00 |
| 16388    | Tomasz | Wicek    | 44444444444 | 1978-12-12 00:00:00 |
| 16388    | Maria | Bialek  | 55555555555 | 1980-12-12 00:00:00 |

## 3.10. Do poprzednio zadanego zapytania dodać frazę only

```sql
select tableoid, * from only Osoby;
```

Wykorzystując frazę `only` możemy wyświetlić tylko elementy przynależące do tabeli, nie będące pochodnymi.

| tableoid | imie | nazwisko | pesel | data_ur |
| -------- | ---- | ------- | ----- | ------- |
| 16385    | Jan  | Nowak    | 11111111111 | 1988-01-01 00:00:00 |
| 16385    | Adam  | Kowalski | 22222222222 | 1989-10-01 00:00:00 |
| 16385    | Anna  | Krol     | 33333333333 | 1990-10-15 00:00:00 |

## 3.11. Spróbować usunąć rekord dotyczący Marii Bialek z tabeli Pracownicy

```sql
delete from Pracownicy where imie = 'Maria';
```

Usuwanie wykonało się pomyślnie.

## 3.12. Sprawdzić czy rekord został usunięty zarówno z tabeli Pracownicy, jak i z tabeli Osoby

Tak, rekord został usunięty z obu tabel.

```sql
select COUNT(*) from Osoby; -- zwraca 4
select COUNT(*) from Pracownicy; -- zwraca 1
```

## 3.13. Wpisać 2 rekordy do tabeli Pracownicy

```txt
Witold    Wrembel    88888888888    02-02-1977    2    10    1950
Kamila    Bialek     99999999999    12-12-1983    3    20    2000
```

```sql
insert into Pracownicy values ('Witold', 'Wrembel', '88888888888', '02-02-1977', 2, 10, 1950), ('Kamila', 'Bialek', '99999999999', '12-12-1983', 3, 20, 2000);
```

Polecenie wykonało się pomyślnie.

## 3.14. Ponownie wykonać polecenie 3.7. Czy daje się zauważyć jakąś zmianę?

Tak, znajdują się tam teraz 3 rekordy o tym samym tableoid jak wcześniej. Byłyby 4, lecz jeden usunęliśmy w poprzednich zadaniach.

|   | tableoid |
| - | -------- |
| 1 | 16388    |
| 2 | 16388    |
| 3 | 16388    |

## 3.15. Stworzyć nową tabelę, w której będą pamiętane informacje o premiach poszczególnych pracowników, przy czym atrybut premia_kwartalna będzie reprezentowany jako czteroelementowa tablica, a kolejne elementy tej tablicy będą liczbami całkowitymi; wskaźnikiem będzie numer kwartału

```sql
create table premie (nr_prac integer, premia_kwartalna integer[]);
```

Tabela została utworzona pomyślnie.

## 3.16. Wpisać następujące dane do nowoutworzonej tabeli

```sql
insert into premie values (1, '{100,150,200,250}');
```

Wstawianie przebiegło pomyślnie.

## 3.17. Wyświetlić wpisane do tablicy dane, wykonując zapytania typu

```sql
select * from premie;
select premia_kwartalna[1] from premie;
```

| nr_prac | premia_kwartalna |
| ------- | ---------------- |
| 1       | {100,150,200,250} |

| premia_kwartalna |
| ---------------- |
| 100              |

Możemy zwracać pozycje tak samo jak otrzymywanie danych z tablicy w językach programowania.

## 3.18. Stworzyć tabelę zawierającą informacje o książkach pożyczanych przez pracowników w zakładowej bibliotece – ich autorach, tytułach, wydawnictwie i roku wydania

```sql
CREATE TABLE wypozyczenia (nr_prac integer, autor_tytul text[][]);
```

Tabela utworzyła się pomyślnie.

## 3.19. Do utworzonej tabeli wpisać 2 rekordy (dotyczące pracowników o numerach 1, i 2)

```sql
INSERT INTO wypozyczenia VALUES (1, '{{"Tolkien", "Hobbit", "Iskry", 1980}, {"Dickens", "Klub Pickwicka", "MG", 1989}, {"Stone", "Pasja zycia", "ZYSK I S-KA", 1999}}');

INSERT INTO wypozyczenia VALUES (2, '{{"Pascal", "Przewodnik", "lonely planet", 2010}, {"Archer", "Co do grosza", "REBIS Sp. z o.o.", 1999}}');
```

Wstawianie zakończyło się pomyślnie.

## 3.20. Wyświetlić wartości wpisane w tablicach; zaobserwować różnice i podobieństwa w otrzymywanych wynikach

```sql
SELECT * FROM wypozyczenia; -- zwraca dwa rekordy, w czym autor_tytul to tablica za wszystkimi danymi 
SELECT nr_prac, autor_tytul[1][1] FROM wypozyczenia; -- zwraca pierwszą pozycję tablicy 2d, Tolkien i Pascal
SELECT nr_prac, autor_tytul[1:3][1] FROM wypozyczenia; -- zwraca array samych autorów dla każdego rekordu
SELECT nr_prac, autor_tytul[1:3][1:3] FROM wypozyczenia; -- zwraca wyniki jako 2d array w postaci, Autor, Tytuł, Wydawnictwo
SELECT nr_prac, autor_tytul[1:3][2] FROM wypozyczenia; -- podobnie jak ostatnio, lecz bez Wydawnictwa
SELECT nr_prac, autor_tytul[2][2] FROM wypozyczenia; -- zwraca tylko tytuł, drugiej pozycji w liście.
SELECT nr_prac, autor_tytul[2][1] FROM wypozyczenia; -- zwraca tylko nazwisko autora drugiej pozycji w liście.
```

## 3.21. Napisać funkcję w języku SQL, wyświetlającą informacje o nazwisku pracownika, którego numer podany jest parametrem. Ogólna postać funkcji jest następująca

```sql
CREATE FUNCTION nazwafunkcji (typparametru1, typparametru2,...) RETURNS typwynikowy
AS 'ciałofunkcji'
LANGUAGE 'sql';

CREATE FUNCTION dane (integer) RETURNS text
AS 'select nazwisko from Pracownicy where nr_prac = $1'
LANGUAGE 'sql';
```

Funkcja utworzona pomyślnie.

## 3.22. Przetestować działanie funkcji wpisując polecenie

```sql
select dane(1) as nazwisko;
```

Funkcja zwraca dane:

| nazwisko |
| -------- |
| Wicek    |

## 3.23. Napisać funkcję wyświetlającą wszystkie dane osobowe pracownika (imię, nazwisko, PESEL), którego numer podany jest parametrem. W tym celu zdefiniować najpierw typ, a dopiero w drugiej kolejności stosowną procedurę

```sql
CREATE TYPE complex AS (i text, n text, p text);

CREATE FUNCTION dane2 (integer) RETURNS complex
AS 'select imie, nazwisko, PESEL from Pracownicy where nr_prac = $1'
LANGUAGE 'sql';

select dane2(2);
```

Funkcja działa prawidłowo, zwraca typ złożony:

| dane2 |
| ----- |
| (Witold,Wrembel,88888888888) |

## 3.24. Napisać funkcję wyświetlającą wszystkie dane osobowe (imię, nazwisko, PESEL) wszystkich pracowników

```sql
CREATE FUNCTION dane3 () RETURNS setof complex
AS 'select imie, nazwisko, PESEL from Pracownicy'
LANGUAGE 'sql';

select dane3();
```

Dane z funkcji są zwracane dla wszystkich pracowników w tabeli.

| dane3 |
| ----- |
| (Tomasz,Wicek,44444444444) |
| (Witold,Wrembel,88888888888) |
| (Kamila,Bialek,99999999999) |

## 3.25. Napisać funkcję wyświetlającą (tylko) tytuły książek pożyczonych przez pracownika o podanym parametrem funkcji numerze. Podjąć próbę takiego wskazania „współrzędnych" atrybutu tablicowego, aby w wyniku wykonania polecenia SELECT faktycznie pojawiły się tylko tytuły książek (a nie np. autorzy-tytuł)

<!--- ########################################### TODO ########################################### -->
TODO
---

## 3.26. Napisać funkcję w proceduralnym języku Postgresa – plpgsql, zwracającą wartość pensji pracowników podwyższoną o 25% i przetestować jej działanie np. poleceniem select extra_money(1)

```sql
CREATE OR REPLACE FUNCTION extra_money (integer) RETURNS real AS
$$
  DECLARE zm real;
  BEGIN
  SELECT 1.25 * pensja INTO zm FROM pracownicy WHERE nr_prac = $1;
  RETURN zm;
  END;
$$
LANGUAGE 'plpgsql';
```

```sql
select extra_money(1)
```

Polecenie select zwraca wartość `3125`. Oryginalna wartość znajdująca się w tablicy w pierwszym rekordzie to `2500`. Zatem funkcja wykonała się prawidłowo.

## 3.27. Napisać regułę uniemożliwiającą zmianę wartości atrybutu pensja dla aktualizowanego pracownika

```sql
-- Reguła ma ogólną postać:
CREATE RULE nazwareguły AS ON zdarzenie TO tabela [WHERE warunek]
DO [ALSO|INSTEAD] [akcja | (akcje) | NOTHING];

-- Regułę usuwa się poleceniem:
DROP RULE nazwareguły ON tabela;

CREATE RULE regula1
AS ON UPDATE TO Pracownicy
WHERE NEW.pensja <> OLD.pensja
DO INSTEAD NOTHING;
```

Reguła została utworzona pomyślnie.

## 3.28. Sprawdzić poprawność działania reguły, a następnie ją usunąć

```sql
SELECT * FROM pracownicy;
UPDATE pracownicy SET nr_zesp = 30 WHERE nr_zesp = 20;
SELECT * FROM pracownicy;
UPDATE pracownicy SET pensja = 2000 WHERE imie = 'Witold';
SELECT * FROM pracownicy;
```

Zmiana numeru zespołu przebiegła bez problemu. Natomiast próba zmiany pensji pracownika o imieniu 'Witold' choć zakończyła się sukcesem, gdy wyświelimy zawartość tabeli Pracownicy to zauważamy, że żadna aktualizacja nie nastąpiła.

Reguła została pomyślnie usunięta poleceniem:

```sql
DROP RULE regula1 ON Pracownicy
```

## 3.29. Napisać regułę, która nie dopuści na dopisanie nowego pracownika o numerze mniejszym bądź równym zeru i przetestować jej działanie, wydając stosowne komendy

<!--- ########################################### TODO ########################################### -->
TODO
---

## 3.30. Za pomocą reguł utworzyć modyfikowalne widoki (perspektywy), które normalnie nie są obsługiwane przez PostgreSQL. W tym celu utworzyć perspektywę tabeli Osoby

```sql
CREATE VIEW osob_view AS SELECT imie, nazwisko, PESEL FROM osoby WHERE imie='Witold';

CREATE RULE reg2 AS ON INSERT TO osob_view DO INSTEAD INSERT INTO osoby (imie, nazwisko, PESEL) VALUES (NEW.imie,NEW.nazwisko, NEW.PESEL);
```

Perspektywa oraz reguła utworzyły się pomyślnie.

## 3.31. Przetestować działanie reguły reg2

```sql
select * from osob_view;
```

Wyświetla się widok osób z jednym rekordem 'Witold'.

```sql
insert into osob_view values ('Witold', 'Kowalski', '12345678901');
```

Wstawianie przykładowego nowego rekordu do widoku przebiega pomyślnie.

I po ponownym wyświetleniu widoku `osob_view` otrzymujemy dwa wyniki:

| imie | nazwisko | pesel |
| ---- | -------- | ----- |
| Witold | Kowalski | 12345678901 |
| Witold | Wrembel | 88888888888 |

## 3.32. W celu pamiętania czasu modyfikacji danych w tabeli Premie, dodać do niej 1 kolumnę

```sql
ALTER TABLE Premie ADD COLUMN last_updated timestamptz;
```

Polecenie wykonało się pomyślnie.

## 3.33. Utworzyć wyzwalacz, który dla każdego następnego wstawienia nowego wiersza (lub modyfikacji istniejącego) w tabeli Premie spowoduje umieszczenie aktualnego znacznika czasu w polu last_updated bieżącego rekordu tabeli

```sql
-- Uwaga. Trigger definiuje się następującą składnią:
CREATE TRIGGER nazwa
BEFORE | AFTER /* czy trigger ma być wykonany przed czy po zdarzeniu */

INSERT | UPDATE | DELETE /* których zdarzeń trigger dotyczy, można łączyć kilka przez OR */
ON tabela
FOR EACH
ROW | STATEMENT /* czy trigger ma być wywołany raz na rekord, czy raz na instrukcję */
EXECUTE PROCEDURE procedura (parametry); /* co ma być wywołane jako obsługa triggera ale wcześniej trzeba zdefiniować niezbędną funkcję. */

-- Trigger usuwa poleceniem:
DROP TRIGGER nazwa ON tabela;
```

Funkcja ma postać:

```sql
CREATE OR REPLACE FUNCTION upd() RETURNS trigger AS
$$
  BEGIN
  NEW.last_updated = now();
  RETURN NEW;
  END;
$$
LANGUAGE plpgsql;
```

Natomiast trigger rozwiązujący to zadanie ma następującą definicję:

```sql
CREATE TRIGGER last_upd
BEFORE insert OR update ON Premie
FOR EACH ROW
EXECUTE PROCEDURE upd();
```

Trigger został utworzony pomyślnie.

## 3.34. Przetestować działanie napisanego wyzwalacza

```sql
SELECT * FROM Premie; -- pole last_updated jest równe [null]
INSERT INTO Premie VALUES (2, '{300,150,100,150}');
SELECT * FROM Premie;
```

Po aktualizacji, pole last_updated także się zaktualizowało o aktualny timestamp wykonania dla nowego rekordu.

| nr_prac | premia_kwartalna | last_updated |
| ------- | ---------------- | ------------ |
| 2 | {300,150,100,150} | 2024-06-20 7:51:19.333221-05 |

## 3.35. Utworzyć tabelę TOWARY(id,nazwa,cena_netto) i wpisać następujące dane

```txt
1    kabel     50
2    laptop    940
3    monitor   600
```

<!--- ########################################### TODO ########################################### -->
TODO
---

## 3.36. Napisać funkcję podatek_vat() oraz wyświetlić towary – tzn. (id, nazwa, cena_netto, podatek_vat(cena_netto), cena_netto + podatek_vat(cena) as cena_brutto)

<!--- ########################################### TODO ########################################### -->
TODO
---

## 3.37. Założyć tabelę TOWARY2(id,nazwa,cena,cena_vat,cena_brutto). Napisać wyzwalacz, który przy wprowadzaniu oraz uaktualnianiu krotek (id,nazwa,cena_netto), obliczy odpowiednio cenę_vat oraz cenę_brutto

<!--- ########################################### TODO ########################################### -->
TODO
---

## 3.38. Przetestować wyzwalacz z punktu 3.41 – czyli wydać polecenie aktywujące trigger i sprawdzić czy działa zgodnie z poleceniem

<!--- ########################################### TODO ########################################### -->
TODO
---

## 3.39. Po zatwierdzeniu wykonania zadań przez prowadzącego, usunąć bazę, utworzoną na początku laboratorium

<!--- ########################################### TODO ########################################### -->
TODO
---
