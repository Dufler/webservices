package it.ltc.services.sede.model.carico;

import java.util.List;

/**
 * Rappresenta un collo riscontrato in ingresso e il suo contenuto.
 * Il carico identifica la testata presente a sistema.
 * L'ID e l'etichetta vengono deteriminati dal sistema in fase di inserimento.
 * @author Damiano
 *
 */
@Deprecated
public class ColloInRiscontroJSON {
	
	private int id;
	private int carico;
	private String etichetta;
	private String magazzino;
	private String barcodeCliente;
	private List<ProdottoInRiscontroJSON> prodotti;
	
	public ColloInRiscontroJSON() {}

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

	public List<ProdottoInRiscontroJSON> getProdotti() {
		return prodotti;
	}

	public void setProdotti(List<ProdottoInRiscontroJSON> prodotti) {
		this.prodotti = prodotti;
	}

}
