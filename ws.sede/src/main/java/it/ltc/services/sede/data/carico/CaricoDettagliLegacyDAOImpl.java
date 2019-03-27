package it.ltc.services.sede.data.carico;

import java.util.LinkedList;
import java.util.List;

import org.jboss.logging.Logger;

import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.dao.legacy.ColliPackDao;
import it.ltc.database.dao.legacy.MagazzinoDao;
import it.ltc.database.dao.legacy.PakiArticoloDao;
import it.ltc.database.dao.legacy.PakiTestaDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.ColliPack;
import it.ltc.database.model.legacy.Magazzini;
import it.ltc.database.model.legacy.PakiArticolo;
import it.ltc.database.model.legacy.PakiTesta;
import it.ltc.database.model.legacy.model.PakiTestaTotali;
import it.ltc.model.shared.dao.ICaricoDettaglioDao;
import it.ltc.model.shared.json.interno.carico.CaricoDettaglio;
import it.ltc.services.custom.exception.CustomException;

public class CaricoDettagliLegacyDAOImpl extends PakiArticoloDao implements ICaricoDettaglioDao {
	
	private static final Logger logger = Logger.getLogger("CaricoDettagliLegacyDAOImpl");
	
	protected final PakiTestaDao daoCarichi;
	protected final ArticoliDao daoProdotti;
	protected final MagazzinoDao daoMagazzini;
	protected final ColliPackDao daoColliPack;

	public CaricoDettagliLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		daoCarichi = new PakiTestaDao(persistenceUnit);
		daoProdotti = new ArticoliDao(persistenceUnit);
		daoMagazzini = new MagazzinoDao(persistenceUnit);
		daoColliPack = new ColliPackDao(persistenceUnit);
	}
	
	protected void aggiornaTotaliCarico(PakiTesta carico) {
		if (carico != null) {
			PakiTestaTotali totali = calcolaTotaliPerCarico(carico.getIdTestaPaki());
			//int dichiarato = calcolaTotaleDichiaratoPerCarico(idCarico);
			//int riscontrato = calcolaTotaleVerificatoPerCarico(idCarico);
			carico.setQtaTotAto(totali.getTotaleDichiarato());
			carico.setQtaTotAre(totali.getTotaleRiscontrato());
			carico = daoCarichi.aggiorna(carico);
			if (carico == null)
				logger.error("Impossibile aggiornare le quantità del carico!");
		}
	}
	
	protected PakiTesta checkCarico(CaricoDettaglio json) {
		PakiTesta carico = daoCarichi.trovaDaID(json.getCarico());
		if (carico == null)	throw new CustomException("L'ID indicato per il carico non esiste! (" + json.getCarico() + ")");
		if (carico.getStato().equals("IN_LAVORAZIONE")) throw new CustomException("Non è possibile modificare il carico indicato ! (Stato = " + carico.getStato() + ")");
		return carico;
	}
	
	protected PakiArticolo checkRiga(CaricoDettaglio json) {
		PakiArticolo riga = trovaDaID(json.getId());
		if (riga == null)	throw new CustomException("L'ID indicato per la riga non esiste! (" + json.getId() + ")");
		return riga;
	}

	@Override
	public CaricoDettaglio inserisci(CaricoDettaglio json) {
		PakiTesta carico = checkCarico(json);
		PakiArticolo riga = deserializza(json, carico, new PakiArticolo());		
		PakiArticolo entity = insert(riga);
		if (entity != null)
			aggiornaTotaliCarico(carico);
		return serializza(entity);
	}

	@Override
	public CaricoDettaglio aggiorna(CaricoDettaglio json) {
		PakiTesta carico = checkCarico(json);
		PakiArticolo rigaEsistente = checkRiga(json);
		PakiArticolo riga = deserializza(json, carico, rigaEsistente);
		PakiArticolo entity = update(riga, riga.getIdPakiArticolo());
		if (entity != null) {
			aggiornaTotaliCarico(carico);
			List<ColliPack> prodotti = daoColliPack.trovaProdottiNellaRigaDiCarico(riga.getIdPakiArticolo());
			for (ColliPack prodotto : prodotti) {
				prodotto.setMagazzino(riga.getMagazzinoltc());
				ColliPack collipack = daoColliPack.aggiorna(prodotto);
				if (collipack == null)
					logger.error("Impossibile aggiornare il magazzino sul collipack: " + prodotto.getIdColliPack());
			}
		}
		return serializza(entity);
	}

	@Override
	public CaricoDettaglio elimina(CaricoDettaglio json) {
		PakiTesta carico = checkCarico(json); //Eseguo comunque un controllo per vedere se è un'operazione valida.
		PakiArticolo riga = checkRiga(json);
		PakiArticolo entity = delete(riga.getIdPakiArticolo());
		if (entity != null) 
			aggiornaTotaliCarico(carico);
		return serializza(entity);
	}

	@Override
	public List<CaricoDettaglio> trovaDettagli(int idCarico) {
		List<PakiArticolo> entities = trovaRigheDaCarico(idCarico);
		List<CaricoDettaglio> jsons = new LinkedList<>();
		for (PakiArticolo entity : entities)
			jsons.add(serializza(entity));
		return jsons;
	}
	
	protected CaricoDettaglio serializza(PakiArticolo riga) {
		CaricoDettaglio json;
		if (riga != null) {
			//Recupero le info necessarie
			Articoli prodotto = daoProdotti.trovaDaIDUnivoco(riga.getCodUnicoArt()); //FIXME - Questa riga andrà tolta a regime, verra usato l'ID già presente a livello di riga.
			json = new CaricoDettaglio();
			json.setId(riga.getIdPakiArticolo());
			json.setArticolo(prodotto != null ? prodotto.getIdArticolo() : -1);
			json.setCarico(riga.getIdPakiTesta());
			json.setMagazzino(riga.getMagazzinoltc());
			json.setQuantitaDichiarata(riga.getQtaPaki());
			json.setQuantitaRiscontrata(riga.getQtaVerificata());
			json.setRiga(riga.getRigaPacki());
			json.setNote(riga.getNrDispo());
			json.setMadeIn(riga.getMadeIn());
			json.setColloCliente(riga.getBarcodeCollo());
			json.setSeriali(null); //TODO: Questa può essere ricavata facilmente.
		} else {
			json = null;
		}
		return json;
	}
	
	protected PakiArticolo deserializza(CaricoDettaglio json, PakiTesta carico, PakiArticolo riga) {
		if (json != null) {
			Articoli prodotto = daoProdotti.trovaDaID(json.getArticolo());
			if (prodotto == null) throw new CustomException("Nessun prodotto trovato con l'ID specificato (" + json.getId() + ")");
			Magazzini magazzino = daoMagazzini.trovaDaCodiceLTC(json.getMagazzino());
			if (magazzino == null) throw new CustomException("Nessun magazzino trovato con il codice specificato (" + json.getMagazzino() + ")");
			riga = new PakiArticolo();
			riga.setIdPakiArticolo(json.getId());
			riga.setRigaPacki(json.getRiga());
			riga.setBarcodeCollo(json.getColloCliente());
			riga.setCodBarre(prodotto.getCodBarre());
			riga.setCodUnicoArt(prodotto.getIdUniArticolo());
			riga.setIdArticolo(prodotto.getIdArticolo());
			riga.setCodArtStr(prodotto.getCodArtStr());
			riga.setMagazzino(magazzino != null ? magazzino.getMagaCliente() : "");
			riga.setMagazzinoltc(json.getMagazzino());
			riga.setQtaPaki(json.getQuantitaDichiarata());
			riga.setQtaVerificata(json.getQuantitaRiscontrata());
			riga.setNrDispo(json.getNote());
			riga.setMadeIn(json.getMadeIn());
			riga.setUtente(utente);
			//Campi del carico
			riga.setIdPakiTesta(json.getCarico());
			riga.setNrOrdineFor(carico.getNrPaki());
		} else {
			riga = null;
		}
		return riga;
	}

}
