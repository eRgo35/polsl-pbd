# Laboratorium z Podstaw Baz Danych
## Temat: NoSQL

### Data wykonania ćwiczenia: 23.05.2024
### Przygotowali: Michał Czyż, Dawid Głąb
### Grupa 6, Sekcja 13
### Zestaw C

## 1. Podaj numery sal, w których odbywają się zajęcia z przedmiotu o numerze 20 (4).

```aql
for r in rozklady
filter r.NR_PRZEDM == "20"
return r.NR_SALI
```

4 elements

```json
[
  "2",
  "3",
  "4",
  "8"
]
```

## 2. Podać nazwiska studentów zaczynające się od litery M (13).

```aql
FOR s IN studenci
FILTER LIKE(s.NAZWISKO, "M%")
return s.NAZWISKO
```

13 elements

```json
[
  "MARCZAK",
  "MISIURA",
  "MAJAKOWSKI",
  "MAJ",
  "MODELAK",
  "MANIERAK",
  "MICHAL",
  "MOMATIUK",
  "MUZYKANT",
  "MIKUS",
  "MILIONER",
  "MILIONER",
  "MILIONER"
]
```


## 3. Podać nazwy przedmiotów, za które odpowiedzialny jest pracownik JANECZEK (3).

```aql
for p in przedmioty
let janeczek = (
    for pr in pracownicy
    filter pr.NAZWISKO == "JANECZEK"
    return pr._key
)
filter p.NR_ODP_PRAC == janeczek[0]
return p.NAZWA_PRZEDM
```

3 elements

```json
[
  "ARCHITEKTURA KOMPUTEROW",
  "MODELOWANIE CYFROWE",
  "METODY NUMERYCZNE"
]
```

## 4. Podać numery sal, w których zajęcia rozpoczynają się przed godz. 14:00. Wyświetl w porządku od najwcześniej rozpoczynających się zajęć (23).

```aql
FOR r IN rozklady
FILTER r.GODZINA < 14
sort r.GODZINA asc
return r.NR_SALI
```

23 elements

```json
[
  "3",
  "3",
  "4",
  "8",
  "9",
  "16",
  "1",
  "4",
  "4",
  "5",
  "7",
  "8",
  "11",
  "11",
  "16",
  "16",
  "1",
  "1",
  "2",
  "2",
  "6",
  "7",
  "10"
]
```
## 5. Podać typy oraz nazwy tych przedmiotów, z których zajęcia odbywają się w salach o numerach 2 lub 4 (8).

```aql
for r in rozklady
    filter r.NR_SALI == "2" || r.NR_SALI == "4"
    for p in przedmioty
        filter r.NR_PRZEDM == p._key
        return {
            typ: p.KOD_TPRZEDM,
            nazwa: p.NAZWA_PRZEDM
        }
```

8 elements

```json
{
  "typ": "WYK",
  "nazwa": "ALGEBRA"
},
```

## 6. Podać alfabetycznie nazwiska tych pracowników, którzy prowadzą zajęcia w salach zaopatrzonych w ekran, mogących pomieścić więcej niż 20 studentów (7).

```aql
for pr in pracownicy
    for s in sale
    filter s.ROZM_SALI > 20 && s.EKRAN == "T"
        for r in rozklady
        filter r.NR_SALI == s._key
    filter pr._key == r.NR_PRAC
    sort pr.NAZWISKO asc
return distinct pr.NAZWISKO
```

7 elements

```json
[
  "GRZYBIARZ",
  "JANECZEK",
  "PODWISLAK",
  "POPKO",
  "SKOREK",
  "WIEZYCKA",
  "WUJEK"
]
```

## 7. Podać nazwy przedmiotów oraz numery zespołów pracowników realizujących zajęcia z tych przedmiotów, odbywające się w środę o godz. 10:00 (2).

```aql
for r in rozklady
filter r.DZIEN == "SRO" AND r.GODZINA == 10
    for p in przedmioty
    filter p._key == r.NR_PRZEDM
        for pr in pracownicy
        filter pr._key == r.NR_PRAC
            for z in zespoly
            filter z._key == pr.NR_ZESP
return {
    nazwaPrzedmiotu: p.NAZWA_PRZEDM,
    numerZespolu: z._key
}
```

2 elements

```json
  {
    "nazwaPrzedmiotu": "MODELOWANIE CYFROWE",
    "numerZespolu": "6"
  },
```

## 8. Podać nazwy przedmiotów i nazwy ich przedmiotów nadrzędnych (4).

```aql
for p in przedmioty
    for pp in przedmioty
    filter p.NR_PRZEDM_NADRZ == pp._key
return {
    nazwaPrzemiotu: p.NAZWA_PRZEDM,
    nazwaNadrzednego: pp.NAZWA_PRZEDM
}
```

4 elements

```json
  {
    "nazwaPrzemiotu": "BAZY DANYCH",
    "nazwaNadrzednego": "BAZY DANYCH"
  },
```

## 9. Podać z ilu różnych przedmiotów prowadzi zajęcia pracownik SKRZYPEK (1/1).

```aql
for p in przedmioty
for r in rozklady
let skrzypek = (
    for pr in pracownicy
    filter pr.NAZWISKO == "SKRZYPEK"
    return pr._key
)
filter r.NR_PRAC == skrzypek[0]
filter p._key == r.NR_PRZEDM
collect nazwa = p.NAZWA_PRZEDM
collect with count into amount
return amount
```

1 element

```json
[
  1
]
```

## 10. Podać datę urodzenia najmłodszego pracownika odpowiadającego za prowadzenie przedmiotu (1).

```aql
for p in przedmioty
    for pr in pracownicy
    filter p.NR_ODP_PRAC == pr._key
    collect aggregate
        youngest = MAX(pr.DATA_UR)
return youngest
```

1 element

```json
[
  "1965-11-06 00:00:00"
]
```

## 11. Podać rozmiar największej sali, w której odbywają się zajęcia w poniedziałek między godz. 12:00 a 16:00 (1/32).

```aql
for s in sale
    for r in rozklady
    filter r.DZIEN == "PON" and r.GODZINA >= 12 and r.GODZINA <= 16
    filter r.NR_SALI == s._key
        collect aggregate biggest = MAX(s.ROZM_SALI)
        return biggest
```

1 element

```json
[
  32
]
```

## 12. Podać liczbę przedmiotów prowadzonych w poszczególnych salach (uwaga: przedmioty się powtarzają, zliczamy przedmioty, nie zajęcia) (14).

```aql

```

```json

```

## 13. Dla każdego przedmiotu podać średnią ocen (11).

```aql
for p in przedmioty
    let avg = (
    for o in oceny
        filter p._key == o.NR_PRZEDM
        collect aggregate avg = average(o.OCENA)
        return avg
    )
    filter avg[0] != null
    return {"nazwa": p.NAZWA_PRZEDM, "srednia":avg[0]}
```

11 Elements

```json
[
  {
    "nazwa": "BAZY DANYCH",
    "srednia": 3.7
  },
]
```

## 14. Podać liczbę zajęć realizowanych przez każdego odpowiedzialnego za przedmiot pracownika (8).

```aql
for p in przedmioty
    for pr in pracownicy
        filter pr._key == p.NR_ODP_PRAC
        for r in rozklady
            filter r.NR_PRAC == pr._key
            collect nazwiska = pr.NAZWISKO with count into total
            return {
                nazwisko: nazwiska,
                liczbaZajec: total
            }
```

8 elements

```json
  {
    "nazwisko": "BIERNAT",
    "liczbaZajec": 2
  },
```

## 15. Podać nazwy przedmiotów i ilość pracowników biorących w nich udział (4).

```aql
// todo
for p in przedmioty
    for r in rozklady
        collect pracownicy = r.NR_PRAC with count into il_prac
        return {"nr_prac":pracownicy, "il_prac":il_prac}
```

```json

```

## 16. Podać nazwiska pracowników mających więcej niż jedne zajęcia w PON (6).

```aql

```

```json

```

## 17. Podać przedmioty, z których zajęcia odbywają się w różnych salach (w więcej niż jednej sali) - (9).

```aql

```

```json

```

## 18. Podać nazwy przedmiotów, w których pracownicy odpowiedzialni za ich realizację są młodsi niż pracownik JANECZEK (2).

```aql

```

```json

```

## 19. Podać nazwiska pracowników, których średnia wszystkich wypłat jest mniejsza niż średnia wszystkich wypłat pracownika MISIURY (26).

```aql

```

```json

```

## 20. Podać nazwy przedmiotów, w realizacji których biorą udział pracownicy z większej liczby zespołów niż w realizacji przedmiotu ALGEBRA (1).

```aql

```

```json

```

