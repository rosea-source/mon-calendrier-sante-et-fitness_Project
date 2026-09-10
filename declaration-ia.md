# Déclaration d'utilisation de l'intelligence artificielle

## Projet : Mon Calendrier Santé et Fitness
**Cours :** 420-G25-RO — Développement d'applications mobiles  
**Session :** Hiver 2026  
**Établissement :** Collège de Rosemont

---

## 1. Outils d'IA utilisés

| Outil | Utilisation |
|-------|-------------|
| **Claude (Anthropic)** | Assistance au débogage, génération de code, révision d'architecture |

---

## 2. Utilisation dans le projet

L'intelligence artificielle a été utilisée comme outil d'assistance tout au long du développement de ce projet Android. Voici les domaines où elle a été sollicitée :

### 2.1 Débogage et corrections
- Résolution d'erreurs Gradle (JDK, `kotlinOptions`, versions de dépendances)
- Correction d'imports incorrects liés à la migration Room → SQLiteOpenHelper
- Résolution de conflits Git lors de merges entre branches
- Correction d'attributs XML invalides (`android:cornerRadius` → `app:cornerRadius`)

### 2.2 Génération de code
- Structure de fichiers XML de layout (`fragment_accueil.xml`, `activity_main.xml`, `dialog_preferences.xml`)
- Fichiers de configuration (`AndroidManifest.xml`, `build.gradle.kts`)
- Logique du calendrier dynamique dans `AccueilFragment.kt`
- Migration complète de Room vers SQLiteOpenHelper (`FitnessDbHelper`, Repository avec `Cursor` et `ContentValues`)
- Tests unitaires avec mocks (`AccueilPresenterTest`, `SeancePresentateurTest`)
- Tests Espresso bout-en-bout (`AccueilEspressoTest`)
- SharedPreferences (`PreferencesManager`, `dialog_preferences.xml`)

### 2.3 Architecture et structure
- Conseils sur le pattern MVP (Model-View-Presenter)
- Organisation des couches `data`, `ui`, `presentateur`, `contract`
- Gestion de la base de données SQLite via `SQLiteOpenHelper`
- Mise en place des Repository (`SeanceRepository`, `PeseeRepository`, `TypeExerciceRepository`)
- Séparation des responsabilités entre Fragment (affichage) et Presenter (logique)

### 2.4 Commandes Git
- Aide aux opérations Git (stash, rebase, merge, gestion de conflits)
- Messages de commit descriptifs

---

## 3. Déclaration éthique

### 3.1 Transparence
L'utilisation de l'IA est déclarée de façon complète et honnête. Aucune partie du code ou de la documentation générée par l'IA n'est présentée comme étant entièrement le fruit d'un travail personnel sans assistance.

### 3.2 Responsabilité
Tout le code généré ou suggéré par l'IA a été :
- **Lu et compris** avant d'être intégré au projet
- **Testé** sur émulateur et appareil physique (Samsung)
- **Adapté** selon les besoins spécifiques du projet
- **Validé** par l'équipe avant chaque commit

### 3.3 Apprentissage
L'IA a été utilisée comme un outil pédagogique — similaire à un tutoriel ou un mentor — et non comme un substitut à la compréhension. Les concepts expliqués par l'IA ont été assimilés et appliqués de façon autonome dans la suite du développement.

Par exemple, la migration de Room vers SQLiteOpenHelper a permis à l'équipe de comprendre en profondeur le fonctionnement bas niveau de SQLite sur Android : création des tables avec `onCreate`, lecture avec `Cursor`, écriture avec `ContentValues`, et gestion du cycle de vie de la base de données.

### 3.4 Limites de l'utilisation
L'IA n'a **pas** été utilisée pour :
- Remplacer la réflexion sur l'architecture globale du projet
- Générer des tests automatisés sans que l'équipe les comprenne et les valide
- Produire la documentation finale sans révision humaine
- Prendre des décisions de conception à la place de l'équipe

---

## 4. Politique d'utilisation de l'établissement

L'utilisation de l'IA dans ce projet respecte la politique du Collège de Rosemont concernant l'intégrité académique. L'IA est utilisée comme outil d'apprentissage et de productivité, et non comme moyen de contournement des objectifs pédagogiques.

---

*Document rédigé par l'équipe et révisé avec l'assistance de Claude (Anthropic).*  
*Date : Mai 2026*