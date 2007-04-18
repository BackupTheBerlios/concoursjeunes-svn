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
		PLACEMENT BOOLEAN,
		CODEFFTA VARCHAR(16),
		PRIMARY KEY (CODECRITERE, NUMREGLEMENT),
		FOREIGN KEY (NUMREGLEMENT) REFERENCES REGLEMENT (NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE
	);

CREATE TABLE CRITEREELEMENT (
		CODECRITEREELEMENT VARCHAR(16) NOT NULL,
		CODECRITERE VARCHAR(16) NOT NULL,
		NUMREGLEMENT INTEGER NOT NULL,
		LIBELLECRITEREELEMENT VARCHAR(32),
		ACTIF BOOLEAN,
		PRIMARY KEY (CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT),
		FOREIGN KEY (CODECRITERE, NUMREGLEMENT) REFERENCES CRITERE (CODECRITERE, NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE
	);
	
CREATE TABLE CRITEREELEMENT (
		CODECRITEREELEMENT VARCHAR(16) NOT NULL,
		CODECRITERE VARCHAR(16) NOT NULL,
		NUMREGLEMENT INTEGER NOT NULL,
		LIBELLECRITEREELEMENT VARCHAR(32),
		ACTIF BOOLEAN,
		PRIMARY KEY (CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT),
		FOREIGN KEY (CODECRITERE, NUMREGLEMENT) REFERENCES CRITERE (CODECRITERE, NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE
	);

CREATE TABLE CRITERIASET (
		NUMDISTANCESBLASONS INTEGER NOT NULL,
		NUMREGLEMENT1 INTEGER NOT NULL,
		CODECRITEREELEMENT VARCHAR(16) NOT NULL,
		CODECRITERE VARCHAR(16) NOT NULL,
		NUMREGLEMENT2 INTEGER NOT NULL,
		PRIMARY KEY (NUMCRITERIASET, CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT),
		FOREIGN KEY (NUMDISTANCESBLASONS, NUMREGLEMENT1) REFERENCES DISTANCESBLASONS (NUMDISTANCESBLASONS, NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE,
		FOREIGN KEY (CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT2) REFERENCES CRITEREELEMENT (CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE
	);

CREATE TABLE DISTANCESBLASONS (
		NUMDISTANCESBLASONS INTEGER NOT NULL,
		NUMREGLEMENT INTEGER NOT NULL,
		BLASONS INTEGER,
		PRIMARY KEY (NUMDISTANCESBLASONS, NUMREGLEMENT),
		FOREIGN KEY (NUMREGLEMENT) REFERENCES REGLEMENT (NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE
	);

CREATE TABLE DISTANCES (
		NUMDISTANCES INTEGER NOT NULL,
		NUMDISTANCESBLASONS INTEGER,
		NUMREGLEMENT INTEGER NOT NULL,
		DISTANCE INTEGER,
		PRIMARY KEY (NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT),
		FOREIGN KEY (NUMDISTANCESBLASONS, NUMREGLEMENT) REFERENCES DISTANCESBLASONS (NUMDISTANCESBLASONS, NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE
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
		CERTIFMEDICAL BIGINT,
		AGREMENTENTITE VARCHAR(7),
		GENREFFTA INTEGER,
		CATEGORIEFFTA INTEGER,
		NIVEAUFFTA INTEGER,
		ARCFFTA INTEGER,
		PRIMARY KEY (NUMLICENCEARCHER)
	);

CREATE TABLE DISTINGUER (
		NUMLICENCEARCHER VARCHAR(7) NOT NULL,
		CODECRITEREELEMENT VARCHAR(16) NOT NULL,
		CODECRITERE VARCHAR(16) NOT NULL,
		NUMREGLEMENT INTEGER NOT NULL,
		PRIMARY KEY (NUMLICENCEARCHER, CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT),
		FOREIGN KEY (NUMLICENCEARCHER) REFERENCES ARCHERS (NUMLICENCEARCHER) ON UPDATE CASCADE ON DELETE CASCADE,
		FOREIGN KEY (CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT) REFERENCES CRITEREELEMENT (CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT) ON UPDATE CASCADE ON DELETE CASCADE
	);

CREATE INDEX I_NOM_ARCHERS ON ARCHERS (NOMARCHER ASC);