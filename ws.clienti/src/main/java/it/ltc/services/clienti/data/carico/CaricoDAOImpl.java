package it.ltc.services.clienti.data.carico;

import java.util.List;

import it.ltc.database.dao.Dao;
import it.ltc.database.model.commessa.Ingresso;
import it.ltc.database.model.commessa.IngressoDettaglio;
import it.ltc.services.clienti.model.prodotto.CaricoJSON;
import it.ltc.services.clienti.model.prodotto.IngressoDettaglioJSON;
import it.ltc.services.clienti.model.prodotto.IngressoJSON;
import it.ltc.services.clienti.model.prodotto.ModificaCaricoJSON;

public class CaricoDAOImpl extends Dao implements CaricoDAO<Ingresso, IngressoDettaglio> {

	public CaricoDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
	}

	@Override
	public CaricoJSON trovaDaID(int idCarico, boolean dettagliato) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public CaricoJSON trovaDaRiferimento(String riferimento, boolean dettagliato) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IngressoJSON> trovaTutti() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean inserisci(CaricoJSON carico) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean inserisciDettaglio(IngressoDettaglioJSON carico) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean aggiorna(IngressoJSON carico) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean elimina(IngressoJSON carico) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Ingresso deserializzaIngresso(CaricoJSON json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IngressoDettaglio> deserializzaDettagli(CaricoJSON json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CaricoJSON serializzaCarico(Ingresso carico, List<IngressoDettaglio> dettagli) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean aggiornaDettaglio(IngressoDettaglioJSON carico) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean eliminaDettaglio(IngressoDettaglioJSON carico) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IngressoJSON serializzaIngresso(Ingresso carico) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IngressoDettaglioJSON serializzaDettaglio(IngressoDettaglio dettaglio) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean modificaCaricoDiTest(ModificaCaricoJSON modifiche) {
		// TODO Auto-generated method stub
		return false;
	}

}
