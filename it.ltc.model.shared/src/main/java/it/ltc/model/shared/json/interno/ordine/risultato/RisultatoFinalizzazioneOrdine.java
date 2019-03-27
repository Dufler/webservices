package it.ltc.model.shared.json.interno.ordine.risultato;

import java.util.List;

import it.ltc.model.shared.json.interno.ordine.OrdineTestata;

/**
 * Rappresenta il risultato della finalizzazione di un ordine.
 * @author Damiano
 *
 */
public class RisultatoFinalizzazioneOrdine {
	
	private OrdineTestata ordine;
	private List<ProblemaFinalizzazioneOrdine> problemi;
	
	public RisultatoFinalizzazioneOrdine() {}

	public OrdineTestata getOrdine() {
		return ordine;
	}

	public void setOrdine(OrdineTestata ordine) {
		this.ordine = ordine;
	}

	public List<ProblemaFinalizzazioneOrdine> getProblemi() {
		return problemi;
	}

	public void setProblemi(List<ProblemaFinalizzazioneOrdine> problemi) {
		this.problemi = problemi;
	}

}
