MariaDB [gestion_notes]> show tables;
+-------------------------+
| Tables_in_gestion_notes |
+-------------------------+
| Enseignant              |
| Etudiant                |
| Etudiant_Module         |
| Formation               |
| Module                  |
| Module_Etudiant         |
| Note                    |
| ResponsablePedagogique  |
| Utilisateur             |
+-------------------------+
9 rows in set (0,001 sec)

MariaDB [gestion_notes]> desc Etudiant;
+---------------+--------------+------+-----+---------+-------+
| Field         | Type         | Null | Key | Default | Extra |
+---------------+--------------+------+-----+---------+-------+
| ine           | varchar(255) | NO   | PRI | NULL    |       |
| adresse       | varchar(255) | YES  |     | NULL    |       |
| dateNaissance | date         | YES  |     | NULL    |       |
| email         | varchar(255) | YES  |     | NULL    |       |
| nom           | varchar(255) | YES  |     | NULL    |       |
| prenoms       | varchar(255) | YES  |     | NULL    |       |
| sexe          | varchar(255) | YES  |     | NULL    |       |
| formation_id  | bigint(20)   | YES  | MUL | NULL    |       |
+---------------+--------------+------+-----+---------+-------+
8 rows in set (0,012 sec)

MariaDB [gestion_notes]> desc Enseignant;
+---------------+--------------+------+-----+---------+-------+
| Field         | Type         | Null | Key | Default | Extra |
+---------------+--------------+------+-----+---------+-------+
| adresse       | varchar(255) | YES  |     | NULL    |       |
| bureau        | varchar(255) | YES  |     | NULL    |       |
| dateNaissance | date         | YES  |     | NULL    |       |
| grade         | varchar(255) | YES  |     | NULL    |       |
| institution   | varchar(255) | YES  |     | NULL    |       |
| matricule     | varchar(255) | YES  | UNI | NULL    |       |
| sexe          | varchar(255) | YES  |     | NULL    |       |
| specialite    | varchar(255) | YES  |     | NULL    |       |
| id            | bigint(20)   | NO   | PRI | NULL    |       |
+---------------+--------------+------+-----+---------+-------+
9 rows in set (0,012 sec)

MariaDB [gestion_notes]> desc Formation;
+------------------+--------------+------+-----+---------+----------------+
| Field            | Type         | Null | Key | Default | Extra          |
+------------------+--------------+------+-----+---------+----------------+
| id               | bigint(20)   | NO   | PRI | NULL    | auto_increment |
| niveau           | varchar(255) | YES  |     | NULL    |                |
| nom              | varchar(255) | YES  |     | NULL    |                |
| responsableEmail | varchar(255) | YES  |     | NULL    |                |
| responsableNom   | varchar(255) | YES  |     | NULL    |                |
| responsable_id   | bigint(20)   | YES  | MUL | NULL    |                |
+------------------+--------------+------+-----+---------+----------------+
6 rows in set (0,014 sec)

MariaDB [gestion_notes]> desc Module;
+--------------------------+--------------+------+-----+---------+----------------+
| Field                    | Type         | Null | Key | Default | Extra          |
+--------------------------+--------------+------+-----+---------+----------------+
| id                       | bigint(20)   | NO   | PRI | NULL    | auto_increment |
| code                     | varchar(255) | YES  |     | NULL    |                |
| coefficient              | double       | NO   |     | NULL    |                |
| credits                  | int(11)      | NO   |     | NULL    |                |
| nom                      | varchar(255) | YES  |     | NULL    |                |
| volumeHoraire            | int(11)      | NO   |     | NULL    |                |
| enseignantResponsable_id | bigint(20)   | YES  | MUL | NULL    |                |
| formation_id             | bigint(20)   | YES  | MUL | NULL    |                |
+--------------------------+--------------+------+-----+---------+----------------+
8 rows in set (0,015 sec)

MariaDB [gestion_notes]> desc ResponsablePedagogique;
+-------+------------+------+-----+---------+-------+
| Field | Type       | Null | Key | Default | Extra |
+-------+------------+------+-----+---------+-------+
| id    | bigint(20) | NO   | PRI | NULL    |       |
+-------+------------+------+-----+---------+-------+
1 row in set (0,014 sec)

MariaDB [gestion_notes]> desc ResponsablePedagogique;
+-------+------------+------+-----+---------+-------+
| Field | Type       | Null | Key | Default | Extra |
+-------+------------+------+-----+---------+-------+
| id    | bigint(20) | NO   | PRI | NULL    |       |
+-------+------------+------+-----+---------+-------+
1 row in set (0,002 sec)

MariaDB [gestion_notes]> desc Utilisateur;
+----------+--------------+------+-----+---------+----------------+
| Field    | Type         | Null | Key | Default | Extra          |
+----------+--------------+------+-----+---------+----------------+
| id       | bigint(20)   | NO   | PRI | NULL    | auto_increment |
| email    | varchar(255) | YES  | UNI | NULL    |                |
| nom      | varchar(255) | YES  |     | NULL    |                |
| password | varchar(255) | YES  |     | NULL    |                |
| prenom   | varchar(255) | YES  |     | NULL    |                |
| role     | varchar(255) | YES  |     | NULL    |                |
+----------+--------------+------+-----+---------+----------------+
6 rows in set (0,003 sec)

MariaDB [gestion_notes]> 
MariaDB [gestion_notes]> desc Etudiant_Module;
+--------------+--------------+------+-----+---------+-------+
| Field        | Type         | Null | Key | Default | Extra |
+--------------+--------------+------+-----+---------+-------+
| Etudiant_ine | varchar(255) | NO   | MUL | NULL    |       |
| modules_id   | bigint(20)   | NO   | MUL | NULL    |       |
+--------------+--------------+------+-----+---------+-------+
2 rows in set (0,013 sec)

MariaDB [gestion_notes]> 
MariaDB [gestion_notes]> desc Module_Etudiant;
+---------------+--------------+------+-----+---------+-------+
| Field         | Type         | Null | Key | Default | Extra |
+---------------+--------------+------+-----+---------+-------+
| Module_id     | bigint(20)   | NO   | MUL | NULL    |       |
| etudiants_ine | varchar(255) | NO   | MUL | NULL    |       |
+---------------+--------------+------+-----+---------+-------+
2 rows in set (0,015 sec)

MariaDB [gestion_notes]> 






-- Insertion du responsable pédagogique
INSERT INTO Utilisateur (email, nom, prenom, password, role) 
VALUES ('admin@zig-univ.sn', 'Diop', 'Mamadou', 'admin', 'responsable');

INSERT INTO ResponsablePedagogique (id)
SELECT id FROM Utilisateur WHERE email = 'admin@zig-univ.sn';

-- Insertion des formations
INSERT INTO Formation (nom, niveau, responsable_id, responsableNom, responsableEmail) 
VALUES ('Informatique', 'Licence 1', (SELECT id FROM Utilisateur WHERE email = 'admin@zig-univ.sn'), 'Diop Mamadou', 'admin@zig-univ.sn');

INSERT INTO Formation (nom, niveau, responsable_id, responsableNom, responsableEmail) 
VALUES ('Gestion', 'Licence 1', (SELECT id FROM Utilisateur WHERE email = 'admin@zig-univ.sn'), 'Diop Mamadou', 'admin@zig-univ.sn');

-- Récupération des IDs des formations
SET @id_form_1 = (SELECT id FROM Formation WHERE nom = 'Informatique');
SET @id_form_2 = (SELECT id FROM Formation WHERE nom = 'Gestion');

-- Insertion des enseignants
INSERT INTO Utilisateur (email, nom, prenom, password, role) VALUES ('enseignant1@zig-univ.sn', 'Fall', 'Awa', 'pass123', 'enseignant');
INSERT INTO Utilisateur (email, nom, prenom, password, role) VALUES ('enseignant2@zig-univ.sn', 'Sarr', 'Alioune', 'pass123', 'enseignant');
INSERT INTO Utilisateur (email, nom, prenom, password, role) VALUES ('enseignant3@zig-univ.sn', 'Ba', 'Fatou', 'pass123', 'enseignant');
INSERT INTO Utilisateur (email, nom, prenom, password, role) VALUES ('enseignant4@zig-univ.sn', 'Ndoye', 'Ibrahima', 'pass123', 'enseignant');

-- Insertion des enseignants dans la table Enseignant
INSERT INTO Enseignant (id, adresse, bureau, dateNaissance, grade, institution, matricule, sexe, specialite)
SELECT id, 'Ziguinchor', 'B101', '1980-05-10', 'Maitre de conférences', 'Zig-Univ', 'ENS001', 'F', 'Informatique' FROM Utilisateur WHERE email = 'enseignant1@zig-univ.sn';

INSERT INTO Enseignant (id, adresse, bureau, dateNaissance, grade, institution, matricule, sexe, specialite)
SELECT id, 'Ziguinchor', 'B102', '1978-08-22', 'Professeur', 'Zig-Univ', 'ENS002', 'M', 'Mathématiques' FROM Utilisateur WHERE email = 'enseignant2@zig-univ.sn';

INSERT INTO Enseignant (id, adresse, bureau, dateNaissance, grade, institution, matricule, sexe, specialite)
SELECT id, 'Ziguinchor', 'B103', '1985-12-15', 'Maitre Assistant', 'Zig-Univ', 'ENS003', 'F', 'Gestion' FROM Utilisateur WHERE email = 'enseignant3@zig-univ.sn';

INSERT INTO Enseignant (id, adresse, bureau, dateNaissance, grade, institution, matricule, sexe, specialite)
SELECT id, 'Ziguinchor', 'B104', '1982-09-30', 'Professeur', 'Zig-Univ', 'ENS004', 'M', 'Économie' FROM Utilisateur WHERE email = 'enseignant4@zig-univ.sn';

-- Insertion des modules pour la formation Informatique
INSERT INTO Module (code, nom, coefficient, credits, volumeHoraire, enseignantResponsable_id, formation_id) 
VALUES ('INFO101', 'Algorithmique', 3, 6, 45, (SELECT id FROM Utilisateur WHERE email = 'enseignant1@zig-univ.sn'), @id_form_1);

INSERT INTO Module (code, nom, coefficient, credits, volumeHoraire, enseignantResponsable_id, formation_id) 
VALUES ('INFO102', 'Bases de données', 3, 6, 45, (SELECT id FROM Utilisateur WHERE email = 'enseignant2@zig-univ.sn'), @id_form_1);

INSERT INTO Module (code, nom, coefficient, credits, volumeHoraire, enseignantResponsable_id, formation_id) 
VALUES ('INFO103', 'Programmation C', 3, 6, 45, (SELECT id FROM Utilisateur WHERE email = 'enseignant1@zig-univ.sn'), @id_form_1);

INSERT INTO Module (code, nom, coefficient, credits, volumeHoraire, enseignantResponsable_id, formation_id) 
VALUES ('INFO104', 'Réseaux', 3, 6, 45, (SELECT id FROM Utilisateur WHERE email = 'enseignant2@zig-univ.sn'), @id_form_1);

INSERT INTO Module (code, nom, coefficient, credits, volumeHoraire, enseignantResponsable_id, formation_id) 
VALUES ('INFO105', 'Systèmes d\'exploitation', 3, 6, 45, (SELECT id FROM Utilisateur WHERE email = 'enseignant1@zig-univ.sn'), @id_form_1);

INSERT INTO Module (code, nom, coefficient, credits, volumeHoraire, enseignantResponsable_id, formation_id) 
VALUES ('INFO106', 'Sécurité informatique', 3, 6, 45, (SELECT id FROM Utilisateur WHERE email = 'enseignant2@zig-univ.sn'), @id_form_1);

-- Insertion des modules pour la formation Gestion
INSERT INTO Module (code, nom, coefficient, credits, volumeHoraire, enseignantResponsable_id, formation_id) 
VALUES ('GEST101', 'Comptabilité', 3, 6, 45, (SELECT id FROM Utilisateur WHERE email = 'enseignant3@zig-univ.sn'), @id_form_2);

INSERT INTO Module (code, nom, coefficient, credits, volumeHoraire, enseignantResponsable_id, formation_id) 
VALUES ('GEST102', 'Marketing', 3, 6, 45, (SELECT id FROM Utilisateur WHERE email = 'enseignant4@zig-univ.sn'), @id_form_2);

INSERT INTO Module (code, nom, coefficient, credits, volumeHoraire, enseignantResponsable_id, formation_id) 
VALUES ('GEST103', 'Droit des affaires', 3, 6, 45, (SELECT id FROM Utilisateur WHERE email = 'enseignant3@zig-univ.sn'), @id_form_2);

INSERT INTO Module (code, nom, coefficient, credits, volumeHoraire, enseignantResponsable_id, formation_id) 
VALUES ('GEST104', 'Finance', 3, 6, 45, (SELECT id FROM Utilisateur WHERE email = 'enseignant4@zig-univ.sn'), @id_form_2);

INSERT INTO Module (code, nom, coefficient, credits, volumeHoraire, enseignantResponsable_id, formation_id) 
VALUES ('GEST105', 'Management', 3, 6, 45, (SELECT id FROM Utilisateur WHERE email = 'enseignant3@zig-univ.sn'), @id_form_2);

INSERT INTO Module (code, nom, coefficient, credits, volumeHoraire, enseignantResponsable_id, formation_id) 
VALUES ('GEST106', 'Ressources humaines', 3, 6, 45, (SELECT id FROM Utilisateur WHERE email = 'enseignant4@zig-univ.sn'), @id_form_2);

-- Insertion des étudiants
INSERT INTO Etudiant (ine, adresse, dateNaissance, email, nom, prenoms, sexe, formation_id) 
VALUES ('E001', 'Ziguinchor', '2003-01-10', 'etu1@zig-univ.sn', 'Dieng', 'Abdou', 'M', @id_form_1);

INSERT INTO Etudiant (ine, adresse, dateNaissance, email, nom, prenoms, sexe, formation_id) 
VALUES ('E002', 'Ziguinchor', '2002-02-15', 'etu2@zig-univ.sn', 'Faye', 'Aminata', 'F', @id_form_1);

INSERT INTO Etudiant (ine, adresse, dateNaissance, email, nom, prenoms, sexe, formation_id) 
VALUES ('E003', 'Ziguinchor', '2003-03-20', 'etu3@zig-univ.sn', 'Sow', 'Cheikh', 'M', @id_form_1);

INSERT INTO Etudiant (ine, adresse, dateNaissance, email, nom, prenoms, sexe, formation_id) 
VALUES ('E004', 'Ziguinchor', '2002-04-25', 'etu4@zig-univ.sn', 'Diop', 'Fatou', 'F', @id_form_1);

INSERT INTO Etudiant (ine, adresse, dateNaissance, email, nom, prenoms, sexe, formation_id) 
VALUES ('E005', 'Ziguinchor', '2003-05-30', 'etu5@zig-univ.sn', 'Ba', 'Moussa', 'M', @id_form_1);

INSERT INTO Etudiant (ine, adresse, dateNaissance, email, nom, prenoms, sexe, formation_id) 
VALUES ('E006', 'Ziguinchor', '2003-06-10', 'etu6@zig-univ.sn', 'Ndour', 'Saliou', 'M', @id_form_1);

INSERT INTO Etudiant (ine, adresse, dateNaissance, email, nom, prenoms, sexe, formation_id) 
VALUES ('E007', 'Ziguinchor', '2003-07-15', 'etu7@zig-univ.sn', 'Gaye', 'Awa', 'F', @id_form_1);

INSERT INTO Etudiant (ine, adresse, dateNaissance, email, nom, prenoms, sexe, formation_id) 
VALUES ('E008', 'Ziguinchor', '2003-08-20', 'etu8@zig-univ.sn', 'Ka', 'Ibrahima', 'M', @id_form_1);

INSERT INTO Etudiant (ine, adresse, dateNaissance, email, nom, prenoms, sexe, formation_id) 
VALUES ('E009', 'Ziguinchor', '2003-09-25', 'etu9@zig-univ.sn', 'Camara', 'Mame', 'F', @id_form_1);

INSERT INTO Etudiant (ine, adresse, dateNaissance, email, nom, prenoms, sexe, formation_id) 
VALUES ('E010', 'Ziguinchor', '2003-10-30', 'etu10@zig-univ.sn', 'Fall', 'Serigne', 'M', @id_form_1);

INSERT INTO Etudiant (ine, adresse, dateNaissance, email, nom, prenoms, sexe, formation_id) 
VALUES ('E011', 'Ziguinchor', '2003-01-10', 'etu11@zig-univ.sn', 'Mbaye', 'Ndeye', 'F', @id_form_2);

INSERT INTO Etudiant (ine, adresse, dateNaissance, email, nom, prenoms, sexe, formation_id) 
VALUES ('E012', 'Ziguinchor', '2003-02-15', 'etu12@zig-univ.sn', 'Diouf', 'Ibrahima', 'M', @id_form_2);

INSERT INTO Etudiant (ine, adresse, dateNaissance, email, nom, prenoms, sexe, formation_id) 
VALUES ('E013', 'Ziguinchor', '2003-03-20', 'etu13@zig-univ.sn', 'Sene', 'Mame', 'F', @id_form_2);

INSERT INTO Etudiant (ine, adresse, dateNaissance, email, nom, prenoms, sexe, formation_id) 
VALUES ('E014', 'Ziguinchor', '2003-04-25', 'etu14@zig-univ.sn', 'Lo', 'Amadou', 'M', @id_form_2);

INSERT INTO Etudiant (ine, adresse, dateNaissance, email, nom, prenoms, sexe, formation_id) 
VALUES ('E015', 'Ziguinchor', '2003-05-30', 'etu15@zig-univ.sn', 'Thiam', 'Aissatou', 'F', @id_form_2);

INSERT INTO Etudiant (ine, adresse, dateNaissance, email, nom, prenoms, sexe, formation_id) 
VALUES ('E016', 'Ziguinchor', '2003-06-10', 'etu16@zig-univ.sn', 'Dia', 'Mamadou', 'M', @id_form_2);

INSERT INTO Etudiant (ine, adresse, dateNaissance, email, nom, prenoms, sexe, formation_id) 
VALUES ('E017', 'Ziguinchor', '2003-07-15', 'etu17@zig-univ.sn', 'Ndour', 'Fatima', 'F', @id_form_2);

INSERT INTO Etudiant (ine, adresse, dateNaissance, email, nom, prenoms, sexe, formation_id) 
VALUES ('E018', 'Ziguinchor', '2003-08-20', 'etu18@zig-univ.sn', 'Fofana', 'Ousmane', 'M', @id_form_2);

INSERT INTO Etudiant (ine, adresse, dateNaissance, email, nom, prenoms, sexe, formation_id) 
VALUES ('E019', 'Ziguinchor', '2003-09-25', 'etu19@zig-univ.sn', 'Ba', 'Khadija', 'F', @id_form_2);

INSERT INTO Etudiant (ine, adresse, dateNaissance, email, nom, prenoms, sexe, formation_id) 
VALUES ('E020', 'Ziguinchor', '2003-10-30', 'etu20@zig-univ.sn', 'Fall', 'El Hadji', 'M', @id_form_2);


