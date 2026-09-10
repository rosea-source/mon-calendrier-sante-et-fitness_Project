# Mon Calendrier Santé & Fitness

Application Android de suivi d'activité physique et de poids, développée dans le cadre du cours 420-G25-RO au Collège de Rosemont.

---

## Fonctionnalités

- **Calendrier mensuel** : visualisation des jours avec et sans séance (point vert / rouge)
- **Navigation mensuelle** : navigation entre les mois avec indicateurs visuels
- **Ajout de séances** : enregistrement avec type d'exercice, durée, intensité et poids
- **Historique** : consultation des séances passées et évolution du poids
- **Suivi du poids** : enregistrement hebdomadaire et calcul de la variation
- **Préférences utilisateur** : prénom et poids objectif sauvegardés via SharedPreferences

---

## Architecture

Le projet suit le pattern **MVP (Model-View-Presenter)** avec **SQLiteOpenHelper** pour la persistance locale.

```
app/
├── data/
│   ├── local/
│   │   ├── database/              # FitnessDbHelper (SQLiteOpenHelper)
│   │   ├── entity/                # Data classes : Seance, Pesee, TypeExercice
│   │   └── PreferencesManager.kt  # SharedPreferences
│   └── repository/                # SeanceRepository, PeseeRepository, TypeExerciceRepository
├── contract/                      # Interfaces MVP : AccueilContract, AjouterSeanceContract
├── presentateur/                  # AccueilPresenter, SeancePresentateur
└── ui/
    ├── accueil/                   # AccueilFragment
    ├── ajouter/                   # Fragment d'ajout de séance
    └── historique/                # Fragment historique
```

### Couches MVP

| Couche | Rôle |
|--------|------|
| **Model** | Data classes + Repository — accès SQLite via `SQLiteOpenHelper` |
| **View** | Fragments Android — affichage et interactions UI |
| **Presenter** | Logique métier — pont entre View et Model |

### Persistance

| Mécanisme | Usage |
|-----------|-------|
| **SQLite** (via `SQLiteOpenHelper`) | Séances, pesées, types d'exercice |
| **SharedPreferences** | Prénom et poids objectif de l'utilisateur |

---

## Stack technique

| Technologie | Version |
|-------------|---------|
| Kotlin | 2.1.0 |
| Android Gradle Plugin | 8.7.3 |
| compileSdk | 36 |
| minSdk | 24 |
| Navigation Component | 2.8.9 |
| JDK | 17 |

> Ce projet utilise **SQLiteOpenHelper** natif Android — aucune dépendance Room.

---

## Répartition du travail

| Tâche | Yumnah  | Alexandra Rose  | Amphy  |
|-------|-----------------|--------------------------|-----------------|
| Base de données SQLite | Support | Principal | |
| Écran 1 — Accueil | | Principal | Support |
| Écran 2 — Ajout de séance | Principal | Support | |
| Écran 3 — Historique & Poids | Support | | Principal |
| Tests unitaires | Partagé | Partagé | Partagé |
| Tests bout-en-bout | Partagé | Partagé | Partagé|

### Détail des contributions

**Yumnah Ahmed (6236429)**
- Création du projet de base et configuration initiale
- Écran d'ajout de séance (fragment, presenter, validation)
- Correction des imports et vérifications continues sur la branche `developpement`


**Alexandra Rose Cirius (2368820)**
- Écran d'accueil : calendrier mensuel, dots indicateurs, dialog préférences
- Migration Room → SQLiteOpenHelper
- Correction des branches Git (ajout et suppression)
- Vérifications continues sur la branche `developpement`

**Amphy Reyes (1423179)**
- Écran historique : liste des séances et suivi du poids
- Support écran d'accueil
- Support écran Ajouter
- Correction de UI

---

## Tests

### Tests unitaires (`com/test/`)

| Fichier | Couverture |
|---------|------------|
| `AccueilPresenterTest` | Chargement des séances, calcul des stats, cycle de vie du presenter |
| `SeancePresentateurTest` | Validation des champs, insertion|
| `HistoriquePresentateurTest` |  modification, suppression |

### Tests bout-en-bout Espresso (`com/androidTest/`)

| Fichier | Scénarios                                                      |
|---------|----------------------------------------------------------------|
| `AccueilEspressoTest` | Affichage du calendrier, format du total, navigation vers ajout |
| `AjouterSeanceTest` | Parcours complet d'ajout de séance                             |
| `HistoriqueEspressoTest` | Parcours complet d'historique                                  |

---

## Installation

### Prérequis

- Android Studio Hedgehog ou plus récent
- JDK 17
- Appareil ou émulateur Android API 24+

### Étapes

1. Cloner le dépôt :
   ```bash
   git clone https://git.dti.crosemont.quebec/6236429/mon-calendrier-sante-et-fitness.git
   ```
2. Ouvrir le projet dans Android Studio
3. Dans **File → Settings → Build Tools → Gradle**, sélectionner **JDK 17**
4. Cliquer **Sync Now**
5. Lancer l'app avec ▶

---

## Branches

| Branche | Description |
|---------|-------------|
| `developpement` | Branche principale de développement |
| `contrats-mvp-rose` | Contrats MVP (interfaces) |
| `presentateur-enregistrement` | Presenter d'ajout de séance |
| `documentation` | Documentation du projet |

---

## Équipe

| Membre | 
|--------|
| Yumnah Ahmed | 
| Alexandra Rose Cirius | 
| Amphy Reyes | 

Cours **420-G25-RO** — Collège de Rosemont — Session Hiver 2026

---

## Déclaration IA

L'utilisation de l'intelligence artificielle dans ce projet est documentée dans [`declaration-ia.md`](declaration-ia.md). 
