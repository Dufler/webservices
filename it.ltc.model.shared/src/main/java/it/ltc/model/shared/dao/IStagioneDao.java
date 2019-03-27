package it.ltc.model.shared.dao;

import java.util.List;

import it.ltc.model.shared.json.interno.StagioneJSON;

public interface IStagioneDao {
	
	public StagioneJSON trovaDaID(String codice);
	
	public List<StagioneJSON> trovaTutte();

}
