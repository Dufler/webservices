package it.ltc.model.shared.json.cliente;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
public class OrdineImballatoJSON {
	
	private UscitaJSON ordine;
	@JsonInclude(value=Include.NON_EMPTY)
	private List<ImballoJSON> imballi;
	
	public OrdineImballatoJSON() {}

	public UscitaJSON getOrdine() {
		return ordine;
	}

	public void setOrdine(UscitaJSON ordine) {
		this.ordine = ordine;
	}

	public List<ImballoJSON> getImballi() {
		return imballi;
	}

	public void setImballi(List<ImballoJSON> imballi) {
		this.imballi = imballi;
	}

	@Override
	public String toString() {
		return "OrdineImballatoJSON [ordine=" + ordine + ", imballi=" + imballi + "]";
	}

}
