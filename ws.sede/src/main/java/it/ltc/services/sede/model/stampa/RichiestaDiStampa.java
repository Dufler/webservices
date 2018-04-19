package it.ltc.services.sede.model.stampa;

/**
 * Rappresenta una richiesta di stampa.<br>
 * Devono essere indicati l'IP su cui rigirare la richiesta e il contenuto da stampare.
 * @author Damiano
 *
 */
public class RichiestaDiStampa {
	
	private String ip;
	private String content;
	
	public RichiestaDiStampa() {}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
