package it.ltc.services.logica.data.crm;

import java.util.List;

import it.ltc.database.model.centrale.AziendaBrand;

public interface AssociazioneAziendaBrandDAO {
	
	public List<AziendaBrand> trovaTutti();
	
	public List<AziendaBrand> trovaDaAzienda(int idAzienda);
	
	public List<AziendaBrand> trovaDaBrand(int idBrand);
	
	public AziendaBrand trova(int idAzienda, int idBrand);
	
	public AziendaBrand inserisci(AziendaBrand associazione);
	
	public AziendaBrand aggiorna(AziendaBrand associazione);
	
	public AziendaBrand elimina(AziendaBrand associazione);

}
