-- @release 1

INSERT INTO PUBLIC.REGLEMENT(NUMREGLEMENT, NOMREGLEMENT, NBSERIE, NBVOLEEPARSERIE, NBFLECHEPARVOLEE, NBMEMBRESEQUIPE, NBMEMBRESRETENU, ISOFFICIAL) VALUES(-180676679, STRINGDECODE('Sp\u00E9cial Jeunes F\u00E9d\u00E9ral'), 2, 7, 3, 4, 3, FALSE);

INSERT INTO PUBLIC.CRITERE(CODECRITERE, NUMREGLEMENT, LIBELLECRITERE, SORTORDERCRITERE, CLASSEMENT, PLACEMENT, CODEFFTA) VALUES('genre', -180676679, 'Genre', -1, TRUE, FALSE, 'genre');
INSERT INTO PUBLIC.CRITERE(CODECRITERE, NUMREGLEMENT, LIBELLECRITERE, SORTORDERCRITERE, CLASSEMENT, PLACEMENT, CODEFFTA) VALUES('categorie', -180676679, 'Categorie', 1, FALSE, FALSE, 'categorie');
INSERT INTO PUBLIC.CRITERE(CODECRITERE, NUMREGLEMENT, LIBELLECRITERE, SORTORDERCRITERE, CLASSEMENT, PLACEMENT, CODEFFTA) VALUES('niveau', -180676679, 'Niveau', 1, TRUE, TRUE, 'niveau');
INSERT INTO PUBLIC.CRITERE(CODECRITERE, NUMREGLEMENT, LIBELLECRITERE, SORTORDERCRITERE, CLASSEMENT, PLACEMENT, CODEFFTA) VALUES('arc', -180676679, 'Arc', 1, FALSE, FALSE, 'arc');

INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('H', 'genre', -180676679, 'Homme', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('D', 'genre', -180676679, 'Dame', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('P', 'categorie', -180676679, 'Poussin', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('B', 'categorie', -180676679, 'Benjamin', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('M', 'categorie', -180676679, 'Minime', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('C', 'categorie', -180676679, 'Cadet', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('J', 'categorie', -180676679, 'Junior', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('S', 'categorie', -180676679, 'Senior', FALSE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('V', 'categorie', -180676679, STRINGDECODE('V\u00e9t\u00e9ran'), FALSE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('SV', 'categorie', -180676679, STRINGDECODE('Super-V\u00e9t\u00e9ran'), FALSE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('P', 'niveau', -180676679, 'Poussins', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('1', 'niveau', -180676679, '1', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('2', 'niveau', -180676679, '2', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('3', 'niveau', -180676679, '3', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('A', 'niveau', -180676679, 'As', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('CL', 'arc', -180676679, 'Classique', TRUE);
INSERT INTO PUBLIC.CRITEREELEMENT(CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT, LIBELLECRITEREELEMENT, ACTIF) VALUES('CO', 'arc', -180676679, STRINGDECODE('Arc \u00e0 poulies'), TRUE);

INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, BLASONS) VALUES(1, -180676679, 122);
INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, BLASONS) VALUES(2, -180676679, 122);
INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, BLASONS) VALUES(3, -180676679, 80);
INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, BLASONS) VALUES(4, -180676679, 60);
INSERT INTO PUBLIC.DISTANCESBLASONS(NUMDISTANCESBLASONS, NUMREGLEMENT, BLASONS) VALUES(5, -180676679, 40);

INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(1, 1, -180676679, 10);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(2, 1, -180676679, 10);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(3, 2, -180676679, 15);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(4, 2, -180676679, 15);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(5, 3, -180676679, 15);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(6, 3, -180676679, 15);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(7, 4, -180676679, 15);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(8, 4, -180676679, 15);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(9, 5, -180676679, 15);
INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(10, 5, -180676679, 15);

MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(-1045175507);
MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(-1045175410);
MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(-1045175413);
MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(-1045175412);
MERGE INTO PUBLIC.CRITERIASET(NUMCRITERIASET) VALUES(-1045175490);

INSERT INTO PUBLIC.POSSEDE(NUMCRITERIASET, CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT) VALUES(-1045175507, 'P', 'niveau', -180676679);
INSERT INTO PUBLIC.POSSEDE(NUMCRITERIASET, CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT) VALUES(-1045175410, '1', 'niveau', -180676679);
INSERT INTO PUBLIC.POSSEDE(NUMCRITERIASET, CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT) VALUES(-1045175413, '2', 'niveau', -180676679);
INSERT INTO PUBLIC.POSSEDE(NUMCRITERIASET, CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT) VALUES(-1045175412, '3', 'niveau', -180676679);
INSERT INTO PUBLIC.POSSEDE(NUMCRITERIASET, CODECRITEREELEMENT, CODECRITERE, NUMREGLEMENT) VALUES(-1045175490, 'A', 'niveau', -180676679);

INSERT INTO PUBLIC.ASSOCIER(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMCRITERIASET) VALUES(1, -180676679, -1045175507);
INSERT INTO PUBLIC.ASSOCIER(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMCRITERIASET) VALUES(2, -180676679, -1045175410);
INSERT INTO PUBLIC.ASSOCIER(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMCRITERIASET) VALUES(3, -180676679, -1045175412);
INSERT INTO PUBLIC.ASSOCIER(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMCRITERIASET) VALUES(4, -180676679, -1045175413);
INSERT INTO PUBLIC.ASSOCIER(NUMDISTANCESBLASONS, NUMREGLEMENT, NUMCRITERIASET) VALUES(5, -180676679, -1045175490);