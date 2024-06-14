# Laboratorium z Podstaw Baz Danych

## Temat: Triggery

### Data wykonania ćwiczenia: 20.06.2024

### Przygotowali: Michał Czyż, Dawid Głąb

### Grupa 6, Sekcja 13

## 3.1. Poleceniem create table utworzyć tabelę o następującej strukturze
  
```sql
Osoby (imie varchar(15), nazwisko varchar(15), PESEL varchar(11), data_ur timestamp)
```

## 3.2. Utworzyć dodatkową tabelę Pracownicy, wykorzystując polecenie
  
```sql
create table Pracownicy (nr_prac integer, nr_zesp integer, pensja real) INHERITS (Osoby)
```

## 3.3. Wpisać 3 rekordy do tabeli Osoby

```txt
Jan     Nowak       11111111111    1988-01-01
Adam    Kowalski    22222222222    1989-10-01
Anna    Krol        33333333333    1990-10-15
```

## 3.4. Wpisać 2 rekordy do tabeli Pracownicy

```txt
Tomasz    Wicek     44444444444    1978-12-12    1    10    2500
Maria     Bialek    55555555555    1980-12-12    2    10    2000
```

## 3.5. Wyświetlić (poleceniem select) dane o tabelach Osoby i Pracownicy wpisane do tabeli systemowej (zwanej też często perspektywą systemową) pg_tables, dodając frazę

```sql
... where tablename = 'osoby' or tablename = 'pracownicy'
```

Uwaga! pg_tables to tabela systemowa, która już istnieje w systemie Postgres i którą system Postgres
utrzymuje, więc nie ma potrzeby jej osobnego tworzenia. Można przeglądać jej zawartość poleceniem
SELECT.

## 3.6. Wyświetlić nazwy i typy atrybutów tabeli Osoby

```sql
select pa.attname, pt.typname
from pg_class pc, pg_attribute pa, pg_type pt
where pc.relname='osoby' and pc.oid =pa.attrelid and pt.oid = pa.atttypid;
```

## 3.7. Wyświetlić wartości niejawnej kolumny tableoid tabeli Pracownicy

## 3.8. Wyświetlić wartości niejawnej kolumny tableoid tabeli Osoby. Co daje się zauważyć?

## 3.9. Potwierdzić swoje wcześniejsze obserwacje wyświetlając wszystkie dane wpisane do tabeli Osoby

```sql
select tableoid, * from Osoby;
```

## 3.10. Do poprzednio zadanego zapytania dodać frazę only

```sql
select tableoid, * from only Osoby;
```

## 3.11. Spróbować usunąć rekord dotyczący Marii Bialek z tabeli Pracownicy

```sql
delete from Pracownicy where imie = 'Maria';
```

## 3.12. Sprawdzić czy rekord został usunięty zarówno z tabeli Pracownicy, jak i z tabeli Osoby

## 3.13. Wpisać 2 rekordy do tabeli Pracownicy

```txt
Witold    Wrembel    88888888888    02-02-1977    2    10    1950
Kamila    Bialek     99999999999    12-12-1983    3    20    2000
```

## 3.14. Ponownie wykonać polecenie 3.7. Czy daje się zauważyć jakąś zmianę?

## 3.15. Stworzyć nową tabelę, w której będą pamiętane informacje o premiach poszczególnych pracowników, przy czym atrybut premia_kwartalna będzie reprezentowany jako czteroelementowa tablica, a kolejne elementy tej tablicy będą liczbami całkowitymi; wskaźnikiem będzie numer kwartału

```sql
create table premie (nr_prac integer, premia_kwartalna integer[]);
```

## 3.16. Wpisać następujące dane do nowoutworzonej tabeli

```sql
insert into premie values (1, '{100,150,200,250}');
```

## 3.17. Wyświetlić wpisane do tablicy dane, wykonując zapytania typu

```sql
select * from premie;
select premia_kwartalna[1] from premie;
```

## 3.18. Stworzyć tabelę zawierającą informacje o książkach pożyczanych przez pracowników w zakładowej bibliotece – ich autorach, tytułach, wydawnictwie i roku wydania

```sql
CREATE TABLE wypozyczenia (nr_prac integer, autor_tytul text[][]);
```

## 3.19. Do utworzonej tabeli wpisać 2 rekordy (dotyczące pracowników o numerach 1, i 2)

```sql
INSERT INTO wypozyczenia VALUES
(1, '{{"Tolkien", "Hobbit", "Iskry", 1980}, {"Dickens", "Klub Pickwicka", "MG", 1989}, {"Stone",
"Pasja zycia", "ZYSK I S-KA", 1999}}');

INSERT INTO wypozyczenia VALUES (2, '{{"Pascal", "Przewodnik", "lonely planet", 2010},
{"Archer", "Co do grosza", "REBIS Sp. z o.o.", 1999}}');
```

## 3.20. Wyświetlić wartości wpisane w tablicach; zaobserwować różnice i podobieństwa w otrzymywanych wynikach

```sql
SELECT * FROM wypożyczenia;
SELECT nr_prac, autor_tytul[1][1] FROM wypozyczenia;
SELECT nr_prac, autor_tytul[1:3][1] FROM wypozyczenia;
SELECT nr_prac, autor_tytul[1:3][1:3] FROM wypozyczenia;
SELECT nr_prac, autor_tytul[1:3][2] FROM wypozyczenia;
SELECT nr_prac, autor_tytul[2][2] FROM wypozyczenia;
SELECT nr_prac, autor_tytul[2][1] FROM wypozyczenia;
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

## 3.22. Przetestować działanie funkcji wpisując polecenie

```sql
select dane(1) as nazwisko;
```

## 3.23. Napisać funkcję wyświetlającą wszystkie dane osobowe pracownika (imię, nazwisko, PESEL), którego numer podany jest parametrem. W tym celu zdefiniować najpierw typ, a dopiero w drugiej kolejności stosowną procedurę

```sql
CREATE TYPE complex AS (i text, n text, p text);

CREATE FUNCTION dane2 (integer) RETURNS complex
AS 'select imie, nazwisko, PESEL from Pracownicy where nr_prac = $1'
LANGUAGE 'sql';

select dane2(2);
```

## 3.24. Napisać funkcję wyświetlającą wszystkie dane osobowe (imię, nazwisko, PESEL) wszystkich pracowników

```sql
CREATE FUNCTION dane3 () RETURNS setof complex
AS 'select imie, nazwisko, PESEL from Pracownicy'
LANGUAGE 'sql';

select dane3();
```

## 3.25. Napisać funkcję wyświetlającą (tylko) tytuły książek pożyczonych przez pracownika o podanym parametrem funkcji numerze. Podjąć próbę takiego wskazania „współrzędnych" atrybutu tablicowego, aby w wyniku wykonania polecenia SELECT faktycznie pojawiły się tylko tytuły książek (a nie np. autorzy-tytuł)

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

## 3.28. Sprawdzić poprawność działania reguły, a następnie ją usunąć

```sql
SELECT * FROM pracownicy;
UPDATE pracownicy SET nr_zesp = 30 WHERE nr_zesp = 20;
SELECT * FROM pracownicy;
UPDATE pracownicy SET pensja = 2000 WHERE imie = 'Witold';
SELECT * FROM pracownicy;
```

## 3.29. Napisać regułę, która nie dopuści na dopisanie nowego pracownika o numerze mniejszym bądź równym zeru i przetestować jej działanie, wydając stosowne komendy

## 3.30. Za pomocą reguł utworzyć modyfikowalne widoki (perspektywy), które normalnie nie są obsługiwane przez PostgreSQL. W tym celu utworzyć perspektywę tabeli Osoby

```sql
CREATE VIEW osob_view AS SELECT imie, nazwisko, PESEL FROM osoby WHERE
imie='Witold';

CREATE RULE reg2 AS ON INSERT TO osob_view DO INSTEAD INSERT INTO osoby
(imie, nazwisko, PESEL) VALUES (NEW.imie,NEW.nazwisko, NEW.PESEL);
```

## 3.31. Przetestować działanie reguły reg2

## 3.32. W celu pamiętania czasu modyfikacji danych w tabeli Premie, dodać do niej 1 kolumnę

```sql
ALTER TABLE Premie ADD COLUMN last_updated timestamptz;
```

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

Do zadania 3.38 funkcja ma postać:

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

## 3.34. Przetestować działanie napisanego wyzwalacza

```sql
SELECT * FROM Premie;
INSERT INTO Premie VALUES (2, '{300,150,100,150}');
SELECT * FROM Premie;
```

## 3.35. Utworzyć tabelę TOWARY(id,nazwa,cena_netto) i wpisać następujące dane

```txt
1    kabel     50
2    laptop    940
3    monitor   600
```

## 3.36. Napisać funkcję podatek_vat() oraz wyświetlić towary – tzn. (id, nazwa, cena_netto, podatek_vat(cena_netto), cena_netto + podatek_vat(cena) as cena_brutto)

## 3.37. Założyć tabelę TOWARY2(id,nazwa,cena,cena_vat,cena_brutto). Napisać wyzwalacz, który przy wprowadzaniu oraz uaktualnianiu krotek (id,nazwa,cena_netto), obliczy odpowiednio cenę_vat oraz cenę_brutto

## 3.38. Przetestować wyzwalacz z punktu 3.41 – czyli wydać polecenie aktywujące trigger i sprawdzić czy działa zgodnie z poleceniem

## 3.39. Po zatwierdzeniu wykonania zadań przez prowadzącego, usunąć bazę, utworzoną na początku laboratorium
