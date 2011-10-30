-- @author Aurélien JEOFFRAY
-- Optimise les performances sur les machines multi processeur/core ou Hyper-Threading
--SET MULTI_THREADED 1;
SET AUTOCOMMIT OFF;
SET LOG 0;

SET @DATE_MODIF = CURRENT_DATE();
CREATE SCHEMA IF NOT EXISTS IMPORTFFTA;

-- Integration des fichiers ASCII
CREATE TEMPORARY TABLE IMPORTFFTA.RESULT_CLUB AS SELECT  RANDOM_UUID() as ID_ENTITE, * from (select distinct * FROM CSVREAD('{temp}/result_club.txt',null,'ISO-8859-15',STRINGDECODE('\t')));
CREATE INDEX IF NOT EXISTS IT_RESULT_CLUB_AGREMENT_ENTITE ON IMPORTFFTA.RESULT_CLUB (AGRÉMENT ASC);
UPDATE IMPORTFFTA.RESULT_CLUB SET ID_ENTITE = IFNULL((SELECT distinct ID_ENTITE FROM ENTITE WHERE ENTITE.AGREMENTENTITE=IMPORTFFTA.RESULT_CLUB.AGRÉMENT AND ENTITE.NUMFEDERATION=2), ID_ENTITE);
MERGE INTO Entite (ID_ENTITE, AGREMENTENTITE, NUMFEDERATION, NOMENTITE, VILLEENTITE, TYPEENTITE, CODEPOSTALENTITE, DATEMODIF) 
    SELECT distinct ID_ENTITE, "AGRÉMENT",2,"DÉSIGNATION","LOCALITÉ",3, "Code postal", @DATE_MODIF
        from IMPORTFFTA.RESULT_CLUB
        where "DÉSIGNATION" <> 'DEPARTEMENT FEDERATION';
COMMIT;

CREATE TEMPORARY TABLE IMPORTFFTA.RESULT_LICENCE AS SELECT RANDOM_UUID() AS ID_CONTACT,* FROM CSVREAD('{temp}/result_licence.txt',null,'ISO-8859-15',STRINGDECODE('\t'));
CREATE TEMPORARY TABLE IMPORTFFTA.FFTACATEGORIE (NUM INTEGER PRIMARY KEY, CODE VARCHAR(2));
INSERT INTO IMPORTFFTA.FFTACATEGORIE VALUES (0,'P'), (1,'B'), (2,'M'), (3,'C'), (4,'J'), (5,'S'), (6,'V'), (7,'SV');
CREATE TEMPORARY TABLE IMPORTFFTA.FFTAARC (NUM INTEGER PRIMARY KEY, CODE VARCHAR(2));
INSERT INTO IMPORTFFTA.FFTAARC VALUES (0,'CL'), (1,'CO'), (2,'AD'), (3,'AC'), (4,'BB'), (5,'TL');
CREATE TEMPORARY TABLE IMPORTFFTA.FFTANIVEAU (NUM INTEGER PRIMARY KEY, CODE VARCHAR(2));
INSERT INTO IMPORTFFTA.FFTANIVEAU VALUES (0,'0'), (1,'1'), (2,'2'), (3,'3'), (4,'P'), (5,'Z');
COMMIT;

UPDATE IMPORTFFTA.RESULT_LICENCE L SET L.ID_CONTACT=IFNULL((SELECT A.ID_CONTACT FROM ARCHERS A WHERE A.NUMLICENCEARCHER=L."N° Adh"),L.ID_CONTACT);
MERGE INTO CONTACT (ID_CONTACT, NAME, FIRSTNAME, ID_ENTITE)
	SELECT ID_CONTACT, "NOM", "PRÉNOM", ID_ENTITE FROM IMPORTFFTA.RESULT_LICENCE INNER JOIN ENTITE ON "AGRÉMENT"=AGREMENTENTITE;
MERGE INTO ARCHERS (ID_CONTACT,NUMLICENCEARCHER, 
    CERTIFMEDICAL, SEXE, DATENAISS, CATEGORIE, NIVEAU,
    ARC, DATEMODIF) SELECT ID_CONTACT,"N° Adh", 
        CASEWHEN("CERTIFICAT" is not NULL, TRUE, FALSE), CASEWHEN("SEXE"='H', 0, 1), 
        PARSEDATETIME("NAISSANCE",'dd/MM/yyyy'),
        (select NUM FROM IMPORTFFTA.FFTACATEGORIE where CODE="CATÉGORIE"),
        IFNULL((select NUM FROM IMPORTFFTA.FFTANIVEAU where CODE="NIVEAU"), 0), (select NUM FROM IMPORTFFTA.FFTAARC where CODE="ARME"),@DATE_MODIF FROM IMPORTFFTA.RESULT_LICENCE;
MERGE INTO ASSOCIER_CATEGORIE_CONTACT (ID_CONTACT, NUM_CATEGORIE_CONTACT) SELECT ID_CONTACT, 1 FROM ARCHERS;    
COMMIT;

UPDATE ENTITE SET TYPEENTITE = 2 WHERE AGREMENTENTITE like '%000';
UPDATE ENTITE SET TYPEENTITE = 1 WHERE AGREMENTENTITE like '%00000';
UPDATE ENTITE SET TYPEENTITE = 0 WHERE AGREMENTENTITE like '0000000';

DROP SCHEMA IMPORTFFTA;

COMMIT;

SET LOG 1;
SET AUTOCOMMIT ON;