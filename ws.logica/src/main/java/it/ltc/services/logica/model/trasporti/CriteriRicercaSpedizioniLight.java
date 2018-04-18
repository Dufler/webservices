package it.ltc.services.logica.model.trasporti;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CriteriRicercaSpedizioniLight {
	
	private int idCommessa;
	private String riferimento;
	private String destinatario;
	private Date inizio;
	private Date fine;
	private String stato;
	private String archiviazione;
	
	public CriteriRicercaSpedizioniLight() {}

	public int getIdCommessa() {
		return idCommessa;
	}

	public void setIdCommessa(int idCommessa) {
		this.idCommessa = idCommessa;
	}

	public String getRiferimento() {
		return riferimento;
	}

	public void setRiferimento(String riferimento) {
		this.riferimento = riferimento;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public Date getInizio() {
		return inizio;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	public void setInizio(Date inizio) {
		this.inizio = inizio;
	}

	public Date getFine() {
		return fine;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	public void setFine(Date fine) {
		this.fine = fine;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getArchiviazione() {
		return archiviazione;
	}

	public void setArchiviazione(String archiviazione) {
		this.archiviazione = archiviazione;
	}

}
