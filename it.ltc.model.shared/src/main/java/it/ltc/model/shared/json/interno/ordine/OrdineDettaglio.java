package it.ltc.model.shared.json.interno.ordine;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Classe che modella il flusso dati che rappresenta una riga d'ordine.
 * @author Damiano
 *
 */
public class OrdineDettaglio {
	
	private int id;
	
	private int ordine;
	private int riga;
	private int articolo;
	private String magazzino;
	
	private int quantitaOrdinata;
	private int quantitaAssegnata;
	private int quantitaImballata;
	
	private String note;
	
	@JsonInclude(value=Include.NON_EMPTY)
	private List<String> seriali;
	
	public OrdineDettaglio() {}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrdineDettaglio other = (OrdineDettaglio) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrdineDettaglio [id=" + id + ", ordine=" + ordine + ", riga=" + riga + ", articolo=" + articolo + ", magazzino=" + magazzino + ", quantitaOrdinata=" + quantitaOrdinata + ", quantitaImballata=" + quantitaImballata + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrdine() {
		return ordine;
	}

	public void setOrdine(int ordine) {
		this.ordine = ordine;
	}

	public int getRiga() {
		return riga;
	}

	public void setRiga(int riga) {
		this.riga = riga;
	}

	public int getArticolo() {
		return articolo;
	}

	public void setArticolo(int articolo) {
		this.articolo = articolo;
	}

	public String getMagazzino() {
		return magazzino;
	}

	public void setMagazzino(String magazzino) {
		this.magazzino = magazzino;
	}

	public int getQuantitaOrdinata() {
		return quantitaOrdinata;
	}

	public void setQuantitaOrdinata(int quantitaOrdinata) {
		this.quantitaOrdinata = quantitaOrdinata;
	}

	public int getQuantitaAssegnata() {
		return quantitaAssegnata;
	}

	public void setQuantitaAssegnata(int quantitaAssegnata) {
		this.quantitaAssegnata = quantitaAssegnata;
	}

	public int getQuantitaImballata() {
		return quantitaImballata;
	}

	public void setQuantitaImballata(int quantitaImballata) {
		this.quantitaImballata = quantitaImballata;
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

}
