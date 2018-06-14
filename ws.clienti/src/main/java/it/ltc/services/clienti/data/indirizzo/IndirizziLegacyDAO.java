package it.ltc.services.clienti.data.indirizzo;

import it.ltc.database.model.legacy.Destinatari;
import it.ltc.database.model.legacy.MittentiOrdine;
import it.ltc.model.shared.json.cliente.IndirizzoJSON;

public interface IndirizziLegacyDAO {
	
	public MittentiOrdine trovaMittente(int id);
	
	public MittentiOrdine ottieniMittente(IndirizzoJSON mittente);
	
	public Destinatari trovaDestinatario(int id);
	
	public Destinatari ottieniDestinatario(IndirizzoJSON destinatario);

}
