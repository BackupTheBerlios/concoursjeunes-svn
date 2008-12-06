-- Corrige les défauts de base survenue sur les réglements

update PUBLIC.REGLEMENT set NOMREGLEMENT = 'Savoie', NUMREGLEMENT = -1825540830 where NUMREGLEMENT = -909407998;