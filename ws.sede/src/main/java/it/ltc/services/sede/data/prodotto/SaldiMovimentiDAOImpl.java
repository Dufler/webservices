package it.ltc.services.sede.data.prodotto;

import java.util.List;

import it.ltc.model.shared.json.interno.MovimentoProdotto;
import it.ltc.model.shared.json.interno.SaldoProdotto;

public class SaldiMovimentiDAOImpl implements SaldiMovimentiDAO {
	
	protected String utente;
	
	public SaldiMovimentiDAOImpl(String persistenceUnit) {
		//TODO: estendere un dao.
	}

	@Override
	public List<MovimentoProdotto> trovaMovimenti(int idProdotto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SaldoProdotto> trovaSaldi(int idProdotto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MovimentoProdotto inserisci(MovimentoProdotto movimento) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MovimentoProdotto elimina(MovimentoProdotto movimento) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUtente(String utente) {
		this.utente = utente;		
	}

}
