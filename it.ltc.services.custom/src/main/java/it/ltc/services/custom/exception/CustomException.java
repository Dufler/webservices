package it.ltc.services.custom.exception;

import java.util.List;

/**
 * TODO - Promemoria, vanno sviluppate delle eccezioni particolari (es. DaoException) per gestire i casi in cui qualcosa va storto e segnalare l'errore all'utente. Tali eccezioni andranno gestite singolarmente dentro il CustomExceptionResolver.
 * @author Damiano
 *
 */
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final int httpErrorCode;
	private final List<CustomErrorCause> errors;
	
	/**
	 * Costruttore di default.<br>
	 * Il codice d'errore HTTP viene impostato a 400
	 * @param errorDescription La descrizione dell'errore.
	 */
	public CustomException(String errorDescription) {
		super(errorDescription);
		httpErrorCode = 400;
		errors = null;
	}
	
	/**
	 * Costruttore che permette di specificare anche il codice HTTP dell'errore.
	 * @param errorDescription La descrizione dell'errore.
	 * @param httpErrorCode Il codice identificativo dell'errore.
	 */
	public CustomException(String errorDescription, int httpErrorCode) {
		super(errorDescription);
		this.httpErrorCode = httpErrorCode;
		errors = null;
	}
	
	/**
	 * Costruttore che permette di specificare anche il codice HTTP dell'errore e la lista dei problemi.
	 * @param errorDescription
	 * @param httpErrorCode
	 * @param errors
	 */
	public CustomException(String errorDescription, int httpErrorCode, List<CustomErrorCause> errors) {
		super(errorDescription);
		this.httpErrorCode = httpErrorCode;
		this.errors = errors;
	}
	
	public int getHttpErrorCode() {
		return httpErrorCode;
	}

	public List<CustomErrorCause> getErrors() {
		return errors;
	}

}
