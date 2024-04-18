--Exercise 1
SELECT nazwisko 
FROM studenci 
WHERE nr_stud = 5;
--Exercise 2
SELECT * 
FROM PRZEDMIOTY 
WHERE NAZWA_PRZEDM LIKE 'A%';
--Exercise 3
SELECT nazwisko 
FROM studenci s 
INNER JOIN kierunki k ON s.nr_kier=k.nr_kier 
WHERE nazwa_kier LIKE 'BUDOWA MC';
--Exercise 4
SELECT nazwisko 
FROM STUDENCI s 
INNER JOIN OCENY o ON s.NR_STUD=o.NR_STUD 
WHERE PLEC LIKE 'K' AND OCENA > 4.5 
ORDER BY NAZWISKO ASC;
--Exercise 5
SELECT NAZWA_PRZEDM 
FROM przedmioty p 
INNER JOIN kierunki k ON p.NR_KIER = k.NR_KIER 
WHERE KOD_TPRZEDM LIKE 'WYK' AND NAZWA_KIER LIKE 'BUDOWA MC';
--Exercise 6
SELECT * 
FROM studenci s 
INNER JOIN kierunki k ON s.nr_kier=k.nr_kier 
WHERE plec LIKE 'M' AND nazwa_kier LIKE 'ELEKTRONIKA MED' OR nazwa_kier LIKE 'INFORMATYKA' 
ORDER BY nazwisko DESC;
--Exercise 7
SELECT DISTINCT nazwa_przedm
FROM przedmioty p 
INNER JOIN oceny o ON p.nr_przedm = o.nr_przedm 
WHERE ocena = 5 
GROUP BY p.nazwa_przedm;
--or
SELECT DISTINCT nazwa_przedm 
FROM przedmioty p 
INNER JOIN oceny o ON p.nr_przedm = o.nr_przedm 
WHERE ocena = 5;
--or
SELECT nazwa_przedm 
FROM przedmioty p 
INNER JOIN oceny o ON p.nr_przedm = o.nr_przedm 
WHERE ocena = 5 
GROUP BY p.nazwa_przedm;
--Exercise 8
SELECT p1.nazwa_przedm AS 'Temat podrzedny', p2.nazwa_przedm AS 'Temat nadrzedny' 
FROM przedmioty p1 
INNER JOIN przedmioty p2 ON p1.nr_przedm = p2.nr_przedm_nadrz;
--Exercise 9
SELECT COUNT(nr_stud) 
FROM studenci s 
INNER JOIN kierunki k ON s.nr_kier = k.nr_kier 
WHERE nazwa_kier LIKE 'INFORMATYKA';
--Exercise 10
SELECT AVG(ocena) 
FROM oceny o 
INNER JOIN studenci s ON o.nr_stud = s.nr_stud 
WHERE nazwisko LIKE 'Kowalski';
--Exercise 11
SELECT AVG(OCENA) 
FROM oceny o 
INNER JOIN studenci s ON o.nr_stud = s.nr_stud 
INNER JOIN kierunki k ON s.nr_kier = k.nr_kier 
WHERE nazwa_kier LIKE 'ELEKTRONIKA MED';
--Exercise 12
SELECT nazwisko, MIN(ocena), MAX(ocena) 
FROM oceny o 
INNER JOIN studenci s ON o.nr_stud = s.nr_stud 
GROUP BY nazwisko;
--Exercise 13
SELECT nazwisko, AVG(ocena) 
FROM oceny o 
INNER JOIN studenci s ON o.nr_stud = s.nr_stud 
GROUP BY nazwisko;
--Exercise 14
SELECT nazwa_kier, MIN(data_ur) 
FROM studenci s 
INNER JOIN kierunki k ON s.nr_kier = k.nr_kier 
GROUP BY nazwa_kier;
--Exercise 15
SELECT s.nazwisko, COUNT(ocena), nazwa_przedm 
FROM oceny o 
INNER JOIN studenci s ON o.nr_stud = s.nr_stud 
INNER JOIN przedmioty p ON p.NR_PRZEDM = o.NR_PRZEDM 
GROUP BY o.nr_przedm, nazwisko, nazwa_przedm;
--Exercise 16
SELECT nazwa_przedm 
FROM przedmioty p 
INNER JOIN oceny o ON p.nr_przedm=o.nr_przedm 
GROUP BY nazwa_przedm 
having COUNT(ocena) > 8;
--Exercise 17
SELECT nazwa_przedm, COUNT(DISTINCT ocena) 
FROM oceny o 
INNER JOIN przedmioty p ON p.nr_przedm = o.nr_przedm 
GROUP BY nazwa_przedm;
--Exercise 18
SELECT s.nazwisko, s.DATA_UR
FROM studenci s 
WHERE data_ur < (
	SELECT data_ur 
	FROM studenci s1 
	WHERE s1.nazwisko LIKE 'Wilk'
);

--Exercise 19
SELECT s.nazwisko 
FROM studenci s 
INNER JOIN oceny o ON s.nr_stud = o.nr_stud
GROUP BY nazwisko
HAVING AVG(ocena) > (
	SELECT AVG(ocena) 
	FROM oceny o1 
	INNER JOIN studenci s1 ON o1.nr_stud = s1.nr_stud 
	WHERE s1.nazwisko LIKE 'Walczak'
);
--Exercise 20
SELECT nazwa_kier 
FROM kierunki k 
INNER JOIN studenci s 
ON k.nr_kier = s.nr_kier 
GROUP BY nazwa_kier
HAVING COUNT(nr_stud) > (
	SELECT COUNT(s1.nr_stud) 
	FROM studenci s1 
	INNER JOIN kierunki k1 ON s1.NR_KIER = k1.NR_KIER
	WHERE k1.nazwa_kier LIKE 'ELEKTRONIKA MED'
);