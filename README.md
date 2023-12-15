# Oceny aplikacji mobilnych

Do utworzenia bazy wykorzystane jest ORM i JPA, więc struktura utworzona jest automatycznie przy uruchomieniu aplikacji.
Bazą danych jest in-memory H2, tak więc dane są dostępne tylko gdy aplikacja jest uruchomiona. 

Dla ułatwienia dodawania danych do bazy jest endpoint POST `/utils/force-import`, który wymusza import wszystkich plików z
danymi wejściowymi ze wskazanego katalogu z danymi (parametr `application.properties:import.dir`, domyślnie katalog 
`oceny`).

Pozostałe endpointy, zgodnie z poleceniem to:
 - GET `/api/{appUuid}/avg?since={dateSince}&until={dateUntil)`
 - GET `/api/top-apps/{ageGroup}?since={dateSince}&until={dateUntil}`

A także dodatkowy, pomocniczy, do wymuszenia generowania raportów - generuje raport z obecnego miesiąca:
 - POST `/utils/force-export`