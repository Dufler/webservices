package it.ltc.services.logica.data.crm;

import java.util.List;

import it.ltc.database.model.centrale.CrmTagCategoriaMerceologica;

public interface TagCategorieDAO {

	public List<CrmTagCategoriaMerceologica> trovaTutti();
	
	public List<CrmTagCategoriaMerceologica> trovaDaAzienda(int idAzienda);
	
	public List<CrmTagCategoriaMerceologica> trovaDaTag(String tag);
	
	public CrmTagCategoriaMerceologica trova(int idAzienda, String tag);
	
	public CrmTagCategoriaMerceologica inserisci(CrmTagCategoriaMerceologica tag);
	
	public CrmTagCategoriaMerceologica aggiorna(CrmTagCategoriaMerceologica tag);
	
	public CrmTagCategoriaMerceologica elimina(CrmTagCategoriaMerceologica tag);

}
