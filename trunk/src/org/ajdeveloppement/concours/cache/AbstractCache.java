package org.ajdeveloppement.concours.cache;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestion d'un cache d'une collection d'objet
 * 
 * @author Aurélien JEOFFRAY
 *
 * @param <UID> le type de la clé permettant d'identifier l'objet de manière unique
 * @param <CT> le type de l'objet à mettre en cache
 */
public abstract class AbstractCache<UID, CT> {
	private Map<UID,SoftReference<CT>> instanceCache = Collections.synchronizedMap(
			new HashMap<UID,SoftReference<CT>>());
	
	private int nbCallBeforeCleanKey = 100;
	
	/**
	 * Ajoute une instance au cache
	 * 
	 * @param object l'instance à ajouter au cache
	 */
	public abstract void add(CT object);
	
	/**
	 * Ajoute une instance au cache
	 * 
	 * @param uniqueObjectId la clé unique identifiant l'instance
	 * @param object l'objet à mettre en cache
	 */
	protected void put(UID uniqueObjectId, CT object) {
		nbCallBeforeCleanKey--;
		instanceCache.put(uniqueObjectId, new SoftReference<CT>(object));
		
		if(nbCallBeforeCleanKey == 0)
			removeFreeReference();
	}
	
	/**
	 * Retourne une instance, si disponible à partir de son identifiant. Si
	 * l'objet à été libéré de la mémoire ou n'a pas été mis en cache, retourne null
	 * 
	 * @param uniqueObjectId l'identifiant de l'objet à retourner
	 * @return l'instance en cache ou null si l'instance n'est pas disponible
	 */
	public CT get(UID uniqueObjectId) {
		if(instanceCache.containsKey(uniqueObjectId))
			return instanceCache.get(uniqueObjectId).get();
		return null;
	}
	
	/**
	 * Retire une instance du cache
	 * 
	 * @param uniqueObjectId l'identifiant de l'instance à retirer du cache
	 */
	public void remove(UID uniqueObjectId) {
		instanceCache.remove(uniqueObjectId);
	}
	
	/**
	 * Indique si un objet à été placé en cache. N'indique pas si l'instance
	 * est toujours chargé ou si l'objet à été libéré.
	 * 
	 * @param uniqueObjectId l'identifiant d'instance à tester
	 * @return true si l'identifiant est trouvé dans le cache.
	 */
	public boolean containsKey(UID uniqueObjectId) {
		return instanceCache.containsKey(uniqueObjectId);
	}
	
	/**
	 * Retire du cache tous les identifiants d'objet dont les instances ont
	 * été libéré de la mémoire.
	 */
	public void removeFreeReference() {
		for(UID key : instanceCache.keySet()) {
			if(instanceCache.get(key).get() == null)
				instanceCache.remove(key);
		}
		nbCallBeforeCleanKey = 100;
	}
}
