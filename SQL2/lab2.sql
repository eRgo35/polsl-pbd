-- only PDF file
-- SQL2_Czyż_Michał.PDF
-- Strona tytułowa (pierwsza)
-- Rozwiązania zadań
-- Interlinia 1 w całym dokumencie
-- Akapit - odstępy przed i po 0
-- czcionka 12, 13 lub 14
-- marginesy - min.


-- 1. Dla każdego pracownika odpowiedzialnego za jakiś przedmiot podaj liczbę przedmiotów
-- z których prowadzi zajęcia (tabela ROZKLADY).
SELECT pr.nr_odp_prac, p.nazwisko, COUNT(r.nr_przedm) AS il_przedm
FROM rozklady r
INNER JOIN pracownicy p
ON r.nr_prac = p.nr_prac
INNER JOIN przedmioty pr
ON p.nr_prac = pr.nr_odp_prac
WHERE r.nr_przedm = pr.nr_przedm
GROUP BY pr.nr_odp_prac, p.nazwisko
HAVING COUNT(r.nr_przedm) > 0
--! WIP counting doesn't work as intended (idk why)
SELECT pr.NR_PRAC, pr.nazwisko, COUNT(r.nr_przedm) AS ile_przedm
FROM ROZKLADY r
INNER JOIN PRZEDMIOTY p ON p.NR_PRZEDM = r.NR_PRZEDM
INNER JOIN PRACOWNICY pr ON pr.NR_PRAC = r.NR_PRAC
WHERE r.NR_PRZEDM = p.NR_PRZEDM
GROUP BY NR_PRAC, NAZWISKO
HAVING COUNT(r.NR_PRZEDM) > 0
-- Hard bastard, another version but still broken

-- 2. Podaj nazwiska i średnią ocen z przedmiotu ALGEBRA tych studentów, którzy z tego
-- przedmiotu mają więcej niż jedną ocenę.

SELECT s.NR_STUD, NAZWISKO, AVG(OCENA)
FROM studenci s 
INNER JOIN oceny o ON o.NR_STUD = s.NR_STUD 
INNER JOIN przedmioty p ON p.NR_PRZEDM = o.NR_PRZEDM 
WHERE NAZWA_PRZEDM LIKE 'ALGEBRA'
GROUP BY NAZWISKO, NR_STUD
HAVING COUNT(OCENA) > 1;

-- 3. Podaj przedmioty realizowane przez największą liczbę pracowników.

--! SOMETHING TO CHECK
SELECT nr_przedm, nazwa_przedm 
FROM przedmioty 
GROUP BY nr_przedm, nazwa_przedm 
HAVING COUNT(nr_odp_prac) = (
    SELECT MAX(pracownik_count)
    FROM (
        SELECT COUNT(nr_odp_prac) AS pracownik_count
        FROM przedmioty
        GROUP BY nr_przedm
    ) AS max_pracownik_count
);

--HERE BE DRAGONS!!! 
-- not working for nr_przedm cuz somehow there's 2 of bazy danych
SELECT z.NAZWA_PRZEDM, COUNT(p.NR_PRAC)
FROM rozklady r
INNER JOIN pracownicy p ON p.NR_PRAC = r.NR_PRAC
INNER JOIN przedmioty z ON z.NR_PRZEDM = r.NR_PRZEDM
GROUP BY z.NAZWA_PRZEDM
HAVING COUNT(p.NR_PRAC) = (
	SELECT MAX(MAXIMUM)
	FROM (
		SELECT COUNT(p.NR_PRAC) AS MAXIMUM
		FROM rozklady r
      INNER JOIN pracownicy p ON p.NR_PRAC = r.NR_PRAC
      INNER JOIN przedmioty z ON z.NR_PRZEDM = r.NR_PRZEDM
      GROUP BY z.NAZWA_PRZEDM
	)
	AS podzapytanie
);

-- 4. Podaj numery i nazwiska pracowników, którzy w temacie DYSK otrzymali wypłatę mniejszą
-- od co najmniej jednej z wypłat pracownika o nazwisku GRZYBEK w dowolnym temacie.u

SELECT p.nr_prac, p.NAZWISKO
FROM pracownicy p
INNER JOIN wyplaty w ON p.NR_PRAC = w.NR_PRAC
INNER JOIN tematy t ON w.NR_TEM = t.NR_TEM
WHERE TEMAT LIKE 'DYSK'
GROUP BY p.nr_prac, p.NAZWiSKO
HAVING w.KWOTA < (
	SELECT KWOTA
	FROM pracownicy p
	INNER JOIN wyplaty w ON w.NR_PRAC = p.NR_PRAC
	INNER JOIN TEMATY t ON t.NR_TEM = w.NR_TEM
	WHERE NAZWISKO LIKE 'GRZYBEK'
);

-- 5. Podaj numery, nazwiska i daty urodzenia mężczyzn zatrudnionych w zespole OPROGRAMOWANIE,
-- którzy są młodsi od każdego pracownika zespołu BUDOWA

SELECT nr_prac, nazwisko, data_ur
FROM pracownicy p
INNER JOIN zespoly z ON p.nr_zesp = z.nr_zesp
WHERE plec LIKE 'M' AND z.nazwa_zesp LIKE 'OPROGRAMOWANIE'
GROUP BY nr_prac, nazwisko, data_ur
HAVING data_ur > (
    SELECT MIN(p1.data_ur)
    FROM pracownicy p1
    INNER JOIN zespoly z1 ON p1.nr_zesp = z1.nr_zesp
    WHERE z1.nazwa_zesp LIKE 'BUDOWA'
)
ORDER BY data_ur DESC
LIMIT 1;

-- 6.



-- 7. Podaj nazwy zespołów, w których nie pracuje żadna kobieta. 
SELECT nazwa_zesp
FROM zespoly z
LEFT JOIN pracownicy p
ON z.nr_zesp = p.nr_zesp
GROUP BY nazwa_zesp
HAVING SUM(CASE WHEN p.plec = 'K' THEN 1 ELSE 0 END) = 0;

-- 8.


-- 9. Utworzyć perspektywę zawierającą wyszczególnione informacje, oraz określić, czy można
-- aktualizować dane w tej perspektywie. Jeżeli nie można - podać wszystkie tego przyczyny.
-- Po pomyślnym wykonaniu zadania należy usunąć utworzoną przez siebie perspektywę.
-- Zestawienie pracowników mających wypłaty w poszczególnych tematach w postaci:
-- nr_tem nazwa_tematu nr_prac nazwisko
-- Rekordy nie powinny powtarzać się nawet w sytuacji, gdy pracownik uzyskał wielokrotnie
-- dochód w tym samym temacie.

CREATE VIEW perspektywa AS
SELECT DISTINCT t.nr_tem, t.temat AS nazwa_tematu, p.nr_prac, p.nazwisko
FROM tematy t
INNER JOIN pracownicy p ON t.nr_prac_kt = p.nr_prac
INNER JOIN wyplaty w ON w.NR_TEM = t.NR_TEM;

SELECT * FROM perspektywa;

DROP VIEW perspektywa;

-- todo figure out if can update the view


-- 13.

-- not worky bc should use przydziały somehow
SELECT nr_prac, nazwisko
FROM pracownicy
WHERE nr_prac NOT IN (
    SELECT p.nr_prac
    FROM pracownicy p
    WHERE p.nazwisko LIKE 'NIEZALEZNY' NOT IN(
        SELECT p1.nazwisko
        FROM pracownicy p1
        INNER JOIN tematy t
        ON p1.nr_prac = t.nr_prac_kt
    )
)


--14. Podaj nazwiska pracowników którzy realizowali wszystkie tematy, których
-- kierownikiem jest JASKOLA.
SELECT p.NR_PRAC, p.NAZWISKO, COUNT(*)
FROM przydzialy r
INNER JOIN pracownicy p ON r.NR_PRAC = p.NR_PRAC
INNER JOIN tematy t ON t.NR_TEM = r.NR_TEM
WHERE t.NR_PRAC_KT = (
	SELECT NR_PRAC
	FROM pracownicy
	WHERE NAZWISKO LIKE 'JASKOLA'
)
GROUP BY p.NR_PRAC, p.NAZWISKO
HAVING COUNT(t.NR_TEM) = (
	SELECT COUNT(*)
	FROM (
		SELECT t.NR_TEM
		FROM przydzialy r
		INNER JOIN pracownicy p ON r.NR_PRAC = p.NR_PRAC
		INNER JOIN tematy t ON t.NR_TEM = r.NR_TEM
		WHERE t.NR_PRAC_KT = (
			SELECT NR_PRAC
			FROM pracownicy
			WHERE NAZWISKO LIKE 'JASKOLA'
		)
		GROUP BY t.NR_TEM
	) sub
)