package it.ltc.model.shared.json.interno;

import java.util.Date;

/**
 * Classe che modella il flusso dati per la testata di un carico.
 * @author Damiano
 *
 */
public class CaricoTestata {
	
	private Date documentoData;
	private String documentoRiferimento;
	private String documentoTipo;
	
	private int id;
	
	private String riferimento;
	private int fornitore;
	private String tipo;
	
	private String stato;
	
	private int quantitaDichiarataTotale;
	private int quantitaRiscontrataTotale;
	
	private String stagione;
	private String note;
	
	private Date dataArrivo;
	private Date dataArrivoPresunto;
	private boolean prodottiEccedenti;
	private boolean prodottiNonDichiarati;
	
	public CaricoTestata() {}

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRiferimento() {
		return riferimento;
	}

	public void setRiferimento(String riferimento) {
		this.riferimento = riferimento;
	}

	public int getFornitore() {
		return fornitore;
	}

	public void setFornitore(int fornitore) {
		this.fornitore = fornitore;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public int getQuantitaDichiarataTotale() {
		return quantitaDichiarataTotale;
	}

	public void setQuantitaDichiarataTotale(int quantitaDichiarataTotale) {
		this.quantitaDichiarataTotale = quantitaDichiarataTotale;
	}

	public int getQuantitaRiscontrataTotale() {
		return quantitaRiscontrataTotale;
	}

	public void setQuantitaRiscontrataTotale(int quantitaRiscontrataTotale) {
		this.quantitaRiscontrataTotale = quantitaRiscontrataTotale;
	}

	public String getStagione() {
		return stagione;
	}

	public void setStagione(String stagione) {
		this.stagione = stagione;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getDataArrivo() {
		return dataArrivo;
	}

	public void setDataArrivo(Date dataArrivo) {
		this.dataArrivo = dataArrivo;
	}

	public Date getDataArrivoPresunto() {
		return dataArrivoPresunto;
	}

	public void setDataArrivoPresunto(Date dataArrivoPresunto) {
		this.dataArrivoPresunto = dataArrivoPresunto;
	}

	public boolean isProdottiEccedenti() {
		return prodottiEccedenti;
	}

	public void setProdottiEccedenti(boolean prodottiEccedenti) {
		this.prodottiEccedenti = prodottiEccedenti;
	}

	public boolean isProdottiNonDichiarati() {
		return prodottiNonDichiarati;
	}

	public void setProdottiNonDichiarati(boolean prodottiNonDichiarati) {
		this.prodottiNonDichiarati = prodottiNonDichiarati;
	}

}
