package it.ltc.services.logica.model.utente;

/**
 * Contiene le info della richiesta necessaria a reimpostare la password dell'utente.
 * La risorsa identifica l'utente per cui si sta reimpostando la password.
 * @author Damiano
 *
 */
public class ReimpostaPassword {
	
	private String risorsa;
	private String nuovaPassword;
	
	public ReimpostaPassword() {}

	public String getRisorsa() {
		return risorsa;
	}

	public void setRisorsa(String risorsa) {
		this.risorsa = risorsa;
	}

	public String getNuovaPassword() {
		return nuovaPassword;
	}

	public void setNuovaPassword(String nuovaPassword) {
		this.nuovaPassword = nuovaPassword;
	}

}
