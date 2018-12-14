package it.ltc.model.shared.json.cliente;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
public class IndirizzoJSON {
	
	private int id;
	private String ragioneSociale;
	private String indirizzo;
	private String cap;
	private String localita;
	private String provincia;
	private String nazione;
	private String telefono;
	private String email;
	
	/**
	 * Viene usato solo per gli indirizzi dei destinatari e mittenti ordini nei sistemi legacy.<br>
	 * Non è valorizzato da altre parti e quindi non viene incluso se non è necesarrio.
	 */
	@JsonInclude(value=Include.NON_NULL)
	private String codice;
	
	public IndirizzoJSON() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getLocalita() {
		return localita;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getNazione() {
		return nazione;
	}

	public void setNazione(String nazione) {
		this.nazione = nazione;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	@Override
	public String toString() {
		return "IndirizzoJSON [ragionesociale=" + ragioneSociale + ", indirizzo=" + indirizzo + ", cap=" + cap
				+ ", localita=" + localita + ", provincia=" + provincia + ", nazione=" + nazione + ", telefono="
				+ telefono + ", email=" + email + "]";
	}

}
