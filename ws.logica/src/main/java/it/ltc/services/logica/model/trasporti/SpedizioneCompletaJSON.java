package it.ltc.services.logica.model.trasporti;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SpedizioneCompletaJSON {
	
	private int id;
	private String corriere;
	private Date data;
	private IndirizzoJSON destinatario;
	private IndirizzoJSON mittente;
	private int colli;
	private double peso;
	private double volume;
	private int pezzi;
	private ContrassegnoJSON contrassegno;
	private String servizio;
	private String stato;
	private String letteraDiVettura;
	private String note;
	private String riferimento;
	private boolean ritardo;
	private boolean giacenza;
	private boolean fatturata;
	private List<TrackingJSON> tracking;
	
	public SpedizioneCompletaJSON() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCorriere() {
		return corriere;
	}

	public void setCorriere(String corriere) {
		this.corriere = corriere;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public IndirizzoJSON getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(IndirizzoJSON destinatario) {
		this.destinatario = destinatario;
	}

	public IndirizzoJSON getMittente() {
		return mittente;
	}

	public void setMittente(IndirizzoJSON mittente) {
		this.mittente = mittente;
	}

	public int getColli() {
		return colli;
	}

	public void setColli(int colli) {
		this.colli = colli;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public int getPezzi() {
		return pezzi;
	}

	public void setPezzi(int pezzi) {
		this.pezzi = pezzi;
	}

	public ContrassegnoJSON getContrassegno() {
		return contrassegno;
	}

	public void setContrassegno(ContrassegnoJSON contrassegno) {
		this.contrassegno = contrassegno;
	}

	public String getServizio() {
		return servizio;
	}

	public void setServizio(String servizio) {
		this.servizio = servizio;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getLetteraDiVettura() {
		return letteraDiVettura;
	}

	public void setLetteraDiVettura(String letteraDiVettura) {
		this.letteraDiVettura = letteraDiVettura;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getRiferimento() {
		return riferimento;
	}

	public void setRiferimento(String riferimento) {
		this.riferimento = riferimento;
	}

	public boolean isRitardo() {
		return ritardo;
	}

	public void setRitardo(boolean ritardo) {
		this.ritardo = ritardo;
	}

	public boolean isGiacenza() {
		return giacenza;
	}

	public void setGiacenza(boolean giacenza) {
		this.giacenza = giacenza;
	}

	public boolean isFatturata() {
		return fatturata;
	}

	public void setFatturata(boolean fatturata) {
		this.fatturata = fatturata;
	}

	public List<TrackingJSON> getTracking() {
		return tracking;
	}

	public void setTracking(List<TrackingJSON> tracking) {
		this.tracking = tracking;
	}

}
