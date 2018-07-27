package it.ltc.model.shared.json.cliente;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
public class OrdineJSON {
	
	private UscitaJSON ordine;
	@JsonInclude(value=Include.NON_EMPTY)
	private List<UscitaDettaglioJSON> dettagli;
	@JsonInclude(value=Include.NON_NULL)
	private DocumentoJSON documento;
	
	public OrdineJSON() {}

	public UscitaJSON getOrdine() {
		return ordine;
	}

	public void setOrdine(UscitaJSON ordine) {
		this.ordine = ordine;
	}

	public List<UscitaDettaglioJSON> getDettagli() {
		return dettagli;
	}

	public void setDettagli(List<UscitaDettaglioJSON> dettagli) {
		this.dettagli = dettagli;
	}
	
	public DocumentoJSON getDocumento() {
		return documento;
	}

	public void setDocumento(DocumentoJSON documento) {
		this.documento = documento;
	}

	@Override
	public String toString() {
		String size = dettagli != null ? Integer.toString(dettagli.size()) : "null";
		String riferimento = ordine != null ? ordine.getRiferimentoOrdine() : "null";
		return "OrdineJSON [ordine=" + riferimento + ", dettagli=" + size + "]";
	}

}
