package it.ltc.model.shared.dao;

import java.util.List;

import it.ltc.model.shared.json.interno.CategoriaMerceologicaJSON;

public interface ICategoriaMerceologicaDao {
	
	public CategoriaMerceologicaJSON trovaDaNome(String nome);
	
	public List<CategoriaMerceologicaJSON> trovaTutte();

}
