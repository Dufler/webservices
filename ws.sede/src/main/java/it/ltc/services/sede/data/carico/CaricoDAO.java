package it.ltc.services.sede.data.carico;

import java.util.List;

import it.ltc.model.shared.json.cliente.CaricoJSON;
import it.ltc.model.shared.json.cliente.IngressoJSON;

public interface CaricoDAO {
	
	public CaricoJSON trovaDaID(int idCarico, boolean dettagliato);
	
	public List<IngressoJSON> trovaTutti(IngressoJSON filtro);
	
	public CaricoJSON nuovoCarico(CaricoJSON carico);
	
	public CaricoJSON aggiornaCarico(CaricoJSON carico);
	
	public CaricoJSON eliminaCarico(CaricoJSON carico);

}
