package it.ltc.services.sede.data.operatore;

import java.util.List;

import it.ltc.database.model.sede.AttivitaTipo;

public interface TipoAttivitaDAO {
	
	public AttivitaTipo trovaDaID(int id);
	
	public List<AttivitaTipo> trovaTutti();

}
