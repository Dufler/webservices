package it.ltc.model.shared.json.interno.carico;

import java.util.Date;

public class CaricoStato {
	
	private int carico;
	private String stato;
	private Date data;
	
	public CaricoStato() {}

	public int getCarico() {
		return carico;
	}

	public void setCarico(int carico) {
		this.carico = carico;
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
