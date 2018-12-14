package it.ltc.model.shared.dao;

import java.util.List;

import it.ltc.model.shared.json.cliente.TipoOrdineJSON;

public interface ITipoOrdineDao {
	
	public TipoOrdineJSON trovaDaCodice(String codice);
	
	public List<TipoOrdineJSON> trovaTutti();

}
