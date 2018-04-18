package it.ltc.services.clienti.data.magazzino;

import java.util.List;

public interface MagazzinoDAO<T> {
	
	T findByID(int idMagazzino);
	
	T findByCodiceLTC(String codice);
	
	T findByCodificaCliente(String codifica);
	
	List<T> findAll();

}
