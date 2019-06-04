package it.ltc.services.custom.dao;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.database.model.utente.CommessaUtenti;
import it.ltc.services.custom.controller.LoginController;
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
	
	private static final String NESSUNA_COMMESSA = "Nessuna commessa associabile all'utenza. Ne va specificata una, se necessario.";

	@Autowired
	protected LoginController loginManager;
	
	/**
	 * Cerca tra le varie implementazioni una che vada bene per l'utente e la commessa.
	 * E' necessario che l'utente disponga dei permessi per poterla usare.
	 */
	public T getDao(UtenteUtenti user, String risorsaCommessa) {
		return getDao(user, risorsaCommessa, true);
	}
	
	/**
	 * Cerca tra le varie implementazioni una che vada bene per l'utente e la commessa.
	 * E' possibile specificare se devono essere controllati i permessi dell'utente per la commessa specificata.
	 */
	public T getDao(UtenteUtenti user, String risorsaCommessa, boolean checkPermessoCommessa) {
		CommessaUtenti commessa = checkPermessoCommessa ? loginManager.getCommessaByUserAndResource(user, risorsaCommessa) : loginManager.getCommessaByResource(risorsaCommessa);
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
	protected abstract T findDao(UtenteUtenti user, CommessaUtenti commessa);
}
