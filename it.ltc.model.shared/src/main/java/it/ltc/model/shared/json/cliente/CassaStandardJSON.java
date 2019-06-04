package it.ltc.model.shared.json.cliente;

import java.util.List;

public class CassaStandardJSON {
	
	private String codiceCassa;
	private List<ElementoCassaStandardJSON> elementi;
	
	public CassaStandardJSON() {}

	public String getCodiceCassa() {
		return codiceCassa;
	}

	public void setCodiceCassa(String codiceCassa) {
		this.codiceCassa = codiceCassa;
	}

	public List<ElementoCassaStandardJSON> getElementi() {
		return elementi;
	}

	public void setElementi(List<ElementoCassaStandardJSON> elementi) {
		this.elementi = elementi;
	}

}
