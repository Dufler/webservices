package it.ltc.services.logica.model.trasporti;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TrackingJSON implements Comparable<TrackingJSON> {
	
	private Date data;
	private String stato;
	private String descrizione;
	
	public TrackingJSON() {}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone="Europe/Rome")
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Override
	public int compareTo(TrackingJSON o) {
		Date d1 = data != null ? data : new Date();
		Date d2 = o.getData() != null ? o.getData() : new Date();
		int compare = d1.compareTo(d2);
		return compare;
	}

}
