package it.ltc.model.shared.dao;

import java.util.List;

import it.ltc.model.shared.json.cliente.IndirizzoJSON;

/**
 * Dao per cercare e trovare il mittente di un ordine.<br>
 * Usualmente c'è ne solo uno per cliente.
 * @author Damiano
 *
 */
public interface IMittenteDao {
	
	public IndirizzoJSON trovaPerID(int id);
	
	public List<IndirizzoJSON> cerca(IndirizzoJSON filtro);
	
	public IndirizzoJSON inserisci(IndirizzoJSON destinatario);
	
	public IndirizzoJSON aggiorna(IndirizzoJSON destinatario);
	
	public IndirizzoJSON elimina(IndirizzoJSON destinatario);

}
