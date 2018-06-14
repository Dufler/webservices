package it.ltc.services.sede.data.carico;

import java.util.LinkedList;
import java.util.List;

import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.dao.legacy.MagazzinoDao;
import it.ltc.database.dao.legacy.PakiArticoloDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.Magazzini;
import it.ltc.database.model.legacy.PakiArticolo;
import it.ltc.model.shared.dao.ICaricoDettaglioDao;
import it.ltc.model.shared.json.interno.CaricoDettaglio;

public class CaricoDettagliLegacyDAOImpl extends PakiArticoloDao implements ICaricoDettaglioDao {
	
	protected final ArticoliDao daoProdotti;
	protected final MagazzinoDao daoMagazzini;

	public CaricoDettagliLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		daoProdotti = new ArticoliDao(persistenceUnit);
		daoMagazzini = new MagazzinoDao(persistenceUnit);
	}

	@Override
	public CaricoDettaglio inserisci(CaricoDettaglio json) {
		PakiArticolo riga = deserializza(json);
		PakiArticolo entity = insert(riga);
		return serializza(entity);
	}

	@Override
	public CaricoDettaglio aggiorna(CaricoDettaglio json) {
		PakiArticolo riga = deserializza(json);
		PakiArticolo entity = update(riga, riga.getIdPakiArticolo());
		return serializza(entity);
	}

	@Override
	public CaricoDettaglio elimina(CaricoDettaglio json) {
		PakiArticolo riga = deserializza(json);
		PakiArticolo entity = delete(riga.getIdPakiArticolo());
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
			Articoli prodotto = daoProdotti.trovaDaIDUnivoco(riga.getCodUnicoArt());
			json = new CaricoDettaglio();
			json.setArticolo(prodotto != null ? prodotto.getIdArticolo() : -1);
			json.setCarico(riga.getIdPakiTesta());
			json.setMagazzino(riga.getMagazzinoltc());
			json.setQuantitaDichiarata(riga.getQtaPaki());
			json.setQuantitaRiscontrata(riga.getQtaVerificata());
			json.setRiga(riga.getRigaPacki());
			json.setSeriali(null); //TODO: Questa può essere ricavata facilmente.
		} else {
			json = null;
		}
		return json;
	}
	
	protected PakiArticolo deserializza(CaricoDettaglio json) {
		PakiArticolo riga;
		if (json != null) {
			Articoli prodotto = daoProdotti.trovaDaID(json.getArticolo());
			Magazzini magazzino = daoMagazzini.trovaDaCodiceLTC(json.getMagazzino());
			riga = new PakiArticolo();
			riga.setRigaPacki(json.getRiga());
			riga.setBarcodeCollo(json.getColloCliente());
			riga.setCodBarre(prodotto.getIdUniArticolo());
			riga.setCodUnicoArt(prodotto.getIdUniArticolo());
			riga.setCodArtStr(prodotto.getCodArtStr());
			riga.setMagazzino(magazzino != null ? magazzino.getMagaCliente() : "");
			riga.setMagazzinoltc(json.getMagazzino());
			riga.setQtaPaki(json.getQuantitaDichiarata());
			riga.setQtaVerificata(json.getQuantitaRiscontrata());
			riga.setNrDispo(json.getNote());
			riga.setMadeIn(json.getMadeIn());
		} else {
			riga = null;
		}
		return riga;
	}

}
