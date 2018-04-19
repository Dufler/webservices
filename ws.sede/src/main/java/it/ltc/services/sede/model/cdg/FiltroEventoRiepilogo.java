package it.ltc.services.sede.model.cdg;

import java.util.Date;

public class FiltroEventoRiepilogo {
	
	private int commessa;
	private Date inizio;
	private Date fine;
	
	public FiltroEventoRiepilogo() {}

	public int getCommessa() {
		return commessa;
	}

	public void setCommessa(int commessa) {
		this.commessa = commessa;
	}

	public Date getInizio() {
		return inizio;
	}

	public void setInizio(Date inizio) {
		this.inizio = inizio;
	}

	public Date getFine() {
		return fine;
	}

	public void setFine(Date fine) {
		this.fine = fine;
	}

}
