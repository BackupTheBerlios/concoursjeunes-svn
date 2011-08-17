-- @author Aurélien JEOFFRAY
--SET MULTI_THREADED 1;
SET LOG 0;

SET @DATE_MODIF = CURRENT_DATE();
SET @NUM_FEDERATION_LFBTA = (select NUMFEDERATION FROM FEDERATION WHERE SIGLEFEDERATION='LFBTA');

CREATE SCHEMA IF NOT EXISTS IMPORTLFBTA;

CREATE TEMPORARY TABLE IMPORTLFBTA.CLUBS AS SELECT RANDOM_UUID() AS ID_ENTITE, AGREMENTENTITE, NOMENTITE, 
	ADRESSEENTITE, CODEPOSTALENTITE, VILLEENTITE, PAYS, NOTEENTITE FROM CSVREAD('ressources/update/clubs_lfbta.csv',null,'charset=UTF-8 fieldSeparator=; fieldDelimiter=\"');
UPDATE IMPORTLFBTA.CLUBS SET ID_ENTITE = IFNULL((SELECT ID_ENTITE FROM ENTITE WHERE ENTITE.AGREMENTENTITE=IMPORTLFBTA.CLUBS.AGREMENTENTITE), ID_ENTITE);
MERGE INTO Entite (ID_ENTITE, AGREMENTENTITE, NUMFEDERATION, NOMENTITE, ADRESSEENTITE, CODEPOSTALENTITE, VILLEENTITE, 
		PAYS, TYPEENTITE, NOTEENTITE, DATEMODIF, REMOVABLE) 
    SELECT distinct ID_ENTITE, AGREMENTENTITE, @NUM_FEDERATION_LFBTA, NOMENTITE, ADRESSEENTITE, CODEPOSTALENTITE,
    		VILLEENTITE, PAYS, 3, NOTEENTITE, @DATE_MODIF, 0
        from IMPORTLFBTA.CLUBS;

DROP SCHEMA IMPORTLFBTA;

COMMIT;

SET LOG 1;