package it.ltc.services.logica.model.fatturazione;

import java.util.List;

import it.ltc.database.model.centrale.FatturaVoce;

public class ElementoFatturazioneJSON {
	
	private int ambito;
	private int riferimento;
	private double totale;
	private List<FatturaVoce> voci;
	
	public ElementoFatturazioneJSON() {}

	public int getAmbito() {
		return ambito;
	}

	public void setAmbito(int ambito) {
		this.ambito = ambito;
	}

	public int getRiferimento() {
		return riferimento;
	}

	public void setRiferimento(int riferimento) {
		this.riferimento = riferimento;
	}

	public double getTotale() {
		return totale;
	}

	public void setTotale(double totale) {
		this.totale = totale;
	}

	public List<FatturaVoce> getVoci() {
		return voci;
	}

	public void setVoci(List<FatturaVoce> voci) {
		this.voci = voci;
	}

}
