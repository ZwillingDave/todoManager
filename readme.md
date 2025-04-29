# TodoManager

Ein einfacher To-Do-Manager mit Benutzeranmeldung, Teamfunktion und gemeinsamer Aufgabenverwaltung.

## 🔧 Funktionen

- ✅ Benutzer können sich registrieren und einloggen
- 📝 Nach dem Login können persönliche To-Dos erstellt, bearbeitet und gelöscht werden
- 👥 Benutzer können Teams erstellen und andere Nutzer einladen
- 📋 Aufgaben von Teams werden für alle Mitglieder sichtbar
- 🚧 **Frontend noch in Entwicklung – Coming Soon!**

## 🛠️ Tech Stack

- **Backend**: Java mit Spring Boot
- **Datenbank**: H2 (In-Memory)

## 🚦 Roadmap

- [x] Benutzer-Authentifizierung
- [x] Persönliche To-Dos
- [ ] Team-Erstellung & Einladungen
- [ ] Anzeige der To-Dos (Frontend)
- [ ] Integration mit persistenter Datenbank (z.B. PostgreSQL)

## 📡 API-Beispiele

### 🔐 Authentifizierung

```http
POST /api/auth/signup
POST /api/auth/login
```

### ✅ To-Dos

```http
GET    /api/todos             // Eigene To-Dos abrufen
POST   /api/todos             // Neues To-Do erstellen
PUT    /api/todos/{id}        // To-Do bearbeiten
DELETE /api/todos/{id}        // To-Do löschen
```

### 👥 Groups

```http
POST   /api/groups/add              // Neues Team erstellen
POST   /api/groups/{uuid}/invite    // Nutzer ins Team einladen
GET    /api/groups                  // Gruppen des eingeloggten Nutzers
GET    /api/groups/{id}/todos       // To-Dos der Gruppe
GET    /api/groups/{id}/users       // Mitglieder der Gruppe
```

## 🧾 Beispiel-Datenobjekte

### ToDo

```json

```

### Team

```json

```

## Aktueller Status

ToDoManager befindet sich in einer frühen Entwicklungsphase. Die grundlegenden Backend-Funktionen (API-Endpunkte, Authentifizierung, Datenmodell) sind implementiert.