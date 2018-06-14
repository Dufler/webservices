package it.ltc.services.clienti.data.ordine;

import java.util.List;

import it.ltc.database.dao.Dao;
import it.ltc.database.model.commessa.Uscita;
import it.ltc.database.model.commessa.UscitaDettaglio;
import it.ltc.model.shared.json.cliente.OrdineImballatoJSON;
import it.ltc.model.shared.json.cliente.OrdineJSON;
import it.ltc.model.shared.json.cliente.SpedizioneJSON;
import it.ltc.model.shared.json.cliente.UscitaDettaglioJSON;
import it.ltc.model.shared.json.cliente.UscitaJSON;

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
	public OrdineJSON inserisci(OrdineJSON carico) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UscitaDettaglioJSON inserisciDettaglio(UscitaDettaglioJSON carico) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UscitaJSON aggiorna(UscitaJSON carico) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UscitaDettaglioJSON aggiornaDettaglio(UscitaDettaglioJSON carico) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UscitaJSON elimina(UscitaJSON carico) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UscitaDettaglioJSON eliminaDettaglio(UscitaDettaglioJSON carico) {
		// TODO Auto-generated method stub
		return null;
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
