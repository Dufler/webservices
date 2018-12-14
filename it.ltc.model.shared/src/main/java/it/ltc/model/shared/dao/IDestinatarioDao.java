package it.ltc.model.shared.dao;

import java.util.List;

import it.ltc.model.shared.json.cliente.IndirizzoJSON;

/**
 * Dao per cercare e trovare destinatari.
 * @author Damiano
 *
 */
public interface IDestinatarioDao {
	
	public IndirizzoJSON trovaPerID(int id);
	
	public List<IndirizzoJSON> cerca(IndirizzoJSON filtro);
	
	public IndirizzoJSON inserisci(IndirizzoJSON destinatario);
	
	public IndirizzoJSON aggiorna(IndirizzoJSON destinatario);
	
	public IndirizzoJSON elimina(IndirizzoJSON destinatario);

}
