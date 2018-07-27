package it.ltc.model.shared.json.cliente;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SpedizioneJSON {
	
	private List<String> riferimenti;
	private String corriere;
	private String servizioCorriere;
	private String codiceCorriere;
	private Date dataConsegna;
	private String note;
	@JsonInclude(value=Include.NON_NULL)
	private ContrassegnoJSON contrassegno;
	@JsonInclude(value=Include.NON_NULL)
	private Double valoreDoganale;
	@JsonInclude(value=Include.NON_NULL)
	private DocumentoJSON documentoFiscale;
	
	/**
	 * Se viene indicato 'true' per questo campo viene saltato il controllo sulla corrispondenza tra i destinatari nel caso di raggruppamento di ordini.
	 */
	private boolean forzaAccoppiamentoDestinatari;
	
	public SpedizioneJSON() {}

	public List<String> getRiferimenti() {
		return riferimenti;
	}

	public void setRiferimenti(List<String> riferimenti) {
		this.riferimenti = riferimenti;
	}

	public String getCorriere() {
		return corriere;
	}

	public void setCorriere(String corriere) {
		this.corriere = corriere;
	}

	public String getServizioCorriere() {
		return servizioCorriere;
	}

	public void setServizioCorriere(String servizioCorriere) {
		this.servizioCorriere = servizioCorriere;
	}

	public String getCodiceCorriere() {
		return codiceCorriere;
	}

	public void setCodiceCorriere(String codiceCorriere) {
		this.codiceCorriere = codiceCorriere;
	}

	public Date getDataConsegna() {
		return dataConsegna;
	}

	public void setDataConsegna(Date dataConsegna) {
		this.dataConsegna = dataConsegna;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public ContrassegnoJSON getContrassegno() {
		return contrassegno;
	}

	public void setContrassegno(ContrassegnoJSON contrassegno) {
		this.contrassegno = contrassegno;
	}

	public Double getValoreDoganale() {
		return valoreDoganale;
	}

	public void setValoreDoganale(Double valoreDoganale) {
		this.valoreDoganale = valoreDoganale;
	}

	public DocumentoJSON getDocumentoFiscale() {
		return documentoFiscale;
	}

	public void setDocumentoFiscale(DocumentoJSON documentoFiscale) {
		this.documentoFiscale = documentoFiscale;
	}

	public boolean isForzaAccoppiamentoDestinatari() {
		return forzaAccoppiamentoDestinatari;
	}

	public void setForzaAccoppiamentoDestinatari(boolean forzaAccoppiamentoDestinatari) {
		this.forzaAccoppiamentoDestinatari = forzaAccoppiamentoDestinatari;
	}

	@Override
	public String toString() {
		String listaRiferimenti = "( ";
		for (String riferimento : riferimenti)
			listaRiferimenti += riferimento + " ";
		listaRiferimenti += ")";
		String valoreContrassegno = contrassegno != null ? contrassegno.getValuta() : "null";
		String documento = documentoFiscale != null ? documentoFiscale.getRiferimento() : "null";
		return "SpedizioneJSON [riferimenti=" + listaRiferimenti + ", corriere=" + corriere + ", servizioCorriere=" + servizioCorriere + ", codiceCorriere=" + codiceCorriere + ", dataConsegna=" + dataConsegna + ", note=" + note + ", contrassegno=" + valoreContrassegno + ", valoreDoganale="
				+ valoreDoganale + ", documentoFiscale=" + documento + ", forzaAccoppiamentoDestinatari=" + forzaAccoppiamentoDestinatari + "]";
	}

}
