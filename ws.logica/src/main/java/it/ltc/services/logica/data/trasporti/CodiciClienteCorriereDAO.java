package it.ltc.services.logica.data.trasporti;

import java.util.List;

import it.ltc.database.model.centrale.JoinCommessaCorriere;

public interface CodiciClienteCorriereDAO {
	
	public List<JoinCommessaCorriere> trovaTutti();
	
	public JoinCommessaCorriere trova(String codice);
	
	public JoinCommessaCorriere inserisci(JoinCommessaCorriere codice);
	
	public JoinCommessaCorriere aggiorna(JoinCommessaCorriere codice);
	
	public JoinCommessaCorriere elimina(JoinCommessaCorriere codice);

}
