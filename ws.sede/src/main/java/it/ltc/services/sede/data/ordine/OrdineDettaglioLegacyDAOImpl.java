package it.ltc.services.sede.data.ordine;

import java.util.LinkedList;
import java.util.List;

import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.dao.legacy.MagazzinoDao;
import it.ltc.database.dao.legacy.RighiOrdineDao;
import it.ltc.database.dao.legacy.TestataOrdiniDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.Magazzini;
import it.ltc.database.model.legacy.RighiOrdine;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.database.model.legacy.model.TestataOrdiniTotali;
import it.ltc.model.shared.dao.IOrdineDettaglioDao;
import it.ltc.model.shared.json.interno.ordine.OrdineDettaglio;
import it.ltc.services.custom.exception.CustomException;

public class OrdineDettaglioLegacyDAOImpl extends RighiOrdineDao implements IOrdineDettaglioDao {
	
	protected final ArticoliDao daoProdotti;
	protected final MagazzinoDao daoMagazzini;
	protected final TestataOrdiniDao daoOrdini;

	public OrdineDettaglioLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		daoProdotti = new ArticoliDao(persistenceUnit);
		daoMagazzini = new MagazzinoDao(persistenceUnit);
		daoOrdini = new TestataOrdiniDao(persistenceUnit);
	}
	/**
	 * check sull'esistenza e sullo stato.
	 */
	protected TestataOrdini checkTestata(OrdineDettaglio json) {
		TestataOrdini ordine = daoOrdini.trovaDaID(json.getOrdine());
		if (ordine == null) throw new CustomException("L'ID indicato per l'ordine non esiste. (" + json.getOrdine() + ")");
		if (!(ordine.getStato().equals("INSE") || ordine.getStato().equals("ERRO"))) throw new CustomException("L'ordine indicato non può essere modificato. (Stato = " + ordine.getStato() + ")");
		return ordine;
	}
	
	protected RighiOrdine checkRiga(OrdineDettaglio json) {
		RighiOrdine riga = findByID(json.getId());
		if (riga == null) throw new CustomException("L'ID indicato per la riga non esiste. (" + json.getId() + ")");
		return riga;
	}
	
	protected void aggiornaTotaliOrdine(TestataOrdini ordine) {
		if (ordine != null) {
			TestataOrdiniTotali totali = calcolaTotali(ordine.getIdTestaSped());
			ordine.setQtaTotaleSpedire(totali.getTotaleOrdinato());
			ordine.setQtaimballata(totali.getTotaleImballato());
			ordine.setQtaAssegnata(totali.getTotaleAssegnato());
			daoOrdini.aggiorna(ordine);
		}
	}

	@Override
	public OrdineDettaglio inserisci(OrdineDettaglio json) {
		TestataOrdini ordine = checkTestata(json);		
		RighiOrdine riga = deserializza(json, ordine, new RighiOrdine());
		RighiOrdine entity = inserisci(riga);
		if (entity != null) {
			aggiornaTotaliOrdine(ordine);
		}
		return serializza(entity);
	}

	@Override
	public OrdineDettaglio aggiorna(OrdineDettaglio json) {
		TestataOrdini ordine = checkTestata(json);	
		RighiOrdine rigaEsistente = checkRiga(json);
		RighiOrdine riga = deserializza(json, ordine, rigaEsistente);
		//Forzo le quantità ad essere uguali siccome lo stato dell'ordine è ancora a "INSE" o "ERRO".
		riga.setQtadaubicare(riga.getQtaSpedizione());
		RighiOrdine entity = aggiorna(riga);
		if (entity != null) {
			aggiornaTotaliOrdine(ordine);
		}
		return serializza(entity);
	}

	@Override
	public OrdineDettaglio elimina(OrdineDettaglio json) {
		TestataOrdini ordine = checkTestata(json); //Eseguo comunque un controllo per vedere se è un'operazione valida.
		RighiOrdine rigaEsistente = checkRiga(json);
		RighiOrdine entity = elimina(rigaEsistente);
		if (entity != null) {
			aggiornaTotaliOrdine(ordine);
		}
		return serializza(entity);
	}

	@Override
	public List<OrdineDettaglio> trovaDettagli(int idOrdine) {
		List<RighiOrdine> righe = trovaRigheDaIDOrdine(idOrdine);
		List<OrdineDettaglio> dettagli = new LinkedList<>();
		for (RighiOrdine riga : righe) {
			OrdineDettaglio dettaglio = serializza(riga);
			dettagli.add(dettaglio);
		}
		return dettagli;
	}
	
	protected OrdineDettaglio serializza(RighiOrdine riga) {
		OrdineDettaglio json;
		if (riga != null) {
			json = new OrdineDettaglio();
			json.setId(riga.getIdRigoOrdine());
			json.setArticolo(riga.getIdArticolo());
			json.setMagazzino(riga.getMagazzino());
			json.setNote(riga.getNoteCliente());
			json.setOrdine(riga.getIdTestataOrdine());
			json.setRiga(riga.getNrRigo());
			json.setQuantitaImballata(riga.getQtaImballata());
			json.setQuantitaAssegnata(riga.getQtaAssegnata());
			json.setQuantitaOrdinata(riga.getQtaSpedizione());
		} else {
			json = null;
		}
		return json;
	}
	
	protected RighiOrdine deserializza(OrdineDettaglio json, TestataOrdini ordine, RighiOrdine riga) {
		if (json != null) {
			Articoli prodotto = daoProdotti.trovaDaID(json.getArticolo());
			if (prodotto == null) throw new CustomException("L'ID del prodotto indicato non esiste. (" + json.getArticolo() + ")");
			Magazzini magazzino = daoMagazzini.trovaDaCodiceLTC(json.getMagazzino());
			if (magazzino == null) throw new CustomException("Il magazzino indicato non esiste. (" + json.getMagazzino() + ")");
			riga.setIdRigoOrdine(json.getId());
			riga.setBarraEAN(prodotto.getBarraEAN());
			riga.setBarraUPC(prodotto.getBarraUPC());
			riga.setCodBarre(prodotto.getCodBarre());
			riga.setCodiceArticolo(prodotto.getCodArtStr());
			riga.setColore(prodotto.getColore());
			riga.setComposizione(prodotto.getComposizione());
			riga.setDescrizione(prodotto.getDescrizione());
			riga.setIdArticolo(prodotto.getIdArticolo());
			riga.setIdUnicoArt(prodotto.getIdUniArticolo());
			riga.setMagazzino(magazzino.getCodiceMag());
			riga.setNoteCliente(json.getNote());
			riga.setNrRigo(json.getRiga());
			riga.setNumerata(prodotto.getNumerata());
			riga.setQtaSpedizione(json.getQuantitaOrdinata());
			riga.setTaglia(prodotto.getTaglia());
			//riga.setTipoord(tipoord); FIXME - Qui potrebbe andarci il tipo di riga (es. Coltorti lo usa)
			//Campi ordine
			riga.setDataOrdine(ordine.getDataOrdine());
			riga.setIdDestina(ordine.getIdDestina());
			riga.setIdTestataOrdine(ordine.getIdTestaSped());
			riga.setNrLista(ordine.getNrLista());
			riga.setNrOrdine(ordine.getNrOrdine());
			riga.setRagstampe1(ordine.getNrLista());
		} else {
			riga = null;
		}		
		return riga;
	}

}
