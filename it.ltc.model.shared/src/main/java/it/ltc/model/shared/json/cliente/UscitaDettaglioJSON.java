package it.ltc.model.shared.json.cliente;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
public class UscitaDettaglioJSON {
	
	@JsonInclude(value=Include.NON_NULL)
	private String riferimento;
	private int riga;
	private int quantitaOrdinata;
	private int quantitaImballata;
	@JsonInclude(value=Include.NON_NULL)
	private String magazzino;
	private String prodotto;
	
	@JsonInclude(value=Include.NON_NULL)
	private String tipo;
	
	@JsonInclude(value=Include.NON_NULL)
	private String note;
	
	@JsonInclude(value=Include.NON_EMPTY)
	private List<String> seriali;
	
	public UscitaDettaglioJSON() {}

	public String getRiferimento() {
		return riferimento;
	}

	public void setRiferimento(String riferimento) {
		this.riferimento = riferimento;
	}

	public int getRiga() {
		return riga;
	}

	public void setRiga(int riga) {
		this.riga = riga;
	}

	public int getQuantitaOrdinata() {
		return quantitaOrdinata;
	}

	public void setQuantitaOrdinata(int quantitaOrdinata) {
		this.quantitaOrdinata = quantitaOrdinata;
	}

	public int getQuantitaImballata() {
		return quantitaImballata;
	}

	public void setQuantitaImballata(int quantitaImballata) {
		this.quantitaImballata = quantitaImballata;
	}

	public String getMagazzino() {
		return magazzino;
	}

	public void setMagazzino(String magazzino) {
		this.magazzino = magazzino;
	}

	public String getProdotto() {
		return prodotto;
	}

	public void setProdotto(String prodotto) {
		this.prodotto = prodotto;
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<String> getSeriali() {
		return seriali;
	}

	public void setSeriali(List<String> seriali) {
		this.seriali = seriali;
	}

	@Override
	public String toString() {
		return "UscitaDettaglioJSON [riferimento=" + riferimento + ", riga=" + riga + ", quantitaOrdinata=" + quantitaOrdinata + ", quantitaImballata=" + quantitaImballata + ", magazzino=" + magazzino + ", prodotto=" + prodotto + ", tipo=" + tipo + ", note=" + note + ", seriali="
				+ seriali + "]";
	}

}
