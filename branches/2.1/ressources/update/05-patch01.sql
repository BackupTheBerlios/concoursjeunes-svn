-- @release 4
-- Corrige les défauts de base survenue sur les réglements

update PUBLIC.REGLEMENT set NOMREGLEMENT = STRINGDECODE('Sp\u00E9cial Jeunes F\u00E9d\u00E9ral'), NBVOLEEPARSERIE = 7 where NUMREGLEMENT = -180676679;
update PUBLIC.REGLEMENT set NOMREGLEMENT = 'Savoie', NUMREGLEMENT = -1825540830 where NUMREGLEMENT = -909407998;