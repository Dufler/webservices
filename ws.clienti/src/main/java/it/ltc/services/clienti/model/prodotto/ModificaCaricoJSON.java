package it.ltc.services.clienti.model.prodotto;

public class ModificaCaricoJSON {
	
	public enum StatoCarico { INSERITO, ARRIVATO, IN_LAVORAZIONE, LAVORATO, CHIUSO }
	public enum Riscontro { NESSUNO, TUTTI, RANDOM }
	public enum LavorazioneSeriali { SI, NO }
	
	private int id;
	private StatoCarico stato;
	private Riscontro riscontro;
	private LavorazioneSeriali seriali;
	
	public ModificaCaricoJSON() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public StatoCarico getStato() {
		return stato;
	}

	public void setStato(StatoCarico stato) {
		this.stato = stato;
	}

	public Riscontro getRiscontro() {
		return riscontro;
	}

	public void setRiscontro(Riscontro riscontro) {
		this.riscontro = riscontro;
	}

	public LavorazioneSeriali getSeriali() {
		return seriali;
	}

	public void setSeriali(LavorazioneSeriali seriali) {
		this.seriali = seriali;
	}

}
