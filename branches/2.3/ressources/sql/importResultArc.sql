-- @author Aurélien JEOFFRAY
-- Optimise les performances sur les machines multi processeur/core ou Hyper-Threading
--SET MULTI_THREADED 1;
SET AUTOCOMMIT OFF;
SET LOG 0;

SET @DATE_MODIF = CURRENT_DATE();
CREATE SCHEMA IF NOT EXISTS IMPORTFFTA;

-- Integration des fichiers ASCII
CREATE TEMPORARY TABLE IMPORTFFTA.EntiteFFTA AS SELECT RANDOM_UUID() as ID_ENTITE,NUM,AGR,NOM,
	VILLE,LIGUE,CD,CODEPOSTAL from CSVREAD('{temp}/FICLUB.TXT','NUM;AGR;NOM;VILLE;LIGUE;CD;INC1;INC2;INC3;CODEPOSTAL',null,';');
CREATE INDEX IF NOT EXISTS IT_EntiteFFTA_AGREMENT_ENTITE ON IMPORTFFTA.EntiteFFTA (AGR ASC);
UPDATE IMPORTFFTA.EntiteFFTA SET ID_ENTITE = IFNULL((SELECT distinct ID_ENTITE FROM ENTITE WHERE ENTITE.AGREMENTENTITE=IMPORTFFTA.EntiteFFTA.AGR AND ENTITE.NUMFEDERATION=2), ID_ENTITE);
MERGE INTO Entite (ID_ENTITE, AGREMENTENTITE, NUMFEDERATION, NOMENTITE, VILLEENTITE, TYPEENTITE, CODEPOSTALENTITE, DATEMODIF) 
    SELECT distinct ID_ENTITE, AGR,2,NOM,VILLE,3, CODEPOSTAL, @DATE_MODIF from IMPORTFFTA.EntiteFFTA;
COMMIT;

CREATE TEMPORARY TABLE IMPORTFFTA.ArchersFFTA AS SELECT distinct RANDOM_UUID() AS ID_CONTACT,* from CSVREAD('{temp}/Licence.TXT','NOMARCHER;NUMLICENCEARCHER;PRENOMARCHER;ALIASARCHER;CLUBARCHER;ARCARCHER;CATEGORIEARCHER;GENREARCHER;NIVEAUARCHER;LIBELLENIVEAU;LIBELLECATEGORIE;AGREMENTCLUBARCHER;DATENAISS;CERTIFMEDICAL;INC1;INC2',null,';');
CREATE INDEX IF NOT EXISTS IT_ArchersFFTA_NUMLICENCEARCHER ON IMPORTFFTA.ArchersFFTA (NUMLICENCEARCHER ASC);
UPDATE IMPORTFFTA.ArchersFFTA L SET L.ID_CONTACT=IFNULL((SELECT A.ID_CONTACT FROM ARCHERS A WHERE A.NUMLICENCEARCHER=L.NUMLICENCEARCHER),L.ID_CONTACT);
MERGE INTO CONTACT (ID_CONTACT, NAME, FIRSTNAME, ID_ENTITE)
	SELECT ID_CONTACT, NOMARCHER, PRENOMARCHER, ID_ENTITE FROM IMPORTFFTA.ArchersFFTA INNER JOIN ENTITE ON AGREMENTCLUBARCHER=AGREMENTENTITE;
MERGE INTO ARCHERS (ID_CONTACT,NUMLICENCEARCHER, 
    CERTIFMEDICAL, SEXE, DATENAISS, CATEGORIE, NIVEAU,
    ARC, DATEMODIF) SELECT ID_CONTACT,NUMLICENCEARCHER, 
        CERTIFMEDICAL, GENREARCHER, PARSEDATETIME(DATENAISS,'dd/MM/yy'),
        CATEGORIEARCHER, NIVEAUARCHER, ARCARCHER-1, @DATE_MODIF FROM IMPORTFFTA.ArchersFFTA;
MERGE INTO ASSOCIER_CATEGORIE_CONTACT (ID_CONTACT, NUM_CATEGORIE_CONTACT) SELECT ID_CONTACT, 1 FROM ARCHERS;    
COMMIT;

UPDATE ENTITE SET TYPEENTITE = 2 WHERE AGREMENTENTITE like '%000';
UPDATE ENTITE SET TYPEENTITE = 1 WHERE AGREMENTENTITE like '%00000';
UPDATE ENTITE SET TYPEENTITE = 0 WHERE AGREMENTENTITE like '0000000';

DROP SCHEMA IMPORTFFTA;

COMMIT;

SET LOG 1;
SET AUTOCOMMIT ON;