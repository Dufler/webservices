package it.ltc.services.custom.dao;

import org.jboss.logging.Logger;

import it.ltc.database.dao.common.LoginController;
import it.ltc.database.model.centrale.Commessa;
import it.ltc.database.model.utente.Utente;
import it.ltc.services.custom.exception.CustomException;

/**
 * Fabbrica di DAO capace di trovare il dao giusto per l'utente e la commessa che ne richiedono uno.<br>
 * <br>
 * https://stackoverflow.com/questions/46543172/spring-use-different-bean-as-per-request-value
 * Anche se non è possibile applicarla a causa delle diverse persistence unit non sembra male.
 * In realtà sarebbe possibile se nella configurazione dei bean ne mettessimo uno per persistence unit utilizzata però non mi sembra comodo.
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
			return findDao(user, commessa);
		else {
			logger.warn(NESSUNA_COMMESSA);
			throw new CustomException(NESSUNA_COMMESSA);
		}
	}

	/**
	 * Metodo da implementare, qui viene istanziato e restituito il dao.
	 * @return L'oggetto DAO capace di gestire i dati.
	 */
	protected abstract T findDao(Utente user, Commessa commessa);
}
