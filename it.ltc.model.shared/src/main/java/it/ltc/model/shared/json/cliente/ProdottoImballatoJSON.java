package it.ltc.model.shared.json.cliente;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ProdottoImballatoJSON {
	
	private String prodotto;
	private String descrizione;
	private String taglia;
	private int quantitaImballata;
	
	public ProdottoImballatoJSON() {}

	public String getProdotto() {
		return prodotto;
	}

	public void setProdotto(String prodotto) {
		this.prodotto = prodotto;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getTaglia() {
		return taglia;
	}

	public void setTaglia(String taglia) {
		this.taglia = taglia;
	}

	public int getQuantitaImballata() {
		return quantitaImballata;
	}

	public void setQuantitaImballata(int quantitaImballata) {
		this.quantitaImballata = quantitaImballata;
	}

	@Override
	public String toString() {
		return "ProdottoImballatoJSON [prodotto=" + prodotto + ", quantitaImballata=" + quantitaImballata + "]";
	}

}
