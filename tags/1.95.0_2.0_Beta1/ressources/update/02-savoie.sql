-- @release 1

INSERT INTO PUBLIC.REGLEMENT(NUMREGLEMENT, NOMREGLEMENT, NBSERIE, NBVOLEEPARSERIE, NBFLECHEPARVOLEE, NBMEMBRESEQUIPE, NBMEMBRESRETENU, ISOFFICIAL) VALUES(-909407998, 'savoie', 2, 6, 3, 4, 3, FALSE);

INSERT INTO PUBLIC.CRITERE(CODECRITERE, NUMREGLEMENT, LIBELLECRITERE, SORTORDERCRITERE, CLASSEMENT, PLACEMENT, CODEFFTA) VALUES('genre', -909407998, 'Genre', -1, TRUE, FALSE, 'genre');
INSERT INTO PUBLIC.CRITERE(CODECRITERE, NUMREGLEMENT, LIBELLECRITERE, SORTORDERCRITERE, CLASSEMENT, PLACEMENT, CODEFFTA) VALUES('categorie', -909407998, 'Categorie', 1, FALSE, FALSE, 'categorie');
INSERT INTO PUBLIC.CRITERE(CODECRITERE, NUMREGLEMENT, LIBELLECRITERE, SORTORDERCRITERE, CLASSEMENT, PLACEMENT, CODEFFTA) VALUES('niveau', -909407998, 'Niveau', 1, TRUE, TRUE, 'niveau');
INSERT INTO PUBLIC.CRITERE(CODECRITERE, NUMREGLEMENT, LIBELLECRITERE, SORTORDERCRITERE, CLASSEMENT, PLACEMENT, CODEFFTA) VALUES('arc', -909407998, 'Arc', 1, FALSE, FALSE, 'arc');

INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('H', 'genre', -909407998, 'Homme', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('D', 'genre', -909407998, 'Dame', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('P', 'categorie', -909407998, 'Poussin', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('B', 'categorie', -909407998, 'Benjamin', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('M', 'categorie', -909407998, 'Minime', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('C', 'categorie', -909407998, 'Cadet', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('J', 'categorie', -909407998, 'Junior', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('S', 'categorie', -909407998, 'Senior', FALSE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('V', 'categorie', -909407998, STRINGDECODE('V\u00e9t\u00e9ran'), FALSE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('SV', 'categorie', -909407998, STRINGDECODE('Super-V\u00e9t\u00e9ran'), FALSE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('0', 'niveau', -909407998, 'Niveau 0', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('1', 'niveau', -909407998, 'Niveau 1', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('2', 'niveau', -909407998, 'Niveau 2', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('3', 'niveau', -909407998, 'Niveau 3', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('CL', 'arc', -909407998, 'Classique', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('CO', 'arc', -909407998, STRINGDECODE('Arc \u00e0 poulies'), TRUE);

INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, BLASONS) VALUES(1, -909407998, 80);
INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, BLASONS) VALUES(2, -909407998, 60);
INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, BLASONS) VALUES(3, -909407998, 80);
INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, BLASONS) VALUES(4, -909407998, 80);

INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(1, 1, -909407998, 15);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(2, 1, -909407998, 15);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(3, 2, -909407998, 18);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(4, 2, -909407998, 18);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(5, 3, -909407998, 18);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(6, 3, -909407998, 18);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(7, 4, -909407998, 10);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(8, 4, -909407998, 10);

MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(-1045175411);
MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(-1045175410);
MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(-1045175413);
MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(-1045175412);

INSERT INTO PUBLIC.POSSEDE(NUMCRITERIASET, CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT) VALUES(-1045175410, '1', 'niveau', -909407998);
INSERT INTO PUBLIC.POSSEDE(NUMCRITERIASET, CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT) VALUES(-1045175412, '3', 'niveau', -909407998);
INSERT INTO PUBLIC.POSSEDE(NUMCRITERIASET, CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT) VALUES(-1045175413, '2', 'niveau', -909407998);
INSERT INTO PUBLIC.POSSEDE(NUMCRITERIASET, CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT) VALUES(-1045175411, '0', 'niveau', -909407998);

INSERT INTO PUBLIC.ASSOCIER(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMCRITERIASET) VALUES(1, -909407998, -1045175410);
INSERT INTO PUBLIC.ASSOCIER(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMCRITERIASET) VALUES(2, -909407998, -1045175412);
INSERT INTO PUBLIC.ASSOCIER(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMCRITERIASET) VALUES(3, -909407998, -1045175413);
INSERT INTO PUBLIC.ASSOCIER(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMCRITERIASET) VALUES(4, -909407998, -1045175411);