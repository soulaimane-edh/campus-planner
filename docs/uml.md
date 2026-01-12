# Rapport de conception (UML synthétique)

## Cas d'usage
- **Administrateur** : gérer comptes (créer/activer), créer groupes, associer modules ↔ groupes, nommer un enseignant responsable, planifier des séances avec contrôle de conflits.
- **Enseignant** : consulter ses modules et séances, proposer des créneaux (via planification), mettre à jour ses informations/compétences.
- **Étudiant** : consulter son emploi du temps et les modules associés à son groupe.
- **Bonus** : saisir et consulter les évaluations (notes, type, remarques, date).

## Diagramme de classes (texte)
- `UserAccount (id, fullName, email, password, role, active)`
- `Teacher (id, firstName, lastName, email, specialty, competencies, workloadHours)`
- `Student (id, firstName, lastName, email, fieldOfStudy, level, competencies, groupClass)`
- `GroupClass (id, name, level, fieldOfStudy, modules*)`
- `ModuleCourse (id, code, name, fieldOfStudy, semester, volumeHours, requiredCompetencies, responsibleTeacher, groups*)`
- `SessionSlot (id, moduleCourse, teacher, groupClass, room, startTime, endTime, sessionType)`
- `Evaluation (id, student, moduleCourse, assessmentType, grade, evaluationDate, remarks)`

Relations :
- `Teacher 1..1 --> 0..* ModuleCourse (responsable)`
- `ModuleCourse *<--> * GroupClass`
- `GroupClass 1..1 --> 0..* Student`
- `SessionSlot 1 --> 1 ModuleCourse`, `1 --> 1 Teacher`, `1 --> 1 GroupClass`
- `Evaluation 1 --> 1 Student`, `1 --> 1 ModuleCourse`
- `UserAccount` porte le rôle (ADMIN/TEACHER/STUDENT) et l'authentification simple.

## Séquence : Planifier une séance
1. Admin/enseignant saisit module, enseignant, groupe, salle, début, fin.
2. `SessionController` construit un `SessionSlot` et appelle `TimetableService.schedule()`.
3. `TimetableService` interroge `SessionSlotRepository` pour détecter : salle occupée, enseignant occupé, groupe occupé (chevauchement start/end).
4. S'il y a conflit → exception, message affiché dans la vue. Sinon `SessionSlot` est persisté.
5. La séance apparaît dans l'emploi du temps et sur le tableau de bord.
