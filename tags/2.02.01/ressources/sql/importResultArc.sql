-- @author Aurélien JEOFFRAY
-- Optimise les performances sur les machines multi processeur/core ou Hyper-Threading
SET MULTI_THREADED 1;
SET LOG 0;

-- Integration des fichiers ASCII
TRUNCATE TABLE EntiteFFTA;
INSERT INTO EntiteFFTA SELECT distinct NUM,AGR,NOM,
	VILLE,LIGUE,CD from CSVREAD('{temp}/FICLUB.TXT','NUM;AGR;NOM;VILLE;LIGUE;CD;INC1;INC2;INC3;INC4',null,';');

TRUNCATE TABLE ArchersFFTA;
INSERT INTO ArchersFFTA (NOMARCHER,NUMLICENCEARCHER,PRENOMARCHER,
	ALIASARCHER,CLUBARCHER,ARCARCHER,CATEGORIEARCHER,GENREARCHER,NIVEAUARCHER,
	LIBELLENIVEAU,LIBELLECATEGORIE,AGREMENTCLUBARCHER,DATENAISS,CERTIFMEDICAL,
	INC2) SELECT distinct * from CSVREAD('{temp}/Licence.TXT','NOMARCHER;NUMLICENCEARCHER;PRENOMARCHER;ALIASARCHER;CLUBARCHER;ARCARCHER;CATEGORIEARCHER;GENREARCHER;NIVEAUARCHER;LIBELLENIVEAU;LIBELLECATEGORIE;AGREMENTCLUBARCHER;DATENAISS;CERTIFMEDICAL;INC2',null,';');

-- Placement/Mise à jour des données dans leurs table définitive
MERGE INTO Entite (AGREMENTENTITE, NOMENTITE, VILLEENTITE, TYPEENTITE) 
	SELECT AGREMENTENTITE, NOMENTITE, VILLEENTITE, 3 FROM EntiteFFTA
		WHERE NomEntite <> 'DEPARTEMENT FEDERATION';

MERGE INTO Archers (NUMLICENCEARCHER, NOMARCHER, PRENOMARCHER, 
	CERTIFMEDICAL, AGREMENTENTITE, GENREFFTA, CATEGORIEFFTA, NIVEAUFFTA,
	ARCFFTA) SELECT NUMLICENCEARCHER, NOMARCHER, PRENOMARCHER, 
		CERTIFMEDICAL, AGREMENTCLUBARCHER, GENREARCHER-1, 
		CATEGORIEARCHER-1,NIVEAUARCHER, ARCARCHER-1 FROM ArchersFFTA;

-- Nettoyage des tables
DELETE FROM Entite WHERE AGREMENTENTITE IN (SELECT Entite.AGREMENTENTITE FROM Entite LEFT JOIN EntiteFFTA ON Entite.AGREMENTENTITE=EntiteFFTA.AGREMENTENTITE WHERE EntiteFFTA.AGREMENTENTITE IS NULL);
DELETE FROM Archers WHERE NUMLICENCEARCHER IN (SELECT Archers.NUMLICENCEARCHER FROM Archers LEFT JOIN ArchersFFTA ON Archers.NUMLICENCEARCHER=ArchersFFTA.NUMLICENCEARCHER WHERE ArchersFFTA.NUMLICENCEARCHER IS NULL);

-- Vidage des tables temporaires
TRUNCATE TABLE EntiteFFTA;
TRUNCATE TABLE ArchersFFTA;

UPDATE ENTITE SET TYPEENTITE = 2 WHERE AGREMENTENTITE like '%000';
UPDATE ENTITE SET TYPEENTITE = 1 WHERE AGREMENTENTITE like '%00000';
UPDATE ENTITE SET TYPEENTITE = 0 WHERE AGREMENTENTITE like '0000000';

COMMIT;