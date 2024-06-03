# Laboratorium z Podstaw Baz Danych

## Temat: NoSQL

### Data wykonania ćwiczenia: 23.05.2024

### Przygotowali: Michał Czyż, Dawid Głąb

### Grupa 6, Sekcja 13

### Zestaw C

## 1. Podaj numery sal, w których odbywają się zajęcia z przedmiotu o numerze 20 (4)

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

## 2. Podać nazwiska studentów zaczynające się od litery M (13)

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

## 3. Podać nazwy przedmiotów, za które odpowiedzialny jest pracownik JANECZEK (3)

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

## 4. Podać numery sal, w których zajęcia rozpoczynają się przed godz. 14:00. Wyświetl w porządku od najwcześniej rozpoczynających się zajęć (23)

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

## 5. Podać typy oraz nazwy tych przedmiotów, z których zajęcia odbywają się w salach o numerach 2 lub 4 (8)

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

## 6. Podać alfabetycznie nazwiska tych pracowników, którzy prowadzą zajęcia w salach zaopatrzonych w ekran, mogących pomieścić więcej niż 20 studentów (7)

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

## 7. Podać nazwy przedmiotów oraz numery zespołów pracowników realizujących zajęcia z tych przedmiotów, odbywające się w środę o godz. 10:00 (2)

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

## 8. Podać nazwy przedmiotów i nazwy ich przedmiotów nadrzędnych (4)

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

## 9. Podać z ilu różnych przedmiotów prowadzi zajęcia pracownik SKRZYPEK (1/1)

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

## 10. Podać datę urodzenia najmłodszego pracownika odpowiadającego za prowadzenie przedmiotu (1)

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

## 11. Podać rozmiar największej sali, w której odbywają się zajęcia w poniedziałek między godz. 12:00 a 16:00 (1/32)

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

## 12. Podać liczbę przedmiotów prowadzonych w poszczególnych salach (uwaga: przedmioty się powtarzają, zliczamy przedmioty, nie zajęcia) (14)

```aql
for r in rozklady
collect sale = r.NR_SALI into groupedSale

let iloscPrzemiotow = length(
    for groupedSala in groupedSale
        filter groupedSala.r.NR_PRZEDM in (
        for p in przedmioty
            return p._key
        )
return distinct groupedSala.r.NR_PRZEDM
)

return { 
    "sala": sale, 
    "iloscPrzedmiotow": iloscPrzemiotow 
}
```

14 elements

```json
  {
    "sala": "1",
    "iloscPrzedmiotow": 3
  },
```

## 13. Dla każdego przedmiotu podać średnią ocen (11)

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

## 14. Podać liczbę zajęć realizowanych przez każdego odpowiedzialnego za przedmiot pracownika (8)

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

## 15. Podać nazwy przedmiotów i ilość pracowników biorących w nich udział (4)

```aql
for p in przedmioty
    for r in rozklady
        filter p._key == r.NR_PRZEDM
        for pr in pracownicy
            filter pr._key == r.NR_PRAC
            collect subjects = p.NAZWA_PRZEDM into grouped_subjects
            
            let il_prac = count_distinct(
                for grouped_subject in grouped_subjects
                    return grouped_subject.r.NR_PRAC
            )
return {
    "przedmiot": subjects,
    "il_prac": il_prac
}
```

11 elements

Ilość wyników się nie zgadza. Ale skoro należy podać nazwy przedmiotów i ilu pracowników bierze w nich udział to dotyczy to wszystkich przedmiotów, a ich jest (11).

```json
  {
    "przedmiot": "AIPSK",
    "il_prac": 1
  },
```

## 16. Podać nazwiska pracowników mających więcej niż jedne zajęcia w PON (6)

```aql
for p in pracownicy
  for r in rozklady
  filter r.NR_PRAC == p._key and r.DZIEN == "PON"
  collect prac = p.NAZWISKO into pracG
  let n = count(
    for g in pracG
    return g.r._key
  )
  return {"pracownik": prac, "liczba zajęć": n}
```

6 elements

```json
[
  {
    "pracownik": "GRZYBEK",
    "liczba zajęć": 2
  },
]
```

## 17. Podać przedmioty, z których zajęcia odbywają się w różnych salach (w więcej niż jednej sali) - (9)

```aql
for p in przedmioty
  for r in rozklady
  filter r.NR_PRZEDM == p._key
  for s in sale
    collect name = p.NAZWA_PRZEDM into nameG
    let n = count_distinct(
      for g in nameG
      return g.r.NR_SALI
    )
    filter n > 1
    return {"przedmiot": name, "ilość sal": n}
```

9 elements

```json
[
  {
    "przedmiot": "AIPSK",
    "ilość sal": 2
  },
]
```

## 18. Podać nazwy przedmiotów, w których pracownicy odpowiedzialni za ich realizację są młodsi niż pracownik JANECZEK (2)

```aql
let ja = (
  for p in pracownicy
  filter p.NAZWISKO == "JANECZEK"
  return p.DATA_UR
)[0]
for p in pracownicy
  filter p.DATA_UR > ja
  for pr in przedmioty
    filter pr.NR_ODP_PRAC == p._key
    return {"przedmiot": pr.NAZWA_PRZEDM}

```

2 elements

```json
[
  {
    "przedmiot": "ANALIZA MATEMATYCZNA"
  },
]
```

## 19. Podać nazwiska pracowników, których średnia wszystkich wypłat jest mniejsza niż średnia wszystkich wypłat pracownika MISIURY (26)

```aql
let mout=(
  for p in pracownicy
    filter p.NAZWISKO == "MISIURA"
    for w in wyplaty
      filter w.NR_PRAC == p._key
      collect pr = {"id":p._key} into pg
      let a = average(
        for g in pg
        return g.w.KWOTA
      )
      return a
)[0]
for p in pracownicy
for w in wyplaty
  filter w.NR_PRAC == p._key
  collect pr = {"nazwisko":p.NAZWISKO, "id":p._key} into pg
  let aw = average(
    for g in pg
    return g.w.KWOTA
  )
  filter aw < mout
  return {"nazwisko": pr.nazwisko, "średnia wypłata": aw}
```

26 elements

```json
[
  {
    "nazwisko": "GRZYBEK",
    "średnia wyplat": 537.7777777777778
  },
]
```

## 20. Podać nazwy przedmiotów, w realizacji których biorą udział pracownicy z większej liczby zespołów niż w realizacji przedmiotu ALGEBRA (1)

```aql
let algn = (
  for pr in przedmioty
    filter pr.NAZWA_PRZEDM == "ALGEBRA"
    for r in rozklady
      filter r.NR_PRZEDM = pr._key
      for p in pracownicy
        filter r.NR_PRAC
        collect prz = {"id":pr._key} into pg
        let an = count_distinct(
          for g in pg
            return g.p.NR_ZESP
        )
        return an
)[0]

for pr in przedmioty
  for r in rozklady
    filter r.NR_PRZEDM == pr._key
    for p in pracownicy
      filter r.NR_PRAC == p._key
      collect prz={"id":pr._key, "nazwa":pr.NAZWA_PRZEDM} into pg
      let n = count_distinct(
        for g in pg
          return g.p.NR_ZESP
      )
      filter n > algn
      return {"nazwaPrzedmiotu": prz.nazwa}
```

1 element

```json
[
  {
    "nazwaPrzedmiotu": "BAZY DANYCH"
  }
]
```
