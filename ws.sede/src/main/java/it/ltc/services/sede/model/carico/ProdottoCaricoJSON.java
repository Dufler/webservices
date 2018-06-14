package it.ltc.services.sede.model.carico;

public class ProdottoCaricoJSON {
	
	private int id;
	private int carico;
	private int collo;
	private int prodotto;
	private int quantita;
	private int riga;
	private boolean forzaEccedenza;
	private String seriale;
	
	public ProdottoCaricoJSON() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCarico() {
		return carico;
	}

	public void setCarico(int carico) {
		this.carico = carico;
	}

	public int getCollo() {
		return collo;
	}

	public void setCollo(int collo) {
		this.collo = collo;
	}

	public int getProdotto() {
		return prodotto;
	}

	public void setProdotto(int prodotto) {
		this.prodotto = prodotto;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	public int getRiga() {
		return riga;
	}

	public void setRiga(int riga) {
		this.riga = riga;
	}

	public boolean isForzaEccedenza() {
		return forzaEccedenza;
	}

	public void setForzaEccedenza(boolean forzaEccedenza) {
		this.forzaEccedenza = forzaEccedenza;
	}

	public String getSeriale() {
		return seriale;
	}

	public void setSeriale(String seriale) {
		this.seriale = seriale;
	}

}
