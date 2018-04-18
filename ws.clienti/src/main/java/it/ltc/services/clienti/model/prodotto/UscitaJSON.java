package it.ltc.services.clienti.model.prodotto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
public class UscitaJSON {
	
	private int id;
	private Date dataOrdine;
	@JsonInclude(value=Include.NON_NULL)
	private Date dataConsegna;
	private String tipo;
	private Integer priorita;
	@JsonInclude(value=Include.NON_NULL)
	private String note;
	private String riferimentoOrdine;
	//private String riferimentoDocumentoFiscale;
	private String stato;
	@JsonInclude(value=Include.NON_NULL)
	private IndirizzoJSON destinatario;
	@JsonInclude(value=Include.NON_NULL)
	private IndirizzoJSON mittente;
//	private String corriere;
//	private String servizioCorriere;
//	private String codiceCorriere;
//	@JsonInclude(value=Include.NON_NULL)
//	private ContrassegnoJSON contrassegno;
//	private AssicurazioneJSON assicurazione;
//	private ParticolaritaJSON particolarita;
	//private List<UscitaDettaglioJSON> prodotti;
//	@JsonInclude(value=Include.NON_NULL)
//	private Double valoreDoganale;
	@JsonInclude(value=Include.NON_NULL)
	private String codiceTracking;
	private int pezziOrdinati;
	private int pezziImballati;
//	@JsonInclude(value=Include.NON_NULL)
//	private DocumentoJSON documentoFiscale;
	
	public UscitaJSON() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDataOrdine() {
		return dataOrdine;
	}

	public void setDataOrdine(Date dataOrdine) {
		this.dataOrdine = dataOrdine;
	}

	public Date getDataConsegna() {
		return dataConsegna;
	}

	public void setDataConsegna(Date dataConsegna) {
		this.dataConsegna = dataConsegna;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getPriorita() {
		return priorita;
	}

	public void setPriorita(Integer priorita) {
		this.priorita = priorita;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getRiferimentoOrdine() {
		return riferimentoOrdine;
	}

	public void setRiferimentoOrdine(String riferimentoOrdine) {
		this.riferimentoOrdine = riferimentoOrdine;
	}

//	public String getRiferimentoDocumentoFiscale() {
//		return riferimentoDocumentoFiscale;
//	}
//
//	public void setRiferimentoDocumentoFiscale(String riferimentoDocumentoFiscale) {
//		this.riferimentoDocumentoFiscale = riferimentoDocumentoFiscale;
//	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public IndirizzoJSON getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(IndirizzoJSON destinatario) {
		this.destinatario = destinatario;
	}

	public IndirizzoJSON getMittente() {
		return mittente;
	}

	public void setMittente(IndirizzoJSON mittente) {
		this.mittente = mittente;
	}

//	public String getCorriere() {
//		return corriere;
//	}
//
//	public void setCorriere(String corriere) {
//		this.corriere = corriere;
//	}
//
//	public String getServizioCorriere() {
//		return servizioCorriere;
//	}
//
//	public void setServizioCorriere(String servizioCorriere) {
//		this.servizioCorriere = servizioCorriere;
//	}
//
//	public String getCodiceCorriere() {
//		return codiceCorriere;
//	}
//
//	public void setCodiceCorriere(String codiceCorriere) {
//		this.codiceCorriere = codiceCorriere;
//	}
//
//	public ContrassegnoJSON getContrassegno() {
//		return contrassegno;
//	}
//
//	public void setContrassegno(ContrassegnoJSON contrassegno) {
//		this.contrassegno = contrassegno;
//	}

//	public AssicurazioneJSON getAssicurazione() {
//		return assicurazione;
//	}
//
//	public void setAssicurazione(AssicurazioneJSON assicurazione) {
//		this.assicurazione = assicurazione;
//	}
//
//	public ParticolaritaJSON getParticolarita() {
//		return particolarita;
//	}
//
//	public void setParticolarita(ParticolaritaJSON particolarita) {
//		this.particolarita = particolarita;
//	}

//	public List<UscitaDettaglioJSON> getProdotti() {
//		return prodotti;
//	}
//
//	public void setProdotti(List<UscitaDettaglioJSON> prodotti) {
//		this.prodotti = prodotti;
//	}

//	public Double getValoreDoganale() {
//		return valoreDoganale;
//	}
//
//	public void setValoreDoganale(Double valoreDoganale) {
//		this.valoreDoganale = valoreDoganale;
//	}

	public String getCodiceTracking() {
		return codiceTracking;
	}

	public void setCodiceTracking(String codiceTracking) {
		this.codiceTracking = codiceTracking;
	}

//	public DocumentoJSON getDocumentoOrdine() {
//		return documentoOrdine;
//	}
//
//	public void setDocumentoOrdine(DocumentoJSON documentoOrdine) {
//		this.documentoOrdine = documentoOrdine;
//	}

//	public DocumentoJSON getDocumentoFiscale() {
//		return documentoFiscale;
//	}
//
//	public void setDocumentoFiscale(DocumentoJSON documentoFiscale) {
//		this.documentoFiscale = documentoFiscale;
//	}

	public int getPezziOrdinati() {
		return pezziOrdinati;
	}

	public void setPezziOrdinati(int pezziOrdinati) {
		this.pezziOrdinati = pezziOrdinati;
	}

	public int getPezziImballati() {
		return pezziImballati;
	}

	public void setPezziImballati(int pezziImballati) {
		this.pezziImballati = pezziImballati;
	}

}
