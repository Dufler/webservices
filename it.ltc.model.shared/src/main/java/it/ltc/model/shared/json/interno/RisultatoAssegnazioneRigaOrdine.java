package it.ltc.model.shared.json.interno;

/**
 * Questa classe modella un JSON che contiene le informazioni sull'assegnazione di una riga d'ordine.
 * @author Damiano
 *
 */
public class RisultatoAssegnazioneRigaOrdine {
	
	public enum StatoAssegnazioneRiga { PRELIEVO, SCORTA, NONUBICATO, NONPRESENTE, LEGACY } //FIXME NONPRESENTE può essere splittato su NONDISPONIBILE (disponibilità magasd <= 0), MAIARRIVATO (magasd non presente) e ANOMALO (magasd disponibile ma nessun collipack)
	
	/**
	 * ID di righiordine sui sistemi legacy
	 */
	private int idRigaOrdine;
	private int numeroRiga;
	private int quantitaRichiesta;
	private int quantitaAssegnata;
	
	/**
	 * ID di righiubicpre sui sistemi legacy
	 */
	private int idRigaAssegnazione;
	
	private StatoAssegnazioneRiga stato;
	
	private int quantita;
	private int totalePezzi;
	private String ubicazione;
	private String collo;	
	
	private int idProdotto;
	private String sku;
	private String taglia;
	private String descrizione;
	
	private String anomalie;
	
	public RisultatoAssegnazioneRigaOrdine() {}

	public int getIdRigaOrdine() {
		return idRigaOrdine;
	}

	public void setIdRigaOrdine(int idRigaOrdine) {
		this.idRigaOrdine = idRigaOrdine;
	}

	public int getNumeroRiga() {
		return numeroRiga;
	}

	public void setNumeroRiga(int numeroRiga) {
		this.numeroRiga = numeroRiga;
	}

	public int getQuantitaRichiesta() {
		return quantitaRichiesta;
	}

	public void setQuantitaRichiesta(int quantitaRichiesta) {
		this.quantitaRichiesta = quantitaRichiesta;
	}

	public int getQuantitaAssegnata() {
		return quantitaAssegnata;
	}

	public void setQuantitaAssegnata(int quantitaAssegnata) {
		this.quantitaAssegnata = quantitaAssegnata;
	}

	public int getIdRigaAssegnazione() {
		return idRigaAssegnazione;
	}

	public void setIdRigaAssegnazione(int idRigaAssegnazione) {
		this.idRigaAssegnazione = idRigaAssegnazione;
	}

	public StatoAssegnazioneRiga getStato() {
		return stato;
	}

	public void setStato(StatoAssegnazioneRiga stato) {
		this.stato = stato;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	public int getTotalePezzi() {
		return totalePezzi;
	}

	public void setTotalePezzi(int totalePezzi) {
		this.totalePezzi = totalePezzi;
	}

	public String getUbicazione() {
		return ubicazione;
	}

	public void setUbicazione(String ubicazione) {
		this.ubicazione = ubicazione;
	}

	public String getCollo() {
		return collo;
	}

	public void setCollo(String collo) {
		this.collo = collo;
	}

	public int getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(int idProdotto) {
		this.idProdotto = idProdotto;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getTaglia() {
		return taglia;
	}

	public void setTaglia(String taglia) {
		this.taglia = taglia;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getAnomalie() {
		return anomalie;
	}

	public void setAnomalie(String anomalie) {
		this.anomalie = anomalie;
	}

}
