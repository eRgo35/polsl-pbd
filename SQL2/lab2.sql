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
SELECT pr.NR_PRAC, NAZWISKO, COUNT(DISTINCT r.NR_PRZEDM)
FROM pracownicy pr
INNER JOIN przedmioty p ON p.NR_ODP_PRAC = pr.NR_PRAC
INNER JOIN rozklady r ON r.NR_PRAC = pr.NR_PRAC
GROUP BY pr.NR_PRAC, NAZWISKO;

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

SELECT p.NR_PRZEDM, p.NAZWA_PRZEDM
FROM przedmioty p
INNER JOIN rozklady r ON p.NR_PRZEDM = r.NR_PRZEDM
INNER JOIN pracownicy pr ON pr.NR_PRAC = r.NR_PRAC
GROUP BY p.NAZWA_PRZEDM, p.NR_PRZEDM
ORDER BY COUNT(pr.NR_PRAC) DESC
LIMIT 1;

-- 4. Podaj numery i nazwiska pracowników, którzy w temacie DYSK otrzymali wypłatę mniejszą
-- od co najmniej jednej z wypłat pracownika o nazwisku GRZYBEK w dowolnym temacie.u

SELECT DISTINCT p1.nr_prac, p1.NAZWISKO
FROM pracownicy p1
INNER JOIN wyplaty w1 ON p1.NR_PRAC = w1.NR_PRAC
INNER JOIN tematy t ON w1.NR_TEM = t.NR_TEM
INNER JOIN pracownicy p2 ON p2.NAZWISKO LIKE 'GRZYBEK'
INNER JOIN wyplaty w2 ON p2.NR_PRAC = w2.NR_PRAC
WHERE TEMAT LIKE 'DYSK';

-- 5. Podaj numery, nazwiska i daty urodzenia mężczyzn zatrudnionych w zespole OPROGRAMOWANIE,
-- którzy są młodsi od każdego pracownika zespołu BUDOWA

SELECT nr_prac, nazwisko, data_ur
FROM pracownicy p
INNER JOIN zespoly z ON z.NR_ZESP = p.NR_ZESP
WHERE PLEC LIKE 'M'
AND nazwa_zesp LIKE 'OPROGRAMOWANIE'
AND data_ur > (
	SELECT MAX(data_ur)
	FROM pracownicy p
	INNER JOIN zespoly z ON p.nr_zesp = z.nr_zesp
	WHERE nazwa_zesp LIKE 'BUDOWA'
);
------------------------------------------------------------------------
-- 6. Podaj nazwiska najstarszych studentów na poszczególnych kierunkach.

SELECT k.nr_kier, nazwa_kier, nr_stud, nazwisko
FROM kierunki k
INNER JOIN studenci s ON k.nr_kier = s.nr_kier
INNER JOIN (
    SELECT nr_kier, MIN(data_ur) AS min_data_ur
    FROM studenci
    GROUP BY nr_kier
) d ON s.nr_kier = d.nr_kier AND s.data_ur = d.min_data_ur;

-- 7. Podaj nazwy zespołów, w których nie pracuje żadna kobieta. 
SELECT z.NAZWA_ZESP
FROM zespoly z
LEFT JOIN pracownicy p 
ON z.NR_ZESP = p.NR_ZESP AND p.PLEC = 'K'
WHERE p.NR_ZESP IS NULL;

-- 8.
SELECT z.nr_zesp, nazwa_zesp, COUNT(nr_prac) AS ile
FROM zespoly z
LEFT JOIN pracownicy p ON z.nr_zesp = p.nr_zesp
GROUP BY z.nr_zesp, nazwa_zesp
ORDER BY z.nr_zesp;

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

-- Nie jest możliwa modyfikacja perspektywy ponieważ w zapytaniu tworzącym perspektywę
-- występuje złączenie wielu tabel oraz występuje słowo kluczone DISTINCT (czyli agregat kwerendy)

-- 10.

CREATE VIEW perspektywa2 AS
SELECT z.NR_ZESP AS nrz, z.NAZWA_ZESP, p.NR_PRAC, p.NAZWISKO, SUM(w.KWOTA) AS suma_zarobkow_prac
FROM zespoly z
INNER JOIN pracownicy p ON z.NR_ZESP = p.NR_ZESP
LEFT JOIN wyplaty w ON w.NR_PRAC = p.NR_PRAC
GROUP BY z.NR_ZESP, z.NAZWA_ZESP, p.NR_PRAC, p.NAZWISKO;

SELECT * FROM perspektywa2;

SELECT nazwa_zesp, 
AVG(suma_zarobkow_prac) AS 'sredni',
MIN(suma_zarobkow_prac) AS 'min',
MAX(suma_zarobkow_prac) AS 'max'
FROM perspektywa2
GROUP BY nazwa_zesp

DROP VIEW perspektywa2;

-- 11.

SELECT DISTINCT nazwisko
FROM pracownicy p
INNER JOIN przydzialy r ON r.nr_prac = p.NR_PRAC
LEFT JOIN wyplaty w ON p.NR_PRAC = w.NR_PRAC AND r.NR_TEM = w.NR_TEM
WHERE kwota IS NULL

-- 12.

SELECT nr_prac, NAZWISKO, nr_tem, temat
FROM tematy t
INNER JOIN pracownicy p ON p.nr_prac = t.nr_prac_kt
WHERE NOT EXISTS (
	SELECT *
	FROM przydzialy r
	WHERE t.nr_prac_kt = r.nr_prac AND t.nr_tem = r.nr_tem AND
	r.kod_funkcji LIKE 'KRW'
);

SELECT p.nr_prac, nazwisko, t.nr_tem, temat
FROM pracownicy p
INNER JOIN przydzialy r ON p.nr_prac = r.nr_prac
INNER JOIN tematy t ON r.NR_TEM = t.NR_TEM
WHERE kod_funkcji LIKE 'KRW' AND p.nr_prac NOT LIKE NR_PRAC_KT;

-- 13.

SELECT p.NR_PRAC, p.NAZWISKO
FROM pracownicy p
LEFT JOIN (
    SELECT DISTINCT r.NR_PRAC
    FROM przydzialy r
    INNER JOIN (
        SELECT DISTINCT r.NR_TEM
        FROM przydzialy r
        INNER JOIN pracownicy p ON p.NR_PRAC = r.NR_PRAC
        WHERE p.NAZWISKO LIKE 'NIEZALEZNY'
    ) sub ON r.NR_TEM = sub.NR_TEM
) sub2 ON p.NR_PRAC = sub2.NR_PRAC
WHERE sub2.NR_PRAC IS NULL;

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