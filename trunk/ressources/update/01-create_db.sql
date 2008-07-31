-- Création de la base de données de ConcoursJeunes
-- @author Aurélien JEOFFRAY
-- @release 1
CREATE TABLE PARAM (
		DBVERSION INTEGER NOT NULL,
		APPREVISION INTEGER NOT NULL
	);
INSERT INTO PARAM (DBVERSION, APPREVISION) VALUES (1, 1);

CREATE TABLE REGLEMENT (
		NUMREGLEMENT INTEGER AUTO_INCREMENT NOT NULL,
		NOMREGLEMENT VARCHAR(32),
		NBSERIE INTEGER,
		NBVOLEEPARSERIE INTEGER,
		NBFLECHEPARVOLEE INTEGER,
		NBMEMBRESEQUIPE INTEGER,
		NBMEMBRESRETENU INTEGER,
		ISOFFICIAL BOOLEAN,
		PRIMARY KEY (NUMREGLEMENT)
	);
CREATE INDEX I_NOM_REGLEMENT ON REGLEMENT (NOMREGLEMENT ASC);
	
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

CREATE TABLE DISTANCESBLASONS (
		NUMDISTANCESBLASONS INTEGER AUTO_INCREMENT NOT NULL,
		NUMREGLEMENT INTEGER NOT NULL,
		BLASONS INTEGER,
		NUMBLASON INTEGER NOT NULL,
		PRIMARY KEY (NUMDISTANCESBLASONS, NUMREGLEMENT),
		FOREIGN KEY (NUMREGLEMENT) REFERENCES REGLEMENT (NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE,
		FOREIGN KEY (NUMBLASON) REFERENCES BLASONS (NUMBLASON) ON UPDATE CASCADE ON DELETE CASCADE
	);

CREATE TABLE DISTANCES (
		NUMDISTANCES INTEGER AUTO_INCREMENT NOT NULL,
		NUMDISTANCESBLASONS INTEGER NOT NULL,
		NUMREGLEMENT INTEGER NOT NULL,
		DISTANCE INTEGER,
		PRIMARY KEY (NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT),
		FOREIGN KEY (NUMDISTANCESBLASONS, NUMREGLEMENT) REFERENCES DISTANCESBLASONS (NUMDISTANCESBLASONS, NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE
	);

CREATE TABLE CRITERIASET (
		NUMCRITERIASET INTEGER NOT NULL,
		PRIMARY KEY (NUMCRITERIASET)
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

CREATE TABLE ARCHERSFFTA (
		NUMLICENCEARCHER VARCHAR(64) NOT NULL,
		NOMARCHER VARCHAR(64),
		PRENOMARCHER VARCHAR(64),
		ALIASARCHER VARCHAR(128),
		CLUBARCHER VARCHAR(64),
		ARCARCHER INTEGER,
		CATEGORIEARCHER INTEGER,
		GENREARCHER INTEGER,
		NIVEAUARCHER CHAR(1),
		LIBELLENIVEAU VARCHAR(1),
		LIBELLECATEGORIE VARCHAR(8),
		AGREMENTCLUBARCHER VARCHAR(7),
		DATENAISS VARCHAR(8),
		CERTIFMEDICAL BOOLEAN,
		INC2 INTEGER,
		PRIMARY KEY (NUMLICENCEARCHER)
	);

CREATE TABLE ENTITEFFTA (
		NUMENTITE INTEGER NOT NULL,
		AGREMENTENTITE VARCHAR(7),
		NOMENTITE VARCHAR(64),
		VILLEENTITE VARCHAR(64),
		LIGUEENTITE VARCHAR(2),
		CDENTITE VARCHAR(9),
		PRIMARY KEY (NUMENTITE)
	);

CREATE TABLE ARCHERS (
		NUMLICENCEARCHER VARCHAR(7) NOT NULL,
		NOMARCHER VARCHAR(64),
		PRENOMARCHER VARCHAR(64),
		CERTIFMEDICAL BOOLEAN,
		AGREMENTENTITE VARCHAR(7),
		GENREFFTA INTEGER,
		CATEGORIEFFTA INTEGER,
		NIVEAUFFTA INTEGER,
		ARCFFTA INTEGER,
		PRIMARY KEY (NUMLICENCEARCHER)
	);

CREATE INDEX I_NOM_ARCHERS ON ARCHERS (NOMARCHER ASC);
CREATE INDEX I_PRENOM_ARCHERS ON ARCHERS (PRENOMARCHER ASC);

CREATE TABLE ENTITE (
		AGREMENTENTITE VARCHAR(7) NOT NULL,
		NOMENTITE VARCHAR(64),
		ADRESSEENTITE CLOB,
		CODEPOSTALENTITE VARCHAR(5),
		VILLEENTITE VARCHAR(64),
		NOTEENTITE CLOB,
		TYPEENTITE INTEGER,
		PRIMARY KEY (AGREMENTENTITE)
	);
CREATE INDEX I_NOM_ENTITE ON ENTITE (NOMENTITE ASC);
CREATE INDEX I_VILLE_ENTITE ON ENTITE (VILLEENTITE ASC);

CREATE TABLE ASSOCIER (
		NUMDISTANCESBLASONS INTEGER NOT NULL,
		NUMREGLEMENT INTEGER NOT NULL,
		NUMCRITERIASET INTEGER NOT NULL,
		PRIMARY KEY (NUMDISTANCESBLASONS, NUMREGLEMENT, NUMCRITERIASET),
		FOREIGN KEY (NUMDISTANCESBLASONS, NUMREGLEMENT) REFERENCES DISTANCESBLASONS (NUMDISTANCESBLASONS, NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE,
		FOREIGN KEY (NUMCRITERIASET) REFERENCES CRITERIASET (NUMCRITERIASET) ON UPDATE CASCADE ON DELETE CASCADE
	);
	
CREATE TABLE DISTINGUER (
		NUMLICENCEARCHER VARCHAR(7) NOT NULL,
		NUMREGLEMENT INTEGER NOT NULL,
		NUMCRITERIASET INTEGER NOT NULL,
		PRIMARY KEY (NUMLICENCEARCHER, NUMREGLEMENT, NUMCRITERIASET),
		FOREIGN KEY (NUMLICENCEARCHER) REFERENCES ARCHERS (NUMLICENCEARCHER) ON UPDATE CASCADE ON DELETE CASCADE,
		FOREIGN KEY (NUMREGLEMENT) REFERENCES REGLEMENT (NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE,
		FOREIGN KEY (NUMCRITERIASET) REFERENCES CRITERIASET (NUMCRITERIASET) ON UPDATE CASCADE ON DELETE CASCADE
	);
