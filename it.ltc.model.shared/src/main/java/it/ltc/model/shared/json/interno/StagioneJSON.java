package it.ltc.model.shared.json.interno;

/**
 * Classe che modella una stagione. Il codice è una chiave identificativa e dovrebbe essere nella forma XXYY dove XX sono 2 caratteri (es. AI, PE, CO, ...) e YY sono 2 cifre che indicano l'anno (es. 19)
 * @author Damiano
 *
 */
public class StagioneJSON {
	
	protected String codice;
	protected String descrizione;
	
	public StagioneJSON() {}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Override
	public String toString() {
		return "StagioneJSON [codice=" + codice + ", descrizione=" + descrizione + "]";
	}

}
