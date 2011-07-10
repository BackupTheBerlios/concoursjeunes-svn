-- @author Aurélien JEOFFRAY
-- Optimise les performances sur les machines multi processeur/core ou Hyper-Threading
--SET MULTI_THREADED 1;
SET LOG 0;

SET @DATE_MODIF = CURRENT_DATE();

-- Integration des fichiers ASCII
TRUNCATE TABLE EntiteFFTA;
INSERT INTO EntiteFFTA SELECT distinct NUM,AGR,NOM,
	VILLE,LIGUE,CD from CSVREAD('{temp}/FICLUB.TXT','NUM;AGR;NOM;VILLE;LIGUE;CD;INC1;INC2;INC3;INC4',null,';');

TRUNCATE TABLE ArchersFFTA;
INSERT INTO ArchersFFTA (NOMARCHER,NUMLICENCEARCHER,PRENOMARCHER,
	ALIASARCHER,CLUBARCHER,ARCARCHER,CATEGORIEARCHER,GENREARCHER,NIVEAUARCHER,
	LIBELLENIVEAU,LIBELLECATEGORIE,AGREMENTCLUBARCHER,DATENAISS,CERTIFMEDICAL,
	INC2) SELECT distinct * from CSVREAD('{temp}/Licence.TXT','NOMARCHER;NUMLICENCEARCHER;PRENOMARCHER;ALIASARCHER;CLUBARCHER;ARCARCHER;CATEGORIEARCHER;GENREARCHER;NIVEAUARCHER;LIBELLENIVEAU;LIBELLECATEGORIE;AGREMENTCLUBARCHER;DATENAISS;CERTIFMEDICAL;INC2',null,';');

COMMIT;

-- Placement/Mise à jour des données dans leurs table définitive
MERGE INTO Entite (AGREMENTENTITE, NUMFEDERATION, NOMENTITE, VILLEENTITE, TYPEENTITE, DATEMODIF) 
	SELECT AGREMENTENTITE, 2, NOMENTITE, VILLEENTITE, 3, @DATE_MODIF FROM EntiteFFTA
		WHERE NomEntite <> 'DEPARTEMENT FEDERATION';

MERGE INTO Archers (NUMLICENCEARCHER, NOMARCHER, PRENOMARCHER, 
	CERTIFMEDICAL, AGREMENTENTITE, SEXE, DATENAISS, CATEGORIE, NIVEAU,
	ARC, DATEMODIF) SELECT NUMLICENCEARCHER, NOMARCHER, PRENOMARCHER, 
		CERTIFMEDICAL, AGREMENTCLUBARCHER, GENREARCHER-1, PARSEDATETIME(DATENAISS,'yyyyMMdd'),
		CATEGORIEARCHER-1,NIVEAUARCHER, ARCARCHER-1,@DATE_MODIF FROM ArchersFFTA;

COMMIT;

-- Nettoyage des tables
DELETE FROM Entite WHERE DATEMODIF <> @DATE_MODIF;
DELETE FROM Archers WHERE DATEMODIF <> @DATE_MODIF;

-- Vidage des tables temporaires
TRUNCATE TABLE EntiteFFTA;
TRUNCATE TABLE ArchersFFTA;

UPDATE ENTITE SET TYPEENTITE = 2 WHERE AGREMENTENTITE like '%000';
UPDATE ENTITE SET TYPEENTITE = 1 WHERE AGREMENTENTITE like '%00000';
UPDATE ENTITE SET TYPEENTITE = 0 WHERE AGREMENTENTITE like '0000000';

COMMIT;

SET LOG 1;