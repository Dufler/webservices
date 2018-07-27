package it.ltc.model.shared.dao;

import java.util.List;

import it.ltc.model.shared.json.interno.CaricoDettaglio;

public interface ICaricoDettaglioDao {

	public CaricoDettaglio inserisci(CaricoDettaglio json);
	
	public CaricoDettaglio aggiorna(CaricoDettaglio json);
	
	public CaricoDettaglio elimina(CaricoDettaglio json);
	
	public List<CaricoDettaglio> trovaDettagli(int idCarico);
}
