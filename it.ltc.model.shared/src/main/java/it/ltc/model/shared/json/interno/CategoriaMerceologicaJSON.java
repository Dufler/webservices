package it.ltc.model.shared.json.interno;

public class CategoriaMerceologicaJSON {
	
	private String nome;
	private String descrizione;
	private int commessa;
	private String stato;
	private int brand;
	
	public CategoriaMerceologicaJSON() {}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public int getCommessa() {
		return commessa;
	}

	public void setCommessa(int commessa) {
		this.commessa = commessa;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public int getBrand() {
		return brand;
	}

	public void setBrand(int brand) {
		this.brand = brand;
	}

	@Override
	public String toString() {
		return "CategoriaMerceologicaJSON [nome=" + nome + ", descrizione=" + descrizione + "]";
	}

}
