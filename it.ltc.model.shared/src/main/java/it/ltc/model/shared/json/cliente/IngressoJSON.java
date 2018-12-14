package it.ltc.model.shared.json.cliente;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class IngressoJSON {
	
	private int id;
	private Date dataArrivo;
	private String fornitore;
	private int pezziLetti;
	private int pezziStimati;
	private String riferimentoCliente;
	private String stato;
	private String tipo;
	private String note;
	
	public IngressoJSON() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDataArrivo() {
		return dataArrivo;
	}

	public void setDataArrivo(Date dataArrivo) {
		this.dataArrivo = dataArrivo;
	}

	public String getFornitore() {
		return fornitore;
	}

	public void setFornitore(String fornitore) {
		this.fornitore = fornitore;
	}

	public int getPezziLetti() {
		return pezziLetti;
	}

	public void setPezziLetti(int pezziLetti) {
		this.pezziLetti = pezziLetti;
	}

	public int getPezziStimati() {
		return pezziStimati;
	}

	public void setPezziStimati(int pezziStimati) {
		this.pezziStimati = pezziStimati;
	}

	public String getRiferimentoCliente() {
		return riferimentoCliente;
	}

	public void setRiferimentoCliente(String riferimentoCliente) {
		this.riferimentoCliente = riferimentoCliente;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		//FIXME - Ho introdotto questa modifica perchè nel vecchio esiste ancora un transitorio con questo stato "legacy"
		if (stato != null && stato.equals("ATTESA")) stato = "INSERITO";
		this.stato = stato;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return "IngressoJSON [id=" + id + ", dataArrivo=" + dataArrivo + ", fornitore=" + fornitore + ", pezziLetti=" + pezziLetti + ", pezziStimati=" + pezziStimati + ", riferimentoCliente=" + riferimentoCliente + ", stato=" + stato + ", tipo=" + tipo + ", note=" + note + "]";
	}

}
