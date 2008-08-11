-- @release 1
SET @NAME='2x50m, 2x12 volées';
SET @NUMREGLEMENT=-1891783300;

INSERT INTO PUBLIC.REGLEMENT(NUMREGLEMENT, NOMREGLEMENT, NBSERIE, NBVOLEEPARSERIE, NBFLECHEPARVOLEE, NBMEMBRESEQUIPE, NBMEMBRESRETENU, ISOFFICIAL, NUMFEDERATION, NUMCATEGORIE_REGLEMENT, REMOVABLE) VALUES(@NUMREGLEMENT, @NAME, 2, 12, 3, 4, 3, TRUE, 2, 3, FALSE);

INSERT INTO PUBLIC.CRITERE(CODECRITERE, NUMREGLEMENT, LIBELLECRITERE, SORTORDERCRITERE, CLASSEMENT, PLACEMENT, CODEFFTA, NUMORDRE) VALUES('genre', @NUMREGLEMENT, 'Genre', -1, TRUE, FALSE, 'genre', 1);
INSERT INTO PUBLIC.CRITERE(CODECRITERE, NUMREGLEMENT, LIBELLECRITERE, SORTORDERCRITERE, CLASSEMENT, PLACEMENT, CODEFFTA, NUMORDRE) VALUES('categorie', @NUMREGLEMENT, 'Categorie', 1, TRUE, TRUE, 'categorie', 2);
INSERT INTO PUBLIC.CRITERE(CODECRITERE, NUMREGLEMENT, LIBELLECRITERE, SORTORDERCRITERE, CLASSEMENT, PLACEMENT, CODEFFTA, NUMORDRE) VALUES('arc', @NUMREGLEMENT, 'Arc', 1, TRUE, FALSE, 'arc', 4);

INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('H', 'genre', @NUMREGLEMENT, 'Homme', TRUE, 1);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('D', 'genre', @NUMREGLEMENT, 'Dame', TRUE, 2);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('P', 'categorie', @NUMREGLEMENT, 'Poussin', FALSE, 1);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('B', 'categorie', @NUMREGLEMENT, 'Benjamin', TRUE, 2);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('M', 'categorie', @NUMREGLEMENT, 'Minime', TRUE, 3);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('C', 'categorie', @NUMREGLEMENT, 'Cadet', TRUE, 4);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('J', 'categorie', @NUMREGLEMENT, 'Junior', TRUE, 5);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('S', 'categorie', @NUMREGLEMENT, 'Senior', TRUE, 6);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('V', 'categorie', @NUMREGLEMENT, STRINGDECODE('V\u00e9t\u00e9ran'), TRUE, 7);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('SV', 'categorie', @NUMREGLEMENT, STRINGDECODE('Super-V\u00e9t\u00e9ran'), TRUE, 8);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('CL', 'arc', @NUMREGLEMENT, 'Classique', TRUE, 1);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('CO', 'arc', @NUMREGLEMENT, STRINGDECODE('Arc \u00e0 poulies'), TRUE, 2);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('AD', 'arc', @NUMREGLEMENT, 'Arc droit', FALSE, 3);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('AC', 'arc', @NUMREGLEMENT, 'Arc chasse', FALSE, 4);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('BB', 'arc', @NUMREGLEMENT, 'Bare Bow', FALSE, 5);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('TL', 'arc', @NUMREGLEMENT, 'Tir Libre', FALSE, 6);

INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMBLASON) VALUES(1, @NUMREGLEMENT, 1);
INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMBLASON) VALUES(2, @NUMREGLEMENT, 1);
INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMBLASON) VALUES(3, @NUMREGLEMENT, 1);
INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMBLASON) VALUES(4, @NUMREGLEMENT, 1);
INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMBLASON) VALUES(5, @NUMREGLEMENT, 1);
INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMBLASON) VALUES(6, @NUMREGLEMENT, 1);
INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMBLASON) VALUES(7, @NUMREGLEMENT, 1);

INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(1, 1, @NUMREGLEMENT, 20);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(2, 1, @NUMREGLEMENT, 20);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(3, 2, @NUMREGLEMENT, 30);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(4, 2, @NUMREGLEMENT, 30);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(5, 3, @NUMREGLEMENT, 50);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(6, 3, @NUMREGLEMENT, 50);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(7, 4, @NUMREGLEMENT, 50);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(8, 4, @NUMREGLEMENT, 50);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(9, 5, @NUMREGLEMENT, 50);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(10, 5, @NUMREGLEMENT, 50);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(11, 6, @NUMREGLEMENT, 50);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(12, 6, @NUMREGLEMENT, 50);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(13, 7, @NUMREGLEMENT, 50);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(14, 7, @NUMREGLEMENT, 50);     

MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(1537308382);
MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(1537308393);
MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(1537308383);
MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(1537308390);
MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(1537308399);
MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(1537308402);
MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(411919269);


INSERT INTO PUBLIC.POSSEDE(NUMCRITERIASET, CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT) VALUES
(1537308382, 'B', 'categorie', @NUMREGLEMENT),
(1537308393, 'M', 'categorie', @NUMREGLEMENT),
(1537308383, 'C', 'categorie', @NUMREGLEMENT),
(1537308390, 'J', 'categorie', @NUMREGLEMENT),
(1537308399, 'S', 'categorie', @NUMREGLEMENT),
(1537308402, 'V', 'categorie', @NUMREGLEMENT),
(411919269, 'SV', 'categorie', @NUMREGLEMENT);

INSERT INTO PUBLIC.ASSOCIER(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMCRITERIASET) VALUES
(1, @NUMREGLEMENT, 1537308382),
(2, @NUMREGLEMENT, 1537308393),
(3, @NUMREGLEMENT, 1537308383),
(4, @NUMREGLEMENT, 1537308390),
(5, @NUMREGLEMENT, 1537308399),
(6, @NUMREGLEMENT, 1537308402),
(7, @NUMREGLEMENT, 411919269);

-- @release 1
SET @NAME='2x50m, 2x6 volées';
SET @NUMREGLEMENT=2060995795;

INSERT INTO PUBLIC.REGLEMENT(NUMREGLEMENT, NOMREGLEMENT, NBSERIE, NBVOLEEPARSERIE, NBFLECHEPARVOLEE, NBMEMBRESEQUIPE, NBMEMBRESRETENU, ISOFFICIAL, NUMFEDERATION, NUMCATEGORIE_REGLEMENT) VALUES(@NUMREGLEMENT, @NAME, 2, 6, 6, 4, 3, TRUE, 2, 3);

INSERT INTO PUBLIC.CRITERE(CODECRITERE, NUMREGLEMENT, LIBELLECRITERE, SORTORDERCRITERE, CLASSEMENT, PLACEMENT, CODEFFTA, NUMORDRE) VALUES('genre', @NUMREGLEMENT, 'Genre', -1, TRUE, FALSE, 'genre', 1);
INSERT INTO PUBLIC.CRITERE(CODECRITERE, NUMREGLEMENT, LIBELLECRITERE, SORTORDERCRITERE, CLASSEMENT, PLACEMENT, CODEFFTA, NUMORDRE) VALUES('categorie', @NUMREGLEMENT, 'Categorie', 1, TRUE, TRUE, 'categorie', 2);
INSERT INTO PUBLIC.CRITERE(CODECRITERE, NUMREGLEMENT, LIBELLECRITERE, SORTORDERCRITERE, CLASSEMENT, PLACEMENT, CODEFFTA, NUMORDRE) VALUES('arc', @NUMREGLEMENT, 'Arc', 1, TRUE, FALSE, 'arc', 4);

INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('H', 'genre', @NUMREGLEMENT, 'Homme', TRUE, 1);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('D', 'genre', @NUMREGLEMENT, 'Dame', TRUE, 2);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('P', 'categorie', @NUMREGLEMENT, 'Poussin', FALSE, 1);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('B', 'categorie', @NUMREGLEMENT, 'Benjamin', TRUE, 2);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('M', 'categorie', @NUMREGLEMENT, 'Minime', TRUE, 3);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('C', 'categorie', @NUMREGLEMENT, 'Cadet', TRUE, 4);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('J', 'categorie', @NUMREGLEMENT, 'Junior', TRUE, 5);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('S', 'categorie', @NUMREGLEMENT, 'Senior', TRUE, 6);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('V', 'categorie', @NUMREGLEMENT, STRINGDECODE('V\u00e9t\u00e9ran'), TRUE, 7);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('SV', 'categorie', @NUMREGLEMENT, STRINGDECODE('Super-V\u00e9t\u00e9ran'), TRUE, 8);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('CL', 'arc', @NUMREGLEMENT, 'Classique', TRUE, 1);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('CO', 'arc', @NUMREGLEMENT, STRINGDECODE('Arc \u00e0 poulies'), TRUE, 2);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('AD', 'arc', @NUMREGLEMENT, 'Arc droit', FALSE, 3);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('AC', 'arc', @NUMREGLEMENT, 'Arc chasse', FALSE, 4);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('BB', 'arc', @NUMREGLEMENT, 'Bare Bow', FALSE, 5);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF, NUMORDRE) VALUES('TL', 'arc', @NUMREGLEMENT, 'Tir Libre', FALSE, 6);

INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMBLASON) VALUES(1, @NUMREGLEMENT, 1);
INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMBLASON) VALUES(2, @NUMREGLEMENT, 1);
INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMBLASON) VALUES(3, @NUMREGLEMENT, 1);
INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMBLASON) VALUES(4, @NUMREGLEMENT, 1);
INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMBLASON) VALUES(5, @NUMREGLEMENT, 1);
INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMBLASON) VALUES(6, @NUMREGLEMENT, 1);
INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMBLASON) VALUES(7, @NUMREGLEMENT, 1);

INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(1, 1, @NUMREGLEMENT, 20);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(2, 1, @NUMREGLEMENT, 20);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(3, 2, @NUMREGLEMENT, 30);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(4, 2, @NUMREGLEMENT, 30);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(5, 3, @NUMREGLEMENT, 50);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(6, 3, @NUMREGLEMENT, 50);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(7, 4, @NUMREGLEMENT, 50);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(8, 4, @NUMREGLEMENT, 50);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(9, 5, @NUMREGLEMENT, 50);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(10, 5, @NUMREGLEMENT, 50);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(11, 6, @NUMREGLEMENT, 50);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(12, 6, @NUMREGLEMENT, 50);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(13, 7, @NUMREGLEMENT, 50);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(14, 7, @NUMREGLEMENT, 50);     

MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(1537308382);
MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(1537308393);
MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(1537308383);
MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(1537308390);
MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(1537308399);
MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(1537308402);
MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(411919269);

INSERT INTO PUBLIC.POSSEDE(NUMCRITERIASET, CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT) VALUES
(1537308382, 'B', 'categorie', @NUMREGLEMENT),
(1537308393, 'M', 'categorie', @NUMREGLEMENT),
(1537308383, 'C', 'categorie', @NUMREGLEMENT),
(1537308390, 'J', 'categorie', @NUMREGLEMENT),
(1537308399, 'S', 'categorie', @NUMREGLEMENT),
(1537308402, 'V', 'categorie', @NUMREGLEMENT),
(411919269, 'SV', 'categorie', @NUMREGLEMENT);

INSERT INTO PUBLIC.ASSOCIER(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMCRITERIASET) VALUES
(1, @NUMREGLEMENT, 1537308382),
(2, @NUMREGLEMENT, 1537308393),
(3, @NUMREGLEMENT, 1537308383),
(4, @NUMREGLEMENT, 1537308390),
(5, @NUMREGLEMENT, 1537308399),
(6, @NUMREGLEMENT, 1537308402),
(7, @NUMREGLEMENT, 411919269);