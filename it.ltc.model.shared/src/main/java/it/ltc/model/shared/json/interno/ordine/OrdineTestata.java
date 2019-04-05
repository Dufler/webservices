package it.ltc.model.shared.json.interno.ordine;

import java.util.Date;

public class OrdineTestata {
	
	private int id;
	
	private Date documentoData;
	private String documentoRiferimento;
	private String documentoTipo;	
	
	private String numeroLista;
	private String riferimento;
	private String tipo;
	
	private Date dataOrdine;
	private Date dataCreazione;
	private Date dataConsegna;
	
	private int mittente;
	private int destinatario;
	private String ragioneSocialeDestinatario;
	
	private String stato;
	
	private int colli;
	private double peso;
	private double volume;
	
	private int quantitaOrdinataTotale;
	private int quantitaAssegnataTotale;
	private int quantitaImballataTotale;
	private int quantitaPrelevataTotale;
	
	private int priorita;
	private String note;
	
	//Vengono usati solo come criteri di filtraggio durante la ricerca.
	private Date da;
	private Date a;
	
	public OrdineTestata() {}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrdineTestata other = (OrdineTestata) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrdineTestata [id=" + id + ", riferimento=" + riferimento + ", tipo=" + tipo + ", dataOrdine=" + dataOrdine + ", ragioneSocialeDestinatario=" + ragioneSocialeDestinatario + ", stato=" + stato + ", quantitaOrdinataTotale=" + quantitaOrdinataTotale
				+ ", quantitaImballataTotale=" + quantitaImballataTotale + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDocumentoData() {
		return documentoData;
	}

	public void setDocumentoData(Date documentoData) {
		this.documentoData = documentoData;
	}

	public String getDocumentoRiferimento() {
		return documentoRiferimento;
	}

	public void setDocumentoRiferimento(String documentoRiferimento) {
		this.documentoRiferimento = documentoRiferimento;
	}

	public String getDocumentoTipo() {
		return documentoTipo;
	}

	public void setDocumentoTipo(String documentoTipo) {
		this.documentoTipo = documentoTipo;
	}

	public String getNumeroLista() {
		return numeroLista;
	}

	public void setNumeroLista(String numeroLista) {
		this.numeroLista = numeroLista;
	}

	public String getRiferimento() {
		return riferimento;
	}

	public void setRiferimento(String riferimento) {
		this.riferimento = riferimento;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Date getDataOrdine() {
		return dataOrdine;
	}

	public void setDataOrdine(Date dataOrdine) {
		this.dataOrdine = dataOrdine;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Date getDataConsegna() {
		return dataConsegna;
	}

	public void setDataConsegna(Date dataConsegna) {
		this.dataConsegna = dataConsegna;
	}

	public int getMittente() {
		return mittente;
	}

	public void setMittente(int mittente) {
		this.mittente = mittente;
	}

	public int getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(int destinatario) {
		this.destinatario = destinatario;
	}

	public String getRagioneSocialeDestinatario() {
		return ragioneSocialeDestinatario;
	}

	public void setRagioneSocialeDestinatario(String ragioneSocialeDestinatario) {
		this.ragioneSocialeDestinatario = ragioneSocialeDestinatario;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
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

	public int getQuantitaOrdinataTotale() {
		return quantitaOrdinataTotale;
	}

	public void setQuantitaOrdinataTotale(int quantitaOrdinataTotale) {
		this.quantitaOrdinataTotale = quantitaOrdinataTotale;
	}

	public int getQuantitaAssegnataTotale() {
		return quantitaAssegnataTotale;
	}

	public void setQuantitaAssegnataTotale(int quantitaAssegnataTotale) {
		this.quantitaAssegnataTotale = quantitaAssegnataTotale;
	}

	public int getQuantitaImballataTotale() {
		return quantitaImballataTotale;
	}

	public void setQuantitaImballataTotale(int quantitaImballataTotale) {
		this.quantitaImballataTotale = quantitaImballataTotale;
	}

	public int getQuantitaPrelevataTotale() {
		return quantitaPrelevataTotale;
	}

	public void setQuantitaPrelevataTotale(int quantitaPrelevataTotale) {
		this.quantitaPrelevataTotale = quantitaPrelevataTotale;
	}

	public int getPriorita() {
		return priorita;
	}

	public void setPriorita(int priorita) {
		this.priorita = priorita;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getDa() {
		return da;
	}

	public void setDa(Date da) {
		this.da = da;
	}

	public Date getA() {
		return a;
	}

	public void setA(Date a) {
		this.a = a;
	}
	

}
