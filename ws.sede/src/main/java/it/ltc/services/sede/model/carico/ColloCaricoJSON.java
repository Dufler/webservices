package it.ltc.services.sede.model.carico;

public class ColloCaricoJSON {
	
	private int id;
	private int carico;
	private String etichetta;
	private String magazzino;
	private String barcodeCliente;
	
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

}
