package it.ltc.model.shared.json.cliente;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ContrassegnoJSON {
	
	private String tipo;
	private Double valore;
	private String valuta;
	
	public ContrassegnoJSON() {}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Double getValore() {
		return valore;
	}

	public void setValore(Double valore) {
		this.valore = valore;
	}

	public String getValuta() {
		return valuta;
	}

	public void setValuta(String valuta) {
		this.valuta = valuta;
	}

	@Override
	public String toString() {
		return "ContrassegnoJSON [tipo=" + tipo + ", valore=" + valore + ", valuta=" + valuta + "]";
	}

}
