package it.ltc.model.shared.json.cliente;

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

	@Override
	public String toString() {
		return "ParticolaritaJSON [note=" + note + "]";
	}

}
