package it.ltc.services.clienti.data.carico;

import java.util.List;

import it.ltc.database.dao.Dao;
import it.ltc.database.model.commessa.Ingresso;
import it.ltc.database.model.commessa.IngressoDettaglio;
import it.ltc.model.shared.json.cliente.CaricoJSON;
import it.ltc.model.shared.json.cliente.IngressoDettaglioJSON;
import it.ltc.model.shared.json.cliente.IngressoJSON;
import it.ltc.model.shared.json.cliente.ModificaCaricoJSON;

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
	public CaricoJSON inserisci(CaricoJSON carico) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public IngressoDettaglioJSON inserisciDettaglio(IngressoDettaglioJSON carico) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IngressoJSON aggiorna(IngressoJSON carico) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IngressoJSON elimina(IngressoJSON carico) {
		// TODO Auto-generated method stub
		return null;
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
	public IngressoDettaglioJSON aggiornaDettaglio(IngressoDettaglioJSON carico) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IngressoDettaglioJSON eliminaDettaglio(IngressoDettaglioJSON carico) {
		// TODO Auto-generated method stub
		return null;
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
