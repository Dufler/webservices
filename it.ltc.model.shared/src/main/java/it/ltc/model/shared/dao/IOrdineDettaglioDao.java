package it.ltc.model.shared.dao;

import java.util.List;

import it.ltc.model.shared.json.interno.ordine.OrdineDettaglio;

/**
 * Definisce le operazioni che devono essere implementate dal DAO che gestisce le righe d'ordine.
 * @author Damiano
 *
 */
public interface IOrdineDettaglioDao {
	
	public OrdineDettaglio inserisci(OrdineDettaglio json);
	
	public OrdineDettaglio aggiorna(OrdineDettaglio json);
	
	public OrdineDettaglio elimina(OrdineDettaglio json);
	
	public List<OrdineDettaglio> trovaDettagli(int idOrdine);

}
