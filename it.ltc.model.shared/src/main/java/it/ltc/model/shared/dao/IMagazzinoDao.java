package it.ltc.model.shared.dao;

import java.util.List;

import it.ltc.model.shared.json.interno.MagazzinoJSON;

public interface IMagazzinoDao {
	
	public MagazzinoJSON trovaDaLTC(String codifica);
	
	public MagazzinoJSON trovaDaCliente(String codifica);
	
	public List<MagazzinoJSON> trovaliTutti();

}
