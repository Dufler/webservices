package it.ltc.services.custom.dao;

import org.jboss.logging.Logger;

import it.ltc.database.dao.common.LoginController;
import it.ltc.database.model.centrale.Commessa;
import it.ltc.database.model.utente.Utente;
import it.ltc.services.custom.exception.CustomException;

/**
 * Fabbrica di DAO capace di trovare il dao giusto per l'utente e la commessa che ne richiedono uno.
 * @author Damiano
 *
 * @param <T>
 */
public abstract class FactoryDao<T> {
	
	private static final Logger logger = Logger.getLogger("FactoryDao");
	
	private static final String NESSUNA_COMMESSA = "Nessuna commessa associata all'utenza. Ne va specificata una, se necessario.";

	private final LoginController loginManager;
	
	public FactoryDao() {
		loginManager = LoginController.getInstance();
	}
	
	/**
	 * Cerca tra le varie implementazioni una che vada bene per l'utente e la commessa.
	 * @param commessa
	 * @return
	 */
	public T getDao(Utente user, String risorsaCommessa) {
		Commessa commessa = loginManager.getCommessaByUserAndResource(user, risorsaCommessa);
		if (commessa != null)
			return findDao(commessa);
		else {
			logger.warn(NESSUNA_COMMESSA);
			throw new CustomException(NESSUNA_COMMESSA);
		}
	}

	/**
	 * Metodo da implementare, qui viene istanziato e restituito il dao.
	 * @param commessa La commessa di cui gestire i dati.
	 * @return L'oggetto DAO capace di gestire i dati.
	 */
	protected abstract T findDao(Commessa commessa);
}
