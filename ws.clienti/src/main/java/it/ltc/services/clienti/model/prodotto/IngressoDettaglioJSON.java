package it.ltc.services.clienti.model.prodotto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class IngressoDettaglioJSON {
	
	private String riferimento;
	private int riga;
	private String prodotto;
	private String collo;
	private String magazzino;
	private int quantitaPrevista;
	private int quantitaVerificata;
	
	@JsonInclude(value=Include.NON_NULL)
	private String madeIn;
	
	@JsonInclude(value=Include.NON_NULL)
	private String note;
	
	@JsonInclude(value=Include.NON_EMPTY)
	private List<String> seriali;
	
	public IngressoDettaglioJSON() {}

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

	public String getProdotto() {
		return prodotto;
	}

	public void setProdotto(String prodotto) {
		this.prodotto = prodotto;
	}

	public String getCollo() {
		return collo;
	}

	public void setCollo(String collo) {
		this.collo = collo;
	}

	public String getMagazzino() {
		return magazzino;
	}

	public void setMagazzino(String magazzino) {
		this.magazzino = magazzino;
	}

	public int getQuantitaPrevista() {
		return quantitaPrevista;
	}

	public void setQuantitaPrevista(int quantitaPrevista) {
		this.quantitaPrevista = quantitaPrevista;
	}

	public int getQuantitaVerificata() {
		return quantitaVerificata;
	}

	public void setQuantitaVerificata(int quantitaVerificata) {
		this.quantitaVerificata = quantitaVerificata;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getMadeIn() {
		return madeIn;
	}

	public void setMadeIn(String madeIn) {
		this.madeIn = madeIn;
	}

	public List<String> getSeriali() {
		return seriali;
	}

	public void setSeriali(List<String> seriali) {
		this.seriali = seriali;
	}

}
