package it.ltc.services.logica.data.common;

import java.util.List;

import it.ltc.database.model.centrale.Cliente;

public interface ClientiDAO {
	
	public List<Cliente> trovaTutti();
	
	public Cliente trova(int id);
	
	public Cliente inserisci(Cliente cliente);
	
	public Cliente aggiorna(Cliente cliente);
	
	public Cliente elimina(Cliente cliente);

}
