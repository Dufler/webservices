package it.ltc.services.clienti.model.prodotto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ParticolaritaJSON {
	
	private String note;
	
	public ParticolaritaJSON() {}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
