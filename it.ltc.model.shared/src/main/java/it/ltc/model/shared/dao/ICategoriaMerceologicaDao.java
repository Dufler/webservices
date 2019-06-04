package it.ltc.model.shared.dao;

import java.util.List;

import it.ltc.model.shared.json.interno.CategoriaMerceologicaJSON;

public interface ICategoriaMerceologicaDao {
	
	public CategoriaMerceologicaJSON trovaDaNome(String nome, int commessa);
	
	public List<CategoriaMerceologicaJSON> trovaTutti();
	
	public CategoriaMerceologicaJSON inserisci(CategoriaMerceologicaJSON categoria);
	
	public CategoriaMerceologicaJSON aggiorna(CategoriaMerceologicaJSON categoria);
	
	public CategoriaMerceologicaJSON elimina(CategoriaMerceologicaJSON categoria);

}
