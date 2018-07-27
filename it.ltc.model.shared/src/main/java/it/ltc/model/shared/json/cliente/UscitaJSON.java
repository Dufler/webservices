package it.ltc.model.shared.json.cliente;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
public class UscitaJSON {
	
	private int id;
	private Date dataOrdine;
	
	@JsonInclude(value=Include.NON_NULL)
	private Date dataConsegna;
	private String tipo;
	private Integer priorita;
	
	@JsonInclude(value=Include.NON_NULL)
	private String note;
	private String riferimentoOrdine;
	private String stato;
	
	@JsonInclude(value=Include.NON_NULL)
	private IndirizzoJSON destinatario;
	
	@JsonInclude(value=Include.NON_NULL)
	private IndirizzoJSON mittente;

	@JsonInclude(value=Include.NON_NULL)
	private String codiceTracking;
	
	private int pezziOrdinati;
	private int pezziImballati;
	
	public UscitaJSON() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDataOrdine() {
		return dataOrdine;
	}

	public void setDataOrdine(Date dataOrdine) {
		this.dataOrdine = dataOrdine;
	}

	public Date getDataConsegna() {
		return dataConsegna;
	}

	public void setDataConsegna(Date dataConsegna) {
		this.dataConsegna = dataConsegna;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getPriorita() {
		return priorita;
	}

	public void setPriorita(Integer priorita) {
		this.priorita = priorita;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getRiferimentoOrdine() {
		return riferimentoOrdine;
	}

	public void setRiferimentoOrdine(String riferimentoOrdine) {
		this.riferimentoOrdine = riferimentoOrdine;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
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

	public String getCodiceTracking() {
		return codiceTracking;
	}

	public void setCodiceTracking(String codiceTracking) {
		this.codiceTracking = codiceTracking;
	}

	public int getPezziOrdinati() {
		return pezziOrdinati;
	}

	public void setPezziOrdinati(int pezziOrdinati) {
		this.pezziOrdinati = pezziOrdinati;
	}

	public int getPezziImballati() {
		return pezziImballati;
	}

	public void setPezziImballati(int pezziImballati) {
		this.pezziImballati = pezziImballati;
	}

	@Override
	public String toString() {
		return "UscitaJSON [id=" + id + ", dataOrdine=" + dataOrdine + ", dataConsegna=" + dataConsegna + ", tipo=" + tipo + ", priorita=" + priorita + ", note=" + note + ", riferimentoOrdine=" + riferimentoOrdine + ", stato=" + stato + ", destinatario=" + destinatario
				+ ", mittente=" + mittente + ", codiceTracking=" + codiceTracking + ", pezziOrdinati=" + pezziOrdinati + ", pezziImballati=" + pezziImballati + "]";
	}

}
