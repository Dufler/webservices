package it.ltc.model.shared.json.interno;

/**
 * Classe che modella il problema che potrebbe insorgere per una riga d'ordine quando si tenta di finalizzarlo e impegnare la merce che potrebbe non essere disponibile. 
 * @author Damiano
 *
 */
public class ProblemaFinalizzazioneOrdine {
	
	private int idOrdine;
	private int idRiga;
	private int numeroRiga;
	
	private int idProdotto;
	private String sku;
	private String taglia;
	private String descrizione;
	
	private int quantitaRichiesta;
	private int quantitaDisponibile;
	
	public ProblemaFinalizzazioneOrdine() {}

	public int getIdOrdine() {
		return idOrdine;
	}

	public void setIdOrdine(int idOrdine) {
		this.idOrdine = idOrdine;
	}

	public int getIdRiga() {
		return idRiga;
	}

	public void setIdRiga(int idRiga) {
		this.idRiga = idRiga;
	}

	public int getNumeroRiga() {
		return numeroRiga;
	}

	public void setNumeroRiga(int numeroRiga) {
		this.numeroRiga = numeroRiga;
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

	public int getQuantitaRichiesta() {
		return quantitaRichiesta;
	}

	public void setQuantitaRichiesta(int quantitaRichiesta) {
		this.quantitaRichiesta = quantitaRichiesta;
	}

	public int getQuantitaDisponibile() {
		return quantitaDisponibile;
	}

	public void setQuantitaDisponibile(int quantitaDisponibile) {
		this.quantitaDisponibile = quantitaDisponibile;
	}

}
