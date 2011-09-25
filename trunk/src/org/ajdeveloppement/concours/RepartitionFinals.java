/*
 * Créé le 29 juil. 2010 à 15:46:04 pour ArcCompetition / ArcCompétition
 *
 * Copyright 2002-2010 - Aurélien JEOFFRAY
 *
 * http://arccompetition.ajdeveloppement.org
 *
 * *** CeCILL Terms *** 
 *
 * FRANCAIS:
 *
 * Ce logiciel est un programme informatique servant à gérer les compétions de type
 * spécial jeunes de tir à l'Arc. 
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".
 *
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant 
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 
 * 
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 *
 * ENGLISH:
 * 
 * This software is a computer program whose purpose is to manage the young special archery
 * tournament.
 *
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 *
 *  *** GNU GPL Terms *** 
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.ajdeveloppement.concours;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ajdeveloppement.commons.UncheckedException;
import org.ajdeveloppement.commons.persistence.ObjectPersistence;
import org.ajdeveloppement.commons.persistence.ObjectPersistenceException;
import org.ajdeveloppement.commons.persistence.Session;
import org.ajdeveloppement.commons.persistence.StoreHelper;
import org.ajdeveloppement.commons.persistence.sql.SessionHelper;
import org.ajdeveloppement.commons.persistence.sql.SqlField;
import org.ajdeveloppement.commons.persistence.sql.SqlPrimaryKey;
import org.ajdeveloppement.commons.persistence.sql.SqlStoreHandler;
import org.ajdeveloppement.commons.persistence.sql.SqlTable;

/**
 * @author Aurélien JEOFFRAY
 *
 */
@SqlTable(name="REPARTITION_PHASE_FINALE")
@SqlPrimaryKey(fields={"NUM_REPARTITION_PHASE_FINALE", "NUM_TYPE_REPARTITION"})
public class RepartitionFinals implements ObjectPersistence {
	private static StoreHelper<RepartitionFinals> helper = null;
	static {
		try {
			helper = new StoreHelper<RepartitionFinals>(new SqlStoreHandler<RepartitionFinals>(
					ApplicationCore.dbConnection, RepartitionFinals.class));
		} catch (SQLException e) {
			throw new UncheckedException(e);
		}
	}
	
	public static final short TYPE_INDIV_INTERNATIONNAL = 1;
	public static final short TYPE_INDIV_FRANCAIS = 2;
	
	@SqlField(name="NUM_REPARTITION_PHASE_FINALE")
	private transient short numRepartition = -1;
	
	@SqlField(name="NUM_TYPE_REPARTITION")
	private short typeRepartition = TYPE_INDIV_INTERNATIONNAL;
	
	@SqlField(name="NUM_ORDRE")
	private short numeroOrdre = -1;
	
	public RepartitionFinals() {
	}
	
	/**
	 * @return numRepartition
	 */
	public short getNumRepartition() {
		return numRepartition;
	}

	/**
	 * @param numRepartition numRepartition à définir
	 */
	public void setNumRepartition(short numRepartition) {
		this.numRepartition = numRepartition;
	}

	/**
	 * @param typeRepartition typeRepartition à définir
	 */
	public void setTypeRepartition(short typeRepartition) {
		this.typeRepartition = typeRepartition;
	}

	/**
	 * @return typeRepartition
	 */
	public short getTypeRepartition() {
		return typeRepartition;
	}

	/**
	 * @return numeroOrdre
	 */
	public short getNumeroOrdre() {
		return numeroOrdre;
	}

	/**
	 * @param numeroOrdre numeroOrdre à définir
	 */
	public void setNumeroOrdre(short numeroOrdre) {
		this.numeroOrdre = numeroOrdre;
	}
	
	public static List<RepartitionFinals> getRepartitionFinalsPhase(List<RepartitionFinals> repartitionsFinals, int phase) {
		int nbParticipant = (int)Math.pow(2, phase+1);
		List<RepartitionFinals> repartitionsFinalsPhase = new ArrayList<RepartitionFinals>();
		for(RepartitionFinals repartitionFinals : repartitionsFinals) {
			if(repartitionFinals.getNumeroOrdre() <= nbParticipant)
				repartitionsFinalsPhase.add(repartitionFinals);
			
			if(repartitionsFinalsPhase.size() == nbParticipant) //inutile de continuer à boucler si on a déjà tout le monde
				break;
		}
		
		return repartitionsFinalsPhase;
	}

	@Override
	public void save() throws ObjectPersistenceException {
		SessionHelper.startSaveSession(ApplicationCore.dbConnection, this);
	}
	
	@Override
	public void delete() throws ObjectPersistenceException {
		SessionHelper.startDeleteSession(ApplicationCore.dbConnection, this);
	}

	@Override
	public void save(Session session) throws ObjectPersistenceException {
		if(session == null || !session.contains(this)) {
			if(numRepartition > -1)
				helper.save(this);
		}
	}

	@Override
	public void delete(Session session) throws ObjectPersistenceException {
		if(session == null || !session.contains(this)) {
			if(numRepartition > -1)
				helper.delete(this);
		}
	}
}

	