package it.ltc.model.shared.dao;

import java.util.List;

import it.ltc.model.shared.json.interno.CaricoStato;
import it.ltc.model.shared.json.interno.CaricoTestata;

public interface ICaricoDao {
	
	public CaricoTestata inserisci(CaricoTestata json);
	
	public CaricoTestata aggiorna(CaricoTestata json);
	
	public CaricoTestata elimina(CaricoTestata json);
	
	public CaricoTestata modificaStato(CaricoTestata json);
	
	public CaricoTestata trovaPerID(int id);
	
	public List<CaricoTestata> trovaCorrispondenti(CaricoTestata filtro);
	
	public List<CaricoStato> trovaStati(int idCarico);
	
}
