package it.ltc.model.shared.json.cliente;

import java.util.List;

public class TipoCaricoJSON {
	
	private String codice;
	private String descrizione;
	private List<Integer> particolarita;
	
	public TipoCaricoJSON() {}

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

	public List<Integer> getParticolarita() {
		return particolarita;
	}

	public void setParticolarita(List<Integer> particolarita) {
		this.particolarita = particolarita;
	}

	@Override
	public String toString() {
		return "TipoCaricoJSON [codice=" + codice + ", descrizione=" + descrizione + ", particolarita=" + particolarita + "]";
	}

}
