package it.ltc.model.shared.json.interno.ordine;

import java.util.Date;
import java.util.List;

/**
 * Classe che mappa il JSON che contiene tutte le info su una spedizione.
 * @author Damiano
 *
 */
public class DatiSpedizione {
	
	private int id;
	
	private String note;
	private List<OrdineTestata> ordini;
	
	private int pezzi;
	private int colli;
	private double peso;
	private double volume;
	
	private String corriere;
	private String codiceCorriere;
	private String servizioCorriere;
	
	private Double valoreContrassegno;
	private String tipoContrassegno;
	private String valutaContrassegno;
	
	private Double valoreDoganale;
	private String codiceTracking;
	
	private String riferimentoDocumento;
	private Date dataDocumento;
	private String tipoDocumento;
	
	private Date dataConsegna;
	
	private boolean forzaAccoppiamentoDestinatari;
	
//	private boolean abilitaPartenza;
	
	private String stato;
	
	//Vengono usati solo come criteri di filtraggio durante la ricerca.
	private Date da;
	private Date a;
	
	//campi indirizzo
	private String riferimento;
	private String ragioneSociale;
	private String indirizzo;
	private String localita;
	private String provincia;
	private String cap;
	private String nazione;
	
	public DatiSpedizione() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<OrdineTestata> getOrdini() {
		return ordini;
	}

	public void setOrdini(List<OrdineTestata> ordini) {
		this.ordini = ordini;
	}

	public int getPezzi() {
		return pezzi;
	}

	public void setPezzi(int pezzi) {
		this.pezzi = pezzi;
	}

	public int getColli() {
		return colli;
	}

	public void setColli(int colli) {
		this.colli = colli;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public String getCorriere() {
		return corriere;
	}

	public void setCorriere(String corriere) {
		this.corriere = corriere;
	}

	public String getCodiceCorriere() {
		return codiceCorriere;
	}

	public void setCodiceCorriere(String codiceCorriere) {
		this.codiceCorriere = codiceCorriere;
	}

	public String getServizioCorriere() {
		return servizioCorriere;
	}

	public void setServizioCorriere(String servizioCorriere) {
		this.servizioCorriere = servizioCorriere;
	}

	public Double getValoreContrassegno() {
		return valoreContrassegno;
	}

	public void setValoreContrassegno(Double valoreContrassegno) {
		this.valoreContrassegno = valoreContrassegno;
	}

	public String getTipoContrassegno() {
		return tipoContrassegno;
	}

	public void setTipoContrassegno(String tipoContrassegno) {
		this.tipoContrassegno = tipoContrassegno;
	}

	public String getValutaContrassegno() {
		return valutaContrassegno;
	}

	public void setValutaContrassegno(String valutaContrassegno) {
		this.valutaContrassegno = valutaContrassegno;
	}

	public Double getValoreDoganale() {
		return valoreDoganale;
	}

	public void setValoreDoganale(Double valoreDoganale) {
		this.valoreDoganale = valoreDoganale;
	}

	public String getCodiceTracking() {
		return codiceTracking;
	}

	public void setCodiceTracking(String codiceTracking) {
		this.codiceTracking = codiceTracking;
	}

	public String getRiferimentoDocumento() {
		return riferimentoDocumento;
	}

	public void setRiferimentoDocumento(String riferimentoDocumento) {
		this.riferimentoDocumento = riferimentoDocumento;
	}

	public Date getDataDocumento() {
		return dataDocumento;
	}

	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public Date getDataConsegna() {
		return dataConsegna;
	}

	public void setDataConsegna(Date dataConsegna) {
		this.dataConsegna = dataConsegna;
	}

	public boolean isForzaAccoppiamentoDestinatari() {
		return forzaAccoppiamentoDestinatari;
	}

	public void setForzaAccoppiamentoDestinatari(boolean forzaAccoppiamentoDestinatari) {
		this.forzaAccoppiamentoDestinatari = forzaAccoppiamentoDestinatari;
	}

//	public boolean isAbilitaPartenza() {
//		return abilitaPartenza;
//	}
//
//	public void setAbilitaPartenza(boolean abilitaPartenza) {
//		this.abilitaPartenza = abilitaPartenza;
//	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public Date getDa() {
		return da;
	}

	public void setDa(Date da) {
		this.da = da;
	}

	public Date getA() {
		return a;
	}

	public void setA(Date a) {
		this.a = a;
	}

	public String getRiferimento() {
		return riferimento;
	}

	public void setRiferimento(String riferimento) {
		this.riferimento = riferimento;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getLocalita() {
		return localita;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getNazione() {
		return nazione;
	}

	public void setNazione(String nazione) {
		this.nazione = nazione;
	}

}
