package it.ltc.model.shared.dao;

import java.util.List;

import it.ltc.model.shared.json.cliente.TipoCaricoJSON;

public interface ITipoCaricoDao {
	
	public TipoCaricoJSON trovaDaCodice(String codice);
	
	public List<TipoCaricoJSON> trovaTutti();

}
