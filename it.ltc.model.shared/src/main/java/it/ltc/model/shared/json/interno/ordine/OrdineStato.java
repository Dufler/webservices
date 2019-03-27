package it.ltc.model.shared.json.interno.ordine;

import java.util.Date;

public class OrdineStato {
	
	private int ordine;
	private String stato;
	private Date data;
	
	public OrdineStato() {}

	public int getOrdine() {
		return ordine;
	}

	public void setOrdine(int ordine) {
		this.ordine = ordine;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

}
