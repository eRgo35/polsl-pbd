# Laboratorium z Podstaw Baz Danych
## Temat: Tranzakcje

### Data wykonania ćwiczenia: 23.05.2024
### Przygotowali: Michał Czyż, Dawid Głąb
### Grupa 6, Sekcja 13

Poziomy izolacji:
- Read Uncommitted
- Read Committed
- Repeatable Read
- Serializable

# 1.1. Read Uncommitted - Read Uncommitted

A. Pojawia się zjawisko brudnego odczytu. Zmieniliśmy dane w drugiej tranzakcji, i później je przywróciliśmy, ale w pierwszej tranzakcji, SELECT pokazał tą zmienioną formę.

B. UPDATE wykonał się prawidłowo.

C. Występuje brudny odczyt, ponieważ tranzakcja nie dobiegła do końca, a widać jej zmianę.

D. Wynik zgadza się z tym co zostało przyjęte po tranzakcji, choć pierwsza tranzakcja nie dobiegła do końca.

E. Wykonało się prawidłowo.

F. Wykonało się prawidłowo.

G. Lock wait timeout spowodowany zajęciem zasoby przez pierwszą tranzakcję.

# 1.2. Read Uncommitted - Read Committed

A. pojawia się zjawisko brudnego odczytu. Choć dane w tranzakcji zostały przywrócone, polecenie SELECT, otrzymało dane, które były tymczasowe.

B. UPDATE wykonał się prawidłowo.

C. Występuje brudny odczyt, ponieważ tranzakcja nie dobiegła do końca, a widać jej zmianę.

D. Wynik zgadza się z tym co zostało przyjęte po tranzakcji, choć pierwsza tranzakcja nie dobiegła do końca.

E. Wykonało się prawidłowo.

F. Wykonało się prawidłowo.

G. Lock wait timeout spowodowany zajęciem zasoby przez pierwszą tranzakcję.

# 1.3. Read Uncommitted - Repeatable Read

A. pojawia się zjawisko brudnego odczytu. Zmieniliśmy dane w drugiej tranzakcji, i później je przywróciliśmy, ale w pierwszej tranzakcji, SELECT pokazał tą zmienioną formę.

B. UPDATE wykonał się prawidłowo.

C. Występuje brudny odczyt, ponieważ tranzakcja nie dobiegła do końca, a widać jej zmianę.

D. Wynik zgadza się z tym co zostało przyjęte po tranzakcji, choć pierwsza tranzakcja nie dobiegła do końca.

E. Wykonało się prawidłowo.

F. Wykonało się prawidłowo.

G. Lock wait timeout spowodowany zajęciem zasoby przez pierwszą tranzakcję.


# 1.4. Read Uncommitted - Serializable

A. pojawia się zjawisko brudnego odczytu. Zmieniliśmy dane w drugiej tranzakcji, i później je przywróciliśmy, ale w pierwszej tranzakcji, SELECT pokazał tą zmienioną formę.

B. UPDATE wykonał się prawidłowo.

C. Występuje brudny odczyt, ponieważ tranzakcja nie dobiegła do końca, a widać jej zmianę.

D. Wynik zgadza się z tym co zostało przyjęte po tranzakcji, choć pierwsza tranzakcja nie dobiegła do końca.

E. Wykonało się prawidłowo.

F. Lock wait timeout spowodowany zajęciem zasoby przez drugą tranzakcję.

G. Wykonało się prawidłowo.

Wnioski: Lock timeout wykonał się inaczej niż w poprzednich, ponieważ Serializable, blokuje całą tabelę przed zapisem i odczytem, i odblokowuje ją dopiero po zakończeniu tranzakcji.

# 2.1. Read Committed - Read Uncommitted

A. Nie występuje brudny odczyt.

B. UPDATE wykonał się prawidłowo.

C. Nie występuje brudny odczyt.

D. Wynik zgadza się z tym co zostało przyjęte po tranzakcji, choć pierwsza tranzakcja nie dobiegła do końca.

E. Wykonało się prawidłowo.

F. Wykonało się prawidłowo.

G. Lock wait timeout spowodowany zajęciem zasoby przez pierwszą tranzakcję.

# 2.2. Read Committed - Read Committed

A. Nie występuje brudny odczyt.

B. UPDATE wykonał się prawidłowo.

C. Nie występuje brudny odczyt.

D. Wynik zgadza się z tym co zostało przyjęte po tranzakcji, choć pierwsza tranzakcja nie dobiegła do końca.

E. Wykonało się prawidłowo.

F. Wykonało się prawidłowo.

G. Lock wait timeout spowodowany zajęciem zasoby przez pierwszą tranzakcję.

# 2.3. Read Committed - Repeatable Read

A. Nie występuje brudny odczyt.

B. UPDATE wykonał się prawidłowo.

C. Nie występuje brudny odczyt.

D. Wynik zgadza się z tym co zostało przyjęte po tranzakcji, choć pierwsza tranzakcja nie dobiegła do końca.

E. Wykonało się prawidłowo.

F. Wykonało się prawidłowo.

G. Lock wait timeout spowodowany zajęciem zasoby przez pierwszą tranzakcję.

# 2.4. Read Committed - Serializable

A. Nie występuje brudny odczyt.

B. UPDATE wykonał się prawidłowo.

C. Nie występuje brudny odczyt.

D. Wynik zgadza się z tym co zostało przyjęte po tranzakcji, choć pierwsza tranzakcja nie dobiegła do końca.

E. Wykonało się prawidłowo.

F. Lock wait timeout spowodowany zajęciem zasoby przez drugą tranzakcję.

G. Wykonało się prawidłowo.

# 3.1. Repeatable Read - Read Uncommitted 

A. Nie występuje brudny odczyt.

B. UPDATE wykonał się prawidłowo.

C. Nie występuje brudny odczyt.

D. Po wykonaniu commit nie wyświetla się zmiana polecenia z UPDATE, gdyż pierwsza tranzakcja nie dobiegła końca.

E. Wykonało się prawidłowo.

F. Wykonało się prawidłowo.

G. Lock wait timeout spowodowany zajęciem zasoby przez pierwszą tranzakcję.

# 3.2. Repeatable Read - Read Committed

A. Nie występuje brudny odczyt.

B. UPDATE wykonał się prawidłowo.

C. Nie występuje brudny odczyt.

D. Po wykonaniu commit nie wyświetla się zmiana polecenia z UPDATE, gdyż pierwsza tranzakcja nie dobiegła końca.

E. Wykonało się prawidłowo.

F. Wykonało się prawidłowo.

G. Lock wait timeout spowodowany zajęciem zasoby przez pierwszą tranzakcję.

# 3.3. Repeatable Read - Repeatable Read

A. Nie występuje brudny odczyt.

B. UPDATE wykonał się prawidłowo.

C. Nie występuje brudny odczyt.

D. Po wykonaniu commit nie wyświetla się zmiana polecenia z UPDATE, gdyż pierwsza tranzakcja nie dobiegła końca.

E. Wykonało się prawidłowo.

F. Wykonało się prawidłowo.

G. Lock wait timeout spowodowany zajęciem zasoby przez pierwszą tranzakcję.

# 3.4. Repeatable Read - Serializable

A. Nie występuje brudny odczyt.

B. UPDATE wykonał się prawidłowo.

C. Nie występuje brudny odczyt.

D. Po wykonaniu commit nie wyświetla się zmiana polecenia z UPDATE, gdyż pierwsza tranzakcja nie dobiegła końca.

E. Wykonało się prawidłowo.

F. Lock wait timeout spowodowany zajęciem zasoby przez drugą tranzakcję.

G. Wykonało się prawidłowo.

# 4.1. Serializable - Read Uncommitted

A. Lock wait timeout spowodowany zajęciem zasoby przez drugą tranzakcję.

B. Lock wait timeout spowodowany zajęciem zasoby przez pierwszą tranzakcję.

C. Wykonało się poprawnie.

D. Wykonało się poprawnie.

E. Wykonało się poprawnie.

F. Wykonało się prawidłowo.

G. Lock wait timeout spowodowany zajęciem zasoby przez pierwszą tranzakcję.

# 4.2. Serializable - Read Committed

A. Lock wait timeout spowodowany zajęciem zasoby przez drugą tranzakcję.

B. Lock wait timeout spowodowany zajęciem zasoby przez pierwszą tranzakcję.

C. Wykonało się poprawnie.

D. Wykonało się poprawnie.

E. Wykonało się poprawnie.

F. Wykonało się prawidłowo.

G. Lock wait timeout spowodowany zajęciem zasoby przez pierwszą tranzakcję.

# 4.3. Serializable - Repeatable Read

A. Lock wait timeout spowodowany zajęciem zasoby przez drugą tranzakcję.

B. Lock wait timeout spowodowany zajęciem zasoby przez pierwszą tranzakcję.

C. Wykonało się poprawnie.

D. Wykonało się poprawnie.

E. Wykonało się poprawnie.

F. Wykonało się prawidłowo.

G. Lock wait timeout spowodowany zajęciem zasoby przez pierwszą tranzakcję.

# 4.4. Serializable - Serializable

A. Lock wait timeout spowodowany zajęciem zasoby przez drugą tranzakcję.

B. Lock wait timeout spowodowany zajęciem zasoby przez pierwszą tranzakcję.

C. Wykonało się poprawnie.

D. Wykonało się poprawnie.

E. Wykonało się poprawnie.

F. Lock wait timeout spowodowany zajęciem zasoby przez drugą tranzakcję.

G. Lock wait timeout spowodowany zajęciem zasoby przez pierwszą tranzakcję.