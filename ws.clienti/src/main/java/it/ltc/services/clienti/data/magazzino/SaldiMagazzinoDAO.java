package it.ltc.services.clienti.data.magazzino;

import java.util.List;

import it.ltc.services.clienti.model.prodotto.InfoProdotto;

public interface SaldiMagazzinoDAO {

	public List<InfoProdotto> getDisponibilita();
	
	public List<InfoProdotto> getDisponibilita(String codificaMagazzino);
	
}
