package it.ltc.services.custom.exception;

import java.util.List;

/**
 * Classe che rappresenta un messaggio d'errore da restituire al client.<br>
 * Il metodo toString è stato esteso per rappresentare un JSON.
 * @author Damiano
 *
 */
public class CustomErrorMessage {
	
	private final int errorCode;
	private final String message;
	private final List<CustomErrorCause> errors;
	
	/**
	 * Custruttore di default a cui vanno specificati il codice d'errore, il messaggio e la lista di problemi.
	 * @param errorCode il codice d'errore secondo lo standard HTTP.
	 * @param message il messaggio specifico d'errore.
	 * @param errors lista di problemi, eventualmente vuota o nulla.
	 */
	public CustomErrorMessage(int errorCode, String message, List<CustomErrorCause> errors) {
		this.errorCode = errorCode;
		this.message = message;
		this.errors = errors;
	}
	
	/**
	 * Costruttore che utilizza le informazioni contenute nell'eccezione.
	 * @param exception L'eccezione da cui ricavare le informazioni sull'errore.
	 */
	public CustomErrorMessage(CustomException exception) {
		this.errorCode = exception.getHttpErrorCode();
		this.message = exception.getMessage();
		this.errors = exception.getErrors();
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}

	public List<CustomErrorCause> getErrors() {
		return errors;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("{\"message\": \"" + message + "\"");
		if (errors != null && !errors.isEmpty()) {
			s.append(", \"errors\": [ ");
			for (CustomErrorCause error : errors) {
				s.append(error.toString());
				s.append(", ");
			}
			s.deleteCharAt(s.length() - 2);
			s.append("]");
		}
		s.append(" }");
		return s.toString();
	}
	
	

}
