package it.ltc.services.sede.model.carico;

import java.util.List;

/**
 * Rappresenta un certa quantità di prodotto contenuto in un collo in ingresso.
 * @author Damiano
 *
 */
@Deprecated
public class ProdottoInRiscontroJSON {
	
	private int id;
	private int quantita;
	private List<String> seriali;
	
	public ProdottoInRiscontroJSON() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	public List<String> getSeriali() {
		return seriali;
	}

	public void setSeriali(List<String> seriali) {
		this.seriali = seriali;
	}

}
