-- @release 4

update PUBLIC.REGLEMENT set NOMREGLEMENT = STRINGDECODE('Sp\u00E9cial Jeunes F\u00E9d\u00E9ral'), NBVOLEEPARSERIE = 7 where NUMREGLEMENT = -180676679;
update PUBLIC.REGLEMENT set NOMREGLEMENT = 'Savoie', NUMREGLEMENT = -1825540830 where NUMREGLEMENT = -909407998;

--INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(1, 1, -1825540830, 15);
--INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(2, 1, -1825540830, 15);
--INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(4, 2, -1825540830, 18);
--INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(5, 3, -1825540830, 18);
--INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(6, 3, -1825540830, 18);
--INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(7, 4, -1825540830, 10);
--INSERT INTO PUBLIC.DISTANCES(NUMDISTANCES, NUMDISTANCESBLASONS, NUMREGLEMENT, DISTANCE) VALUES(8, 4, -1825540830, 10);