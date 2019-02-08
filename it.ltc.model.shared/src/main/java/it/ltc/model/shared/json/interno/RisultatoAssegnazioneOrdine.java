package it.ltc.model.shared.json.interno;

import java.util.List;

import it.ltc.model.shared.json.interno.OrdineTestata;

/**
 * Questa classe modella un JSON che contiene le informazioni sull'assegnazione di un ordine.
 * @author Damiano
 *
 */
public class RisultatoAssegnazioneOrdine {
	
public enum StatoAssegnazione { 
		
		OK("Ok"), 
		PARZIALE("Parziale"), 
		NONDEFINITA("Errore");
		
		private final String descrizione;
		
		private StatoAssegnazione(String descrizione) {
			this.descrizione = descrizione;
		}
		
		@Override
		public String toString() {
			return descrizione;
		}
	}
	
	private OrdineTestata ordine;
	private StatoAssegnazione stato;
	private List<RisultatoAssegnazioneRigaOrdine> assegnazioni;
	
	public RisultatoAssegnazioneOrdine() {}

	public OrdineTestata getOrdine() {
		return ordine;
	}

	public void setOrdine(OrdineTestata ordine) {
		this.ordine = ordine;
	}

	public StatoAssegnazione getStato() {
		return stato;
	}

	public void setStato(StatoAssegnazione stato) {
		this.stato = stato;
	}

	public List<RisultatoAssegnazioneRigaOrdine> getAssegnazioni() {
		return assegnazioni;
	}

	public void setAssegnazioni(List<RisultatoAssegnazioneRigaOrdine> assegnazioni) {
		this.assegnazioni = assegnazioni;
	}

}
