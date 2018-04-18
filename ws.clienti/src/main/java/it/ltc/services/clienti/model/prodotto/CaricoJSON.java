package it.ltc.services.clienti.model.prodotto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Oggetto che mappa il JSON dei carichi.
 * Contiene le informazioni di testata (ingresso), la lista dei prodotti (dettagli) e il documento.
 * @author Damiano
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CaricoJSON {
	
	private IngressoJSON ingresso;
	@JsonInclude(value=Include.NON_EMPTY)
	private List<IngressoDettaglioJSON> dettagli;
	private DocumentoJSON documento;
	
	public CaricoJSON() {}
	
	public IngressoJSON getIngresso() {
		return ingresso;
	}
	
	public void setIngresso(IngressoJSON ingresso) {
		this.ingresso = ingresso;
	}
	
	public List<IngressoDettaglioJSON> getDettagli() {
		return dettagli;
	}
	
	public void setDettagli(List<IngressoDettaglioJSON> dettagli) {
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
		return "CaricoJSON [ingresso=" + ingresso + ", dettagli=" + size + "]";
	}

}
