package it.ltc.services.logica.data.crm;

import java.util.List;

import it.ltc.database.model.centrale.Brand;

public interface BrandDAO {
	
	public List<Brand> trovaTutti();
	
	public List<Brand> trovaDaAzienda(int idAzienda);
	
	public List<Brand> cercaDaNome(String nome);
	
	public Brand trova(int id);
	
	public Brand inserisci(Brand brand);
	
	public Brand aggiorna(Brand brand);
	
	public Brand elimina(Brand brand);

}
