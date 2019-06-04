package it.ltc.model.shared.json.cliente;

import java.util.List;

public class CassaJSON {
	
	private int idCassa;
	private TipoCassa tipo;
	private String modello;
	private String codiceCassa;
	private List<ElementoCassaJSON> prodotti;
	
	public CassaJSON() {}

	public int getIdCassa() {
		return idCassa;
	}

	public void setIdCassa(int idCassa) {
		this.idCassa = idCassa;
	}

	public TipoCassa getTipo() {
		return tipo;
	}

	public void setTipo(TipoCassa tipo) {
		this.tipo = tipo;
	}

	public String getModello() {
		return modello;
	}

	public void setModello(String modello) {
		this.modello = modello;
	}

	public String getCodiceCassa() {
		return codiceCassa;
	}

	public void setCodiceCassa(String codiceCassa) {
		this.codiceCassa = codiceCassa;
	}

	public List<ElementoCassaJSON> getProdotti() {
		return prodotti;
	}

	public void setProdotti(List<ElementoCassaJSON> prodotti) {
		this.prodotti = prodotti;
	}

}
