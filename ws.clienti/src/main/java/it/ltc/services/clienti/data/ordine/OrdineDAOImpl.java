package it.ltc.services.clienti.data.ordine;

import java.util.List;

import it.ltc.database.dao.Dao;
import it.ltc.database.model.commessa.Uscita;
import it.ltc.database.model.commessa.UscitaDettaglio;
import it.ltc.services.clienti.model.prodotto.UscitaJSON;
import it.ltc.services.clienti.model.prodotto.OrdineImballatoJSON;
import it.ltc.services.clienti.model.prodotto.OrdineJSON;
import it.ltc.services.clienti.model.prodotto.SpedizioneJSON;
import it.ltc.services.clienti.model.prodotto.UscitaDettaglioJSON;

public class OrdineDAOImpl extends Dao implements OrdineDAO<Uscita, UscitaDettaglio> {

	public OrdineDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
	}

	@Override
	public OrdineJSON trovaDaID(int idOrdine) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrdineJSON trovaDaRiferimento(String riferimento, boolean dettagliato) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UscitaJSON> trovaTutti() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean inserisci(OrdineJSON carico) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean inserisciDettaglio(UscitaDettaglioJSON carico) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean aggiorna(UscitaJSON carico) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean aggiornaDettaglio(UscitaDettaglioJSON carico) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean elimina(UscitaJSON carico) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean eliminaDettaglio(UscitaDettaglioJSON carico) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public OrdineJSON serializzaOrdine(Uscita uscita, List<UscitaDettaglio> dettagli) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UscitaJSON serializzaUscita(Uscita uscita, boolean dettagliato) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UscitaDettaglioJSON serializzaDettaglio(UscitaDettaglio dettaglio) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uscita deserializzaUscita(OrdineJSON json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UscitaDettaglio deserializzaDettaglio(UscitaDettaglioJSON json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean assegna(String riferimento) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean assegna(int idOrdine) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean spedisci(SpedizioneJSON spedizione) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SpedizioneJSON getDocumentoDiTrasporto(int idSpedizione) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrdineImballatoJSON ottieniDettagliImballo(String riferimento) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrdineImballatoJSON ottieniDettagliImballo(int idOrdine) {
		// TODO Auto-generated method stub
		return null;
	}

}
