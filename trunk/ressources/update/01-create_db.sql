-- Création de la base de données de ArcCompetition
-- @author Aurélien JEOFFRAY
CREATE TABLE IF NOT EXISTS PARAM (
		DBVERSION INTEGER NOT NULL,
		APPREVISION INTEGER NOT NULL,
		DEFAULT_LANG VARCHAR(5) NOT NULL DEFAULT 'fr',
		DBUUID UUID NOT NULL DEFAULT RANDOM_UUID()
	);
INSERT INTO PARAM (DBVERSION, APPREVISION, DEFAULT_LANG) VALUES (30, 1, 'fr');

CREATE TABLE IF NOT EXISTS LIBELLE (
		ID_LIBELLE UUID NOT NULL DEFAULT RANDOM_UUID(),
		LANG VARCHAR(5),
		LIBELLE VARCHAR(255),
		PRIMARY KEY (ID_LIBELLE, LANG)
	);

CREATE TABLE IF NOT EXISTS FEDERATION (
		NUMFEDERATION	INTEGER AUTO_INCREMENT NOT NULL,
		SIGLEFEDERATION	VARCHAR(32),
		NOMFEDERATION	VARCHAR(128),
		PAYS			VARCHAR(2),
		PRIMARY KEY (NUMFEDERATION)
	);
INSERT INTO FEDERATION VALUES (1, 'FITA', STRINGDECODE('F\u00e9d\u00e9ration International de Tir \u00e0 l''Arc'), 'fr');
INSERT INTO FEDERATION VALUES (2, 'FFTA', STRINGDECODE('F\u00e9d\u00e9ration Fran\u00e7aise de Tir \u00e0 l''Arc'), 'fr');

CREATE TABLE NIVEAU_COMPETITION (
		CODENIVEAU		INTEGER,
		NUMFEDERATION	INTEGER,
		LANG			VARCHAR(5),
		LIBELLE			VARCHAR(64),
		DEFAUT			BOOLEAN,
		PRIMARY KEY (CODENIVEAU, NUMFEDERATION, LANG),
		FOREIGN KEY (NUMFEDERATION) REFERENCES FEDERATION (NUMFEDERATION) ON UPDATE CASCADE ON DELETE CASCADE
	);
INSERT INTO NIVEAU_COMPETITION VALUES (1, 1, 'fr', 'Qualificatif', TRUE);
INSERT INTO NIVEAU_COMPETITION VALUES (2, 1, 'fr', 'Championnat d''Europe', FALSE);
INSERT INTO NIVEAU_COMPETITION VALUES (3, 1, 'fr', 'Coupe du Monde', FALSE);
INSERT INTO NIVEAU_COMPETITION VALUES (4, 1, 'fr', 'Jeux Olympique', FALSE);
INSERT INTO NIVEAU_COMPETITION VALUES (1, 1, 'en', 'Qualifier', TRUE);
INSERT INTO NIVEAU_COMPETITION VALUES (2, 1, 'en', 'European Championship', FALSE);
INSERT INTO NIVEAU_COMPETITION VALUES (3, 1, 'en', 'World Cup', FALSE);
INSERT INTO NIVEAU_COMPETITION VALUES (4, 1, 'en', 'Olympic Games', FALSE);
INSERT INTO NIVEAU_COMPETITION VALUES (1, 2, 'fr', 'Qualificatif', TRUE);
INSERT INTO NIVEAU_COMPETITION VALUES (2, 2, 'fr', STRINGDECODE('D\u00e9partemental'), FALSE);
INSERT INTO NIVEAU_COMPETITION VALUES (3, 2, 'fr', STRINGDECODE('R\u00e9gional'), FALSE);
INSERT INTO NIVEAU_COMPETITION VALUES (4, 2, 'fr', 'Championnat de France', FALSE);
INSERT INTO NIVEAU_COMPETITION VALUES (5, 2, 'fr', 'Championnat d''Europe', FALSE);
INSERT INTO NIVEAU_COMPETITION VALUES (6, 2, 'fr', 'Coupe du Monde', FALSE);
INSERT INTO NIVEAU_COMPETITION VALUES (7, 2, 'fr', 'Jeux Olympique', FALSE);

CREATE TABLE REPARTITION_PHASE_FINALE (
		NUM_REPARTITION_PHASE_FINALE SMALLINT NOT NULL,
		NUM_TYPE_REPARTITION SMALLINT NOT NULL,
		NUM_ORDRE SMALLINT NOT NULL,
		PRIMARY KEY (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION)
	);
-- Réglement Internationnal
-- Groupe A
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(0, 1, 1);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(1, 1, 64);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(2, 1, 33);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(3, 1, 32);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(4, 1, 17);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(5, 1, 48);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(6, 1, 49);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(7, 1, 16);
--Groupe B
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(8, 1, 9);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(9, 1, 56);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(10, 1, 41);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(11, 1, 24);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(12, 1, 25);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(13, 1, 40);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(14, 1, 57);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(15, 1, 8);
--Groupe C
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(16, 1, 5);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(17, 1, 60);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(18, 1, 37);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(19, 1, 28);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(20, 1, 21);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(21, 1, 44);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(22, 1, 53);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(23, 1, 12);
-- Groupe D
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(24, 1, 13);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(25, 1, 52);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(26, 1, 45);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(27, 1, 20);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(28, 1, 29);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(29, 1, 36);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(30, 1, 61);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(31, 1, 4);
-- Groupe E
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(32, 1, 3);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(33, 1, 62);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(34, 1, 35);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(35, 1, 30);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(36, 1, 19);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(37, 1, 46);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(38, 1, 51);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(39, 1, 14);
--Groupe F
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(40, 1, 11);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(41, 1, 54);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(42, 1, 43);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(43, 1, 22);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(44, 1, 27);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(45, 1, 38);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(46, 1, 59);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(47, 1, 6);
-- Groupe G
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(48, 1, 7);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(49, 1, 58);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(50, 1, 39);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(51, 1, 26);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(52, 1, 23);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(53, 1, 42);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(54, 1, 55);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(55, 1, 10);
-- Groupe H
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(56, 1, 15);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(57, 1, 50);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(58, 1, 47);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(59, 1, 18);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(60, 1, 31);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(61, 1, 34);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(62, 1, 63);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(63, 1, 2);

-- Réglement Français
-- Groupe A
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(0, 2, 44);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(1, 2, 21);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(2, 2, 53);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(3, 2, 12);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(4, 2, 37);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(5, 2, 28);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(6, 2, 60);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(7, 2, 5);
--Groupe B
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(8, 2, 45);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(9, 2, 20);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(10, 2, 52);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(11, 2, 13);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(12, 2, 36);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(13, 2, 29);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(14, 2, 61);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(15, 2, 4);
--Groupe C
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(16, 2, 41);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(17, 2, 24);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(18, 2, 56);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(19, 2, 9);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(20, 2, 40);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(21, 2, 25);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(22, 2, 57);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(23, 2, 8);
-- Groupe D
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(24, 2, 48);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(25, 2, 17);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(26, 2, 49);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(27, 2, 16);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(28, 2, 33);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(29, 2, 32);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(30, 2, 64);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(31, 2, 1);
-- Groupe E
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(32, 2, 2);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(33, 2, 63);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(34, 2, 31);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(35, 2, 34);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(36, 2, 15);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(37, 2, 50);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(38, 2, 18);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(39, 2, 47);
--Groupe F
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(40, 2, 7);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(41, 2, 58);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(42, 2, 26);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(43, 2, 39);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(44, 2, 10);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(45, 2, 55);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(46, 2, 23);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(47, 2, 42);
-- Groupe G
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(48, 2, 3);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(49, 2, 62);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(50, 2, 30);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(51, 2, 35);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(52, 2, 14);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(53, 2, 51);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(54, 2, 19);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(55, 2, 46);
-- Groupe H
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(56, 2, 6);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(57, 2, 59);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(58, 2, 27);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(59, 2, 38);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(60, 2, 11);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(61, 2, 54);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(62, 2, 22);
INSERT INTO REPARTITION_PHASE_FINALE (NUM_REPARTITION_PHASE_FINALE, NUM_TYPE_REPARTITION, NUM_ORDRE) VALUES(63, 2, 43);

CREATE TABLE TYPE_EQUIPE (
		CODETYPEEQUIPE	VARCHAR(16),
		NUMFEDERATION	INTEGER,
		LANG			VARCHAR(5),
		LIBELLE			VARCHAR(64),
		PRIMARY KEY (CODETYPEEQUIPE, NUMFEDERATION, LANG),
		FOREIGN KEY (NUMFEDERATION) REFERENCES FEDERATION (NUMFEDERATION) ON UPDATE CASCADE ON DELETE CASCADE
	);
INSERT INTO TYPE_EQUIPE VALUES ('D1', 2, 'fr', 'Division 1');
INSERT INTO TYPE_EQUIPE VALUES ('D2', 2, 'fr', 'Division 2');
INSERT INTO TYPE_EQUIPE VALUES ('DR', 2, 'fr', STRINGDECODE('Division R\u00e9gional'));
INSERT INTO TYPE_EQUIPE VALUES ('DNAP', 2, 'fr', STRINGDECODE('Division National Arc \u00e0 Poulies'));
INSERT INTO TYPE_EQUIPE VALUES ('DE', 2, 'fr', STRINGDECODE('Duel Equipe'));
INSERT INTO TYPE_EQUIPE VALUES ('LIBRE', 2, 'fr', '');

CREATE TABLE CATEGORIE_REGLEMENT (
		NUMCATEGORIE_REGLEMENT	INTEGER AUTO_INCREMENT NOT NULL,
		NOMCATEGORIE	VARCHAR(128),
		PRIMARY KEY (NUMCATEGORIE_REGLEMENT)
	);
INSERT INTO CATEGORIE_REGLEMENT VALUES (1, 'Jeunes');
INSERT INTO CATEGORIE_REGLEMENT VALUES (2, 'Salle');
INSERT INTO CATEGORIE_REGLEMENT VALUES (3, STRINGDECODE('Ext\u00e9rieur'));
INSERT INTO CATEGORIE_REGLEMENT VALUES (4, 'Autre');

CREATE TABLE REGLEMENT (
		NUMREGLEMENT INTEGER AUTO_INCREMENT NOT NULL,
		NOMREGLEMENT VARCHAR(32),
		LIBELLE	VARCHAR(64),
		DESCRIPTION VARCHAR(255),
		NBSERIE INTEGER,
		NBVOLEEPARSERIE INTEGER,
		NBFLECHEPARVOLEE INTEGER,
		NBPOINTSPARFLECHE INTEGER,
		NBMEMBRESEQUIPE INTEGER,
		NBMEMBRESRETENU INTEGER,
		ISOFFICIAL BOOLEAN,
		NUMFEDERATION INTEGER NOT NULL,
		NUMCATEGORIE_REGLEMENT INTEGER NOT NULL,
		TYPEREGLEMENT VARCHAR(10) NOT NULL DEFAULT 'TARGET',
		REMOVABLE BOOLEAN,
		PRIMARY KEY (NUMREGLEMENT),
		UNIQUE (NOMREGLEMENT),
		FOREIGN KEY (NUMFEDERATION) REFERENCES FEDERATION (NUMFEDERATION) ON UPDATE CASCADE ON DELETE RESTRICT,
		FOREIGN KEY (NUMCATEGORIE_REGLEMENT) REFERENCES CATEGORIE_REGLEMENT (NUMCATEGORIE_REGLEMENT) ON UPDATE CASCADE ON DELETE RESTRICT
	);
CREATE INDEX I_NOM_REGLEMENT ON REGLEMENT (NOMREGLEMENT ASC);

CREATE TABLE DEPARTAGE (
		NUMDEPARTAGE INTEGER AUTO_INCREMENT NOT NULL,
		NUMREGLEMENT INTEGER, 
		FIELDNAME VARCHAR(64),
		PRIMARY KEY (NUMDEPARTAGE, NUMREGLEMENT),
		FOREIGN KEY (NUMREGLEMENT) REFERENCES REGLEMENT (NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE
	);
	
CREATE TABLE CRITERE (
		CODECRITERE VARCHAR(16) NOT NULL,
		NUMREGLEMENT INTEGER NOT NULL,
		LIBELLECRITERE VARCHAR(32),
		SORTORDERCRITERE INTEGER,
		CLASSEMENT BOOLEAN,
		CLASSEMENTEQUIPE BOOLEAN DEFAULT FALSE,
		PLACEMENT BOOLEAN,
		CODEFFTA VARCHAR(16),
		NUMORDRE INTEGER NOT NULL,
		PRIMARY KEY (CODECRITERE, NUMREGLEMENT),
		FOREIGN KEY (NUMREGLEMENT) REFERENCES REGLEMENT (NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE
	);

CREATE TABLE CRITEREELEMENT (
		CODECRITEREELEMENT VARCHAR(16) NOT NULL,
		CODECRITERE VARCHAR(16) NOT NULL,
		NUMREGLEMENT INTEGER NOT NULL,
		LIBELLECRITEREELEMENT VARCHAR(32),
		ACTIF BOOLEAN,
		NUMORDRE INTEGER NOT NULL,
		PRIMARY KEY (CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT),
		FOREIGN KEY (CODECRITERE, NUMREGLEMENT) REFERENCES CRITERE (CODECRITERE, NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE
	);

CREATE TABLE BLASONS (
		NUMBLASON INTEGER AUTO_INCREMENT NOT NULL,
		NOMBLASON VARCHAR(32) NOT NULL,
		HORIZONTAL_RATIO DOUBLE NOT NULL,
		VERTICAL_RATIO DOUBLE NOT NULL,
		NBARCHER INTEGER NOT NULL,
		NUMORDRE INTEGER NOT NULL,
		IMAGE VARCHAR(64) NOT NULL DEFAULT '',
		PRIMARY KEY (NUMBLASON)
	);
INSERT INTO BLASONS VALUES (1, '122cm', 1, 1, 4, 122, 'targetface_fita_122.png');
INSERT INTO BLASONS VALUES (2, '80cm', 1, 1, 4, 80, 'targetface_fita_80.png');
INSERT INTO BLASONS VALUES (3, '60cm', 0.5, 1, 2, 60, 'targetface_fita_60.png');
INSERT INTO BLASONS VALUES (4, '40cm', 0.5, 0.5, 1, 40, 'targetface_fita_40.png');
INSERT INTO BLASONS VALUES (5, 'Tri Spot "Vegas"', 0.5, 0.5, 1, 39, 'targetface_fita_trispot_vegas.png');
INSERT INTO BLASONS VALUES (6, 'Tri Spot Vertical', 0.25, 1, 1, 24, 'targetface_fita_trispot.png');
INSERT INTO BLASONS VALUES (7, 'Tri Spot 60 Vertical', 0.25, 1, 1, 59, 'targetface_fita_trispot_60.png');
INSERT INTO BLASONS VALUES (8, 'Carte Beursault (Championnats)', 1, 1, 4, 23, 'targetface_ffta_beursault.png');
INSERT INTO BLASONS VALUES (9, 'Carte Beursault (Bouquet)', 1, 1, 4, 23, 'targetface_ffta_beursault_bouquet.png');
INSERT INTO BLASONS VALUES (10, '80cm Réduit (5-10)', 0.5, 0.5, 1, 79, 'targetface_fita_80_reducted.png');

CREATE TABLE ANCRAGES_BLASONS (
		NUMBLASON INTEGER NOT NULL,
		EMPLACEMENT INTEGER NOT NULL,
		ANCRAGEX DOUBLE NOT NULL,
		ANCRAGEY DOUBLE NOT NULL,
		PRIMARY KEY (NUMBLASON, EMPLACEMENT),
		FOREIGN KEY (NUMBLASON) REFERENCES BLASONS (NUMBLASON) ON UPDATE CASCADE ON DELETE CASCADE
	);
-- Emplacement:
-- 0 à 3 postion A, B, C, D
-- 4 les 4 à la fois, 5 AC, 6 BD
INSERT INTO ANCRAGES_BLASONS VALUES (1, 4, 0, 0);
INSERT INTO ANCRAGES_BLASONS VALUES (2, 4, 0, 0);
INSERT INTO ANCRAGES_BLASONS VALUES (3, 5, 0, 0);
INSERT INTO ANCRAGES_BLASONS VALUES (3, 6, 0.5, 0);
INSERT INTO ANCRAGES_BLASONS VALUES (4, 0, 0, 0);
INSERT INTO ANCRAGES_BLASONS VALUES (4, 1, 0.5, 0);
INSERT INTO ANCRAGES_BLASONS VALUES (4, 2, 0, 0.5);
INSERT INTO ANCRAGES_BLASONS VALUES (4, 3, 0.5, 0.5);
INSERT INTO ANCRAGES_BLASONS VALUES (5, 0, 0, 0);
INSERT INTO ANCRAGES_BLASONS VALUES (5, 1, 0.5, 0);
INSERT INTO ANCRAGES_BLASONS VALUES (5, 2, 0, 0.5);
INSERT INTO ANCRAGES_BLASONS VALUES (5, 3, 0.5, 0.5);
INSERT INTO ANCRAGES_BLASONS VALUES (6, 0, 0, 0);
INSERT INTO ANCRAGES_BLASONS VALUES (6, 1, 0.5, 0);
INSERT INTO ANCRAGES_BLASONS VALUES (6, 2, 0.25, 0);
INSERT INTO ANCRAGES_BLASONS VALUES (6, 3, 0.75, 0);
INSERT INTO ANCRAGES_BLASONS VALUES (7, 0, 0, 0);
INSERT INTO ANCRAGES_BLASONS VALUES (7, 1, 0.5, 0);
INSERT INTO ANCRAGES_BLASONS VALUES (7, 2, 0.25, 0);
INSERT INTO ANCRAGES_BLASONS VALUES (7, 3, 0.75, 0);
INSERT INTO ANCRAGES_BLASONS VALUES (8, 4, 0, 0);
INSERT INTO ANCRAGES_BLASONS VALUES (9, 4, 0, 0);
INSERT INTO ANCRAGES_BLASONS VALUES (10, 0, 0, 0);
INSERT INTO ANCRAGES_BLASONS VALUES (10, 1, 0.5, 0);
INSERT INTO ANCRAGES_BLASONS VALUES (10, 2, 0, 0.5);
INSERT INTO ANCRAGES_BLASONS VALUES (10, 3, 0.5, 0.5);

CREATE TABLE CRITERIASET (
		NUMCRITERIASET INTEGER AUTO_INCREMENT NOT NULL,
		NUMREGLEMENT INTEGER,
		IDCRITERIASET VARCHAR(255) NOT NULL,
		PRIMARY KEY (NUMCRITERIASET),
		FOREIGN KEY (NUMREGLEMENT) REFERENCES REGLEMENT (NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE,
		UNIQUE (IDCRITERIASET)
	);
CREATE INDEX I_ID_CRITERIASET ON CRITERIASET (IDCRITERIASET ASC);

CREATE TABLE DISTANCESBLASONS (
		NUMDISTANCESBLASONS INTEGER AUTO_INCREMENT NOT NULL,
		NUMREGLEMENT INTEGER NOT NULL,
		NUMBLASON INTEGER NOT NULL,
		NUMCRITERIASET INTEGER NOT NULL,
		DEFAULTTARGETFACE BOOLEAN NOT NULL,
		PRIMARY KEY (NUMDISTANCESBLASONS, NUMREGLEMENT),
		FOREIGN KEY (NUMREGLEMENT) REFERENCES REGLEMENT (NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE,
		FOREIGN KEY (NUMBLASON) REFERENCES BLASONS (NUMBLASON) ON UPDATE CASCADE ON DELETE CASCADE,
		FOREIGN KEY (NUMCRITERIASET) REFERENCES CRITERIASET (NUMCRITERIASET) ON UPDATE CASCADE ON DELETE CASCADE
	);

CREATE TABLE DISTANCES (
		NUMDISTANCES INTEGER AUTO_INCREMENT NOT NULL,
		NUMDISTANCESBLASONS INTEGER NOT NULL,
		NUMREGLEMENT INTEGER NOT NULL,
		DISTANCE INTEGER,
		PRIMARY KEY (NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT),
		FOREIGN KEY (NUMDISTANCESBLASONS, NUMREGLEMENT) REFERENCES DISTANCESBLASONS (NUMDISTANCESBLASONS, NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE
	);

CREATE TABLE POSSEDE (
		NUMCRITERIASET INTEGER NOT NULL,
		CODECRITEREELEMENT VARCHAR(16) NOT NULL,
		CODECRITERE VARCHAR(16) NOT NULL,
		NUMREGLEMENT INTEGER NOT NULL,
		PRIMARY KEY (NUMCRITERIASET, CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT),
		FOREIGN KEY (NUMCRITERIASET) REFERENCES CRITERIASET (NUMCRITERIASET) ON UPDATE CASCADE ON DELETE CASCADE,
		FOREIGN KEY (CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT) REFERENCES CRITEREELEMENT (CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE
	);

CREATE TABLE SURCLASSEMENT (
		NUMCRITERIASET INTEGER NOT NULL,
		NUMREGLEMENT INTEGER NOT NULL,
		NUMCRITERIASET_SURCLASSE INTEGER NULL,
		PRIMARY KEY (NUMCRITERIASET, NUMREGLEMENT),
		FOREIGN KEY (NUMCRITERIASET) REFERENCES CRITERIASET (NUMCRITERIASET) ON UPDATE CASCADE ON DELETE CASCADE,
		FOREIGN KEY (NUMREGLEMENT) REFERENCES REGLEMENT (NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE,
		FOREIGN KEY (NUMCRITERIASET_SURCLASSE) REFERENCES CRITERIASET (NUMCRITERIASET) ON UPDATE CASCADE ON DELETE CASCADE
	);

--CREATE TABLE ARCHERSFFTA (
--		NUMLICENCEARCHER VARCHAR(64) NOT NULL,
--		NOMARCHER VARCHAR(64),
--		PRENOMARCHER VARCHAR(64),
--		ALIASARCHER VARCHAR(128),
--		CLUBARCHER VARCHAR(64),
--		ARCARCHER INTEGER,
--		CATEGORIEARCHER INTEGER,
--		GENREARCHER INTEGER,
--		NIVEAUARCHER CHAR(1),
--		LIBELLENIVEAU VARCHAR(1),
--		LIBELLECATEGORIE VARCHAR(8),
--		AGREMENTCLUBARCHER VARCHAR(7),
--		DATENAISS VARCHAR(8),
--		CERTIFMEDICAL BOOLEAN,
--		INC2 INTEGER,
--		PRIMARY KEY (NUMLICENCEARCHER)
--	);

--CREATE TABLE ENTITEFFTA (
--		NUMENTITE INTEGER NOT NULL,
--		AGREMENTENTITE VARCHAR(7),
--		NOMENTITE VARCHAR(64),
--		VILLEENTITE VARCHAR(64),
--		LIGUEENTITE VARCHAR(2),
--		CDENTITE VARCHAR(9),
--		PRIMARY KEY (NUMENTITE)
--	);
CREATE TABLE VILLE (
	    CODE_VILLE UUID DEFAULT RANDOM_UUID(),
	    INSEE VARCHAR(5) NOT NULL,
	    NOM VARCHAR(100),
	    UPPER_NAME VARCHAR(100) as UPPER(NOM),
	    ALTITUDE VARCHAR(25),
	    LONGITUDE_RADIAN VARCHAR(50),
	    LATITUDE_RADIAN VARCHAR(50),
	    POP99 VARCHAR(25),
	    SURFACE VARCHAR(25),
	    PAYS VARCHAR(2),
	    PRIMARY KEY(CODE_VILLE)
	);  
CREATE INDEX IF NOT EXISTS I_VILLE_UPPER_NAME ON VILLE (UPPER_NAME ASC);
CREATE INDEX IF NOT EXISTS I_VILLE_PAYS ON VILLE (PAYS ASC);

CREATE TABLE IF NOT EXISTS CODE_POSTAL (
		CODE_POSTAL VARCHAR(5),
		CODE_VILLE UUID NOT NULL,
		PRIMARY KEY (CODE_POSTAL,CODE_VILLE),
		FOREIGN KEY(CODE_VILLE) REFERENCES VILLE (CODE_VILLE) ON DELETE CASCADE ON UPDATE CASCADE
	);
CREATE INDEX IF NOT EXISTS I_CODE_POSTAL_CODE_POSTAL ON CODE_POSTAL (CODE_POSTAL ASC);

CREATE TABLE IF NOT EXISTS ENTITE (
		ID_ENTITE UUID NOT NULL DEFAULT RANDOM_UUID(),
		AGREMENTENTITE VARCHAR(32),
		NUMFEDERATION INTEGER NOT NULL,
		NOMENTITE VARCHAR(64),
		ADRESSEENTITE CLOB,
		CODEPOSTALENTITE VARCHAR(5),
		VILLEENTITE VARCHAR(64),
		PAYS VARCHAR(2),
		NOTEENTITE CLOB,
		TYPEENTITE INTEGER,
		DATEMODIF DATE DEFAULT CURRENT_DATE(),
		REMOVABLE BOOLEAN DEFAULT 1,
		PRIMARY KEY (ID_ENTITE)
	);
CREATE INDEX IF NOT EXISTS I_AGREMENT_ENTITE ON ENTITE (AGREMENTENTITE ASC, NUMFEDERATION ASC);
CREATE INDEX IF NOT EXISTS I_NOM_ENTITE ON ENTITE (NOMENTITE ASC);
CREATE INDEX IF NOT EXISTS I_VILLE_ENTITE ON ENTITE (VILLEENTITE ASC);

CREATE TABLE IF NOT EXISTS CIVILITY (
		ID_CIVILITY UUID NOT NULL DEFAULT RANDOM_UUID(),
		ABREVIATION VARCHAR(10),
		LIBELLE VARCHAR(64),
		MORALE BOOLEAN NOT NULL DEFAULT FALSE,
		PRIMARY KEY (ID_CIVILITY)
	);
INSERT INTO CIVILITY (ABREVIATION,LIBELLE,MORALE) VALUES ('M.', 'Monsieur', 0);
INSERT INTO CIVILITY (ABREVIATION,LIBELLE,MORALE) VALUES ('Mme', 'Madame', 0);
INSERT INTO CIVILITY (ABREVIATION,LIBELLE,MORALE) VALUES ('Mlle', 'Mademoiselle', 0);
INSERT INTO CIVILITY (ABREVIATION,LIBELLE,MORALE) VALUES ('Dr.', 'Docteur', 0);
INSERT INTO CIVILITY (ABREVIATION,LIBELLE,MORALE) VALUES ('Me', 'Maître', 0);

CREATE TABLE IF NOT EXISTS CONTACT (
		ID_CONTACT UUID NOT NULL DEFAULT RANDOM_UUID(),
		NAME VARCHAR(128),
		FIRSTNAME VARCHAR(128),
		ID_CIVILITY UUID,
		ADDRESS VARCHAR(255),
		ZIP_CODE VARCHAR(10),
		CITY VARCHAR(64),
		COUNTRY VARCHAR(2) DEFAULT 'fr',
		NOTE TEXT,
		ID_ENTITE UUID,
		UPPER_NAME VARCHAR(128) as UPPER(NAME),
		PRIMARY KEY (ID_CONTACT),
		FOREIGN KEY (ID_CIVILITY) REFERENCES CIVILITY (ID_CIVILITY) ON UPDATE CASCADE ON DELETE CASCADE,
		FOREIGN KEY (ID_ENTITE) REFERENCES ENTITE (ID_ENTITE) ON UPDATE CASCADE ON DELETE CASCADE
	);
CREATE INDEX IF NOT EXISTS I_NAME ON CONTACT (NAME ASC);
CREATE INDEX IF NOT EXISTS I_UPPER_NAME ON CONTACT (UPPER_NAME ASC);
CREATE INDEX IF NOT EXISTS I_FIRSTNAME ON CONTACT (FIRSTNAME ASC);

CREATE TABLE IF NOT EXISTS COORDINATE (
		ID_COORDINATE UUID NOT NULL DEFAULT RANDOM_UUID(),
		ID_CONTACT UUID NOT NULL,
		CODE_COORDINATE_TYPE VARCHAR(16),
		"VALUE" VARCHAR(64),
		PRIMARY KEY (ID_COORDINATE),
		FOREIGN KEY (ID_CONTACT) REFERENCES CONTACT (ID_CONTACT) ON UPDATE CASCADE ON DELETE CASCADE
	);
	
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

CREATE TABLE IF NOT EXISTS ASSOCIER_CATEGORIE_CONTACT (
		ID_CONTACT UUID NOT NULL,
		NUM_CATEGORIE_CONTACT INTEGER NOT NULL,
		PRIMARY KEY (ID_CONTACT, NUM_CATEGORIE_CONTACT),
		FOREIGN KEY (ID_CONTACT) REFERENCES CONTACT (ID_CONTACT) ON UPDATE CASCADE ON DELETE CASCADE,
		FOREIGN KEY (NUM_CATEGORIE_CONTACT) REFERENCES CATEGORIE_CONTACT (NUM_CATEGORIE_CONTACT) ON UPDATE CASCADE ON DELETE CASCADE
	);

CREATE TABLE IF NOT EXISTS ARCHERS (
		ID_CONTACT UUID NOT NULL,
		NUMLICENCEARCHER VARCHAR(32),
		CERTIFMEDICAL BOOLEAN,
		SEXE INTEGER,
		DATENAISS DATE,
		CATEGORIE INTEGER,
		NIVEAU INTEGER,
		ARC INTEGER,
		DATEMODIF DATE DEFAULT CURRENT_DATE(),
		PRIMARY KEY (ID_CONTACT),
		FOREIGN KEY (ID_CONTACT) REFERENCES CONTACT (ID_CONTACT) ON UPDATE CASCADE ON DELETE CASCADE
	);
CREATE INDEX IF NOT EXISTS I_NUMLICENCEARCHER ON ARCHERS (NUMLICENCEARCHER ASC);
	
CREATE TABLE IF NOT EXISTS DISTINGUER (
		ID_CONTACT UUID NOT NULL,
		NUMREGLEMENT INTEGER NOT NULL,
		NUMCRITERIASET INTEGER NOT NULL,
		PRIMARY KEY (ID_CONTACT, NUMREGLEMENT, NUMCRITERIASET),
		FOREIGN KEY (ID_CONTACT) REFERENCES ARCHERS (ID_CONTACT) ON UPDATE CASCADE ON DELETE CASCADE,
		FOREIGN KEY (NUMREGLEMENT) REFERENCES REGLEMENT (NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE,
		FOREIGN KEY (NUMCRITERIASET) REFERENCES CRITERIASET (NUMCRITERIASET) ON UPDATE CASCADE ON DELETE CASCADE
	);