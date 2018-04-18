package it.ltc.services.clienti.model.prodotto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FornitoreJSON {
	
	private int id;
	private String nome;
	private String riferimentoCliente;
	private String note;
	private IndirizzoJSON indirizzo;
	
	public FornitoreJSON() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRiferimentoCliente() {
		return riferimentoCliente;
	}

	public void setRiferimentoCliente(String riferimentoCliente) {
		this.riferimentoCliente = riferimentoCliente;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public IndirizzoJSON getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(IndirizzoJSON indirizzo) {
		this.indirizzo = indirizzo;
	}

	@Override
	public String toString() {
		return "FornitoreJSON [id=" + id + ", nome=" + nome + ", riferimentoCliente=" + riferimentoCliente + "]";
	}

}
