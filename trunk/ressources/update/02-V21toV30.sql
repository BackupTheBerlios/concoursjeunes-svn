SET MULTI_THREADED 1;

-- Suppression de 2 tables obsolète
DROP TABLE ARCHERSFFTA;
DROP TABLE ENTITEFFTA;

-- Localisation des données de référence en base
CREATE TABLE IF NOT EXISTS LIBELLE (
		ID_LIBELLE UUID NOT NULL DEFAULT RANDOM_UUID(),
		LANG VARCHAR(5),
		LIBELLE VARCHAR(255),
		PRIMARY KEY (ID_LIBELLE, LANG)
	);

-- Table des civilités
CREATE TABLE IF NOT EXISTS CIVILITY (
		ID_CIVILITY UUID NOT NULL DEFAULT RANDOM_UUID(),
		ABREVIATION VARCHAR(10),
		LIBELLE VARCHAR(64),
		MORALE BOOLEAN NOT NULL DEFAULT FALSE,
		LANG VARCHAR(5),
		PRIMARY KEY (ID_CIVILITY)
	);
INSERT INTO CIVILITY (ABREVIATION,LIBELLE,LANG) VALUES ('M.', 'Monsieur', 'fr');
INSERT INTO CIVILITY (ABREVIATION,LIBELLE,LANG) VALUES ('Mme', 'Madame', 'fr');
INSERT INTO CIVILITY (ABREVIATION,LIBELLE,LANG) VALUES ('Mlle', 'Mademoiselle', 'fr');
INSERT INTO CIVILITY (ABREVIATION,LIBELLE,LANG) VALUES ('Dr.', 'Docteur', 'fr');
INSERT INTO CIVILITY (ABREVIATION,LIBELLE,LANG) VALUES ('Me', 'Maître', 'fr');

-- Changement de la clé primaire de la table entité
-- Ajouts et modification de colonnes  
ALTER TABLE ENTITE DROP PRIMARY KEY;
ALTER TABLE ENTITE ADD ID_ENTITE UUID NOT NULL DEFAULT RANDOM_UUID() BEFORE AGREMENTENTITE;
ALTER TABLE ENTITE ADD PRIMARY KEY (ID_ENTITE);
ALTER TABLE ENTITE ADD REMOVABLE BOOLEAN NOT NULL DEFAULT 0;
ALTER TABLE ENTITE ALTER COLUMN AGREMENTENTITE VARCHAR(32);
CREATE INDEX IF NOT EXISTS I_AGREMENT_ENTITE ON ENTITE (AGREMENTENTITE ASC);

-- Gestion des contacts. Les archers hérite désormais de la table contact
CREATE TABLE IF NOT EXISTS CONTACT (
		ID_CONTACT UUID NOT NULL DEFAULT RANDOM_UUID(),
		NAME VARCHAR(128),
		FIRSTNAME VARCHAR(128),
		ID_CIVILITY UUID,
		ADDRESS VARCHAR(255),
		ZIP_CODE VARCHAR(10),
		CITY VARCHAR(64),
		NOTE TEXT,
		ID_ENTITE UUID,
		PRIMARY KEY (ID_CONTACT),
		FOREIGN KEY (ID_CIVILITY) REFERENCES CIVILITY (ID_CIVILITY) ON UPDATE CASCADE ON DELETE CASCADE,
		FOREIGN KEY (ID_ENTITE) REFERENCES ENTITE (ID_ENTITE) ON UPDATE CASCADE ON DELETE CASCADE
	);
CREATE INDEX IF NOT EXISTS I_NAME ON CONTACT (NAME ASC);
CREATE INDEX IF NOT EXISTS I_FIRSTNAME ON CONTACT (FIRSTNAME ASC);

-- Coordonnées associées aux contacts
CREATE TABLE IF NOT EXISTS COORDINATE (
		ID_COORDINATE UUID NOT NULL DEFAULT RANDOM_UUID(),
		ID_CONTACT UUID NOT NULL,
		CODE_COORDINATE_TYPE VARCHAR(10),
		"VALUE" VARCHAR(64),
		PRIMARY KEY (ID_COORDINATE),
		FOREIGN KEY (ID_CONTACT) REFERENCES CONTACT (ID_CONTACT) ON UPDATE CASCADE ON DELETE CASCADE
	);
	
-- Catégories de contacts
CREATE TABLE IF NOT EXISTS CATEGORIE_CONTACT (
		NUM_CATEGORIE_CONTACT INTEGER AUTO_INCREMENT NOT NULL,
		ID_LIBELLE UUID NOT NULL,
		PRIMARY KEY (NUM_CATEGORIE_CONTACT)
	);

SET @NEW_UUID = RANDOM_UUID();
INSERT INTO LIBELLE VALUES (@NEW_UUID,'fr','Archer(e)');
INSERT INTO LIBELLE VALUES (@NEW_UUID,'en','Bowman');
INSERT INTO CATEGORIE_CONTACT (NUM_CATEGORIE_CONTACT,ID_LIBELLE) VALUES (1,@NEW_UUID);
SET @NEW_UUID = RANDOM_UUID();
INSERT INTO LIBELLE VALUES (@NEW_UUID,'fr','Président(e)');
INSERT INTO LIBELLE VALUES (@NEW_UUID,'en','President');
INSERT INTO CATEGORIE_CONTACT (NUM_CATEGORIE_CONTACT,ID_LIBELLE) VALUES (2,@NEW_UUID);
SET @NEW_UUID = RANDOM_UUID();
INSERT INTO LIBELLE VALUES (@NEW_UUID,'fr','Secrétaire');
INSERT INTO LIBELLE VALUES (@NEW_UUID,'en','Secretary');
INSERT INTO CATEGORIE_CONTACT (NUM_CATEGORIE_CONTACT,ID_LIBELLE) VALUES (3,@NEW_UUID);
SET @NEW_UUID = RANDOM_UUID();
INSERT INTO LIBELLE VALUES (@NEW_UUID,'fr','Trésorier(e)');
INSERT INTO LIBELLE VALUES (@NEW_UUID,'en','Treasurer');
INSERT INTO CATEGORIE_CONTACT (NUM_CATEGORIE_CONTACT,ID_LIBELLE) VALUES (4,@NEW_UUID);
SET @NEW_UUID = RANDOM_UUID();
INSERT INTO LIBELLE VALUES (@NEW_UUID,'fr','Résponsable Inscription');
INSERT INTO LIBELLE VALUES (@NEW_UUID,'en','Registration Manager');
INSERT INTO CATEGORIE_CONTACT (NUM_CATEGORIE_CONTACT,ID_LIBELLE) VALUES (5,@NEW_UUID);
SET @NEW_UUID = RANDOM_UUID();
INSERT INTO LIBELLE VALUES (@NEW_UUID,'fr','Divers');
INSERT INTO LIBELLE VALUES (@NEW_UUID,'en','Diverse');
INSERT INTO CATEGORIE_CONTACT (NUM_CATEGORIE_CONTACT,ID_LIBELLE) VALUES (6,@NEW_UUID);

-- lien entre contacts et categories. Un contact peut être associé à plusieur catégories
CREATE TABLE IF NOT EXISTS ASSOCIER_CATEGORIE_CONTACT (
		ID_CONTACT UUID NOT NULL,
		NUM_CATEGORIE_CONTACT INTEGER NOT NULL,
		PRIMARY KEY (ID_CONTACT, NUM_CATEGORIE_CONTACT),
		FOREIGN KEY (ID_CONTACT) REFERENCES CONTACT (ID_CONTACT) ON UPDATE CASCADE ON DELETE CASCADE,
		FOREIGN KEY (NUM_CATEGORIE_CONTACT) REFERENCES CATEGORIE_CONTACT (NUM_CATEGORIE_CONTACT) ON UPDATE CASCADE ON DELETE CASCADE
	);

-- on modifie la structure des tables
-- ajout de la localisation au niveau de la base
ALTER TABLE PARAM ADD DEFAULT_LANG VARCHAR(5) NOT NULL DEFAULT 'fr';
-- Suppression des index sur des champs sur le  point d'être supprimé
DROP INDEX IF EXISTS I_NOM_ARCHER;
DROP INDEX IF EXISTS I_PRENOM_ARCHER;
-- La clé primaire de table de liaison entre archers et critère pose des problème
-- en modification, donc on place les données dans une table temporaire
-- puis on supprime complétement la table pour la recréer avec la nouvelle structure
-- une fois les tables parente modifié.
CREATE LOCAL TEMPORARY TABLE TEMP_DISTINGUER AS SELECT * FROM DISTINGUER;
DROP TABLE DISTINGUER;
-- modification de la clé primaire de la table archers
ALTER TABLE ARCHERS DROP PRIMARY KEY;
ALTER TABLE ARCHERS ADD ID_CONTACT UUID NOT NULL DEFAULT RANDOM_UUID() BEFORE NUMLICENCEARCHER;
ALTER TABLE ARCHERS ADD PRIMARY KEY (ID_CONTACT);
ALTER TABLE ARCHERS ALTER COLUMN NUMLICENCEARCHER VARCHAR(32);
CREATE INDEX IF NOT EXISTS I_NUMLICENCEARCHER ON ARCHERS (NUMLICENCEARCHER ASC);
-- On remplit la table des contacts
INSERT INTO CONTACT (ID_CONTACT,NAME,FIRSTNAME,ID_ENTITE) SELECT ID_CONTACT,NOMARCHER,PRENOMARCHER,ENTITE.ID_ENTITE FROM ARCHERS INNER JOIN ENTITE ON ENTITE.AGREMENTENTITE=ARCHERS.AGREMENTENTITE;
-- Une fois les données cohérente, on fait le lien d'héritage entre contact et archer
ALTER TABLE ARCHERS ADD FOREIGN KEY (ID_CONTACT) REFERENCES CONTACT (ID_CONTACT) ON UPDATE CASCADE ON DELETE CASCADE;
-- suppression des champs obsolète
ALTER TABLE ARCHERS DROP COLUMN NOMARCHER;
ALTER TABLE ARCHERS DROP COLUMN PRENOMARCHER;
ALTER TABLE ARCHERS DROP COLUMN AGREMENTENTITE;

-- regénération et population de la table de liason des crétéres de l'archers avec la nouvelle
-- clé primaire
CREATE TABLE DISTINGUER (
		ID_CONTACT UUID NOT NULL,
		NUMREGLEMENT INTEGER NOT NULL,
		NUMCRITERIASET INTEGER NOT NULL,
		PRIMARY KEY (ID_CONTACT, NUMREGLEMENT, NUMCRITERIASET),
		FOREIGN KEY (NUMREGLEMENT) REFERENCES REGLEMENT (NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE,
		FOREIGN KEY (NUMCRITERIASET) REFERENCES CRITERIASET (NUMCRITERIASET) ON UPDATE CASCADE ON DELETE CASCADE,
		FOREIGN KEY (ID_CONTACT) REFERENCES ARCHERS (ID_CONTACT) ON UPDATE CASCADE ON DELETE CASCADE;
	);
INSERT INTO DISTINGUER (ID_CONTACT, NUMREGLEMENT, NUMCRITERIASET) SELECT ID_CONTACT,NUMREGLEMENT,NUMCRITERIASET FROM TEMP_DISTINGUER D INNER JOIN ARCHERS A ON A.NUMLICENCEARCHER=D.NUMLICENCEARCHER;
DROP TABLE TEMP_DISTINGUER;

-- Comme dans les versions antérieurs de la base il n'y avait que des archers
-- on associe tous les contact présent en base à la catégorie "archer"
INSERT INTO ASSOCIER_CATEGORIE_CONTACT (ID_CONTACT, NUM_CATEGORIE_CONTACT) SELECT ID_CONTACT, 1 FROM CONTACT;