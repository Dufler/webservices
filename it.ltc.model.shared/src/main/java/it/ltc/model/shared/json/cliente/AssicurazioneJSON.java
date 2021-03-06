package it.ltc.model.shared.json.cliente;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class AssicurazioneJSON {
	
	private String tipo;
	private Double valore;
	
	public AssicurazioneJSON() {}

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

	@Override
	public String toString() {
		return "AssicurazioneJSON [tipo=" + tipo + ", valore=" + valore + "]";
	}

}
