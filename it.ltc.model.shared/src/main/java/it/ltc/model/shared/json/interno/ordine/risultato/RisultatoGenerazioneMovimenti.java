package it.ltc.model.shared.json.interno.ordine.risultato;

import java.util.List;

import it.ltc.model.shared.json.interno.ordine.OrdineTestata;

public class RisultatoGenerazioneMovimenti {
	
	private OrdineTestata ordine;
	private boolean esito;
	private List<String> messaggi;
	
	public RisultatoGenerazioneMovimenti() {}

	public OrdineTestata getOrdine() {
		return ordine;
	}

	public void setOrdine(OrdineTestata ordine) {
		this.ordine = ordine;
	}

	public boolean isEsito() {
		return esito;
	}

	public void setEsito(boolean esito) {
		this.esito = esito;
	}

	public List<String> getMessaggi() {
		return messaggi;
	}

	public void setMessaggi(List<String> messaggi) {
		this.messaggi = messaggi;
	}

}
