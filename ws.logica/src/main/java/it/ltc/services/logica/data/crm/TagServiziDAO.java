package it.ltc.services.logica.data.crm;

import java.util.List;

import it.ltc.database.model.centrale.CrmTagServiziRichiesti;

public interface TagServiziDAO {
	
	public List<CrmTagServiziRichiesti> trovaTutti();
	
	public List<CrmTagServiziRichiesti> trovaDaAzienda(int idAzienda);
	
	public List<CrmTagServiziRichiesti> trovaDaTag(String tag);
	
	public CrmTagServiziRichiesti trova(int idAzienda, String tag);
	
	public CrmTagServiziRichiesti inserisci(CrmTagServiziRichiesti tag);
	
	public CrmTagServiziRichiesti aggiorna(CrmTagServiziRichiesti tag);
	
	public CrmTagServiziRichiesti elimina(CrmTagServiziRichiesti tag);

}
