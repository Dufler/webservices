package it.ltc.services.sede.model.carico;

public class ColloCaricoJSON {
	
	private int id;
	private int carico;
	private String collo;
	private String etichetta;
	private String magazzino;
	private String barcodeCliente;
	private String operatoreCreazione;
	private String operatoreUbicazione;
	/**
	 * APERTO, CHIUSO, o UBICATO
	 */
	private String stato;
	private String ubicazione;
	
	public ColloCaricoJSON() {}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCarico() {
		return carico;
	}

	public void setCarico(int carico) {
		this.carico = carico;
	}

	public String getCollo() {
		return collo;
	}

	public void setCollo(String collo) {
		this.collo = collo;
	}

	public String getEtichetta() {
		return etichetta;
	}

	public void setEtichetta(String etichetta) {
		this.etichetta = etichetta;
	}

	public String getMagazzino() {
		return magazzino;
	}

	public void setMagazzino(String magazzino) {
		this.magazzino = magazzino;
	}

	public String getBarcodeCliente() {
		return barcodeCliente;
	}

	public void setBarcodeCliente(String barcodeCliente) {
		this.barcodeCliente = barcodeCliente;
	}

	public String getOperatoreCreazione() {
		return operatoreCreazione;
	}

	public void setOperatoreCreazione(String operatoreCreazione) {
		this.operatoreCreazione = operatoreCreazione;
	}

	public String getOperatoreUbicazione() {
		return operatoreUbicazione;
	}

	public void setOperatoreUbicazione(String operatoreUbicazione) {
		this.operatoreUbicazione = operatoreUbicazione;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getUbicazione() {
		return ubicazione;
	}

	public void setUbicazione(String ubicazione) {
		this.ubicazione = ubicazione;
	}

}
