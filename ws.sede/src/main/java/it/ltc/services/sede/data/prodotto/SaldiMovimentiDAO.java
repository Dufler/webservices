package it.ltc.services.sede.data.prodotto;

import java.util.List;

import it.ltc.model.shared.json.interno.MovimentoProdotto;
import it.ltc.model.shared.json.interno.SaldoProdotto;

public interface SaldiMovimentiDAO {
	
	public void setUtente(String utente);
	
	public List<SaldoProdotto> trovaSaldi(int idProdotto);
	
	public List<MovimentoProdotto> trovaMovimenti(int idProdotto);
	
	public MovimentoProdotto inserisci(MovimentoProdotto movimento);
	
	public MovimentoProdotto elimina(MovimentoProdotto movimento);

}
