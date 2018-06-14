package it.ltc.services.sede.data.carico;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import it.ltc.database.dao.CondizioneWhere;
import it.ltc.database.dao.CondizioneWhere.Operatore;
import it.ltc.database.dao.legacy.PakiTestaDao;
import it.ltc.database.model.legacy.PakiTesta;
import it.ltc.model.shared.dao.ICaricoDao;
import it.ltc.model.shared.json.interno.CaricoTestata;

public class CarichiLegacyDAOImpl extends PakiTestaDao implements ICaricoDao {

	public CarichiLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
	}

	@Override
	public CaricoTestata inserisci(CaricoTestata json) {
		PakiTesta testata = deserializza(json);
		PakiTesta entity = insert(testata);
		return serializza(entity);
	}

	@Override
	public CaricoTestata aggiorna(CaricoTestata json) {
		PakiTesta testata = deserializza(json);
		PakiTesta entity = update(testata, testata.getIdTestaPaki());
		return serializza(entity);
	}

	@Override
	public CaricoTestata elimina(CaricoTestata json) {
		PakiTesta testata = deserializza(json);
		PakiTesta entity = delete(testata.getIdTestaPaki());
		return serializza(entity);
	}

	@Override
	public List<CaricoTestata> trovaCorrispondenti(CaricoTestata filtro) {
		List<CondizioneWhere> condizioni = new LinkedList<>();
		//Tipo di carico
		String tipo = filtro.getTipo();
		if (tipo != null && !tipo.isEmpty()) {
			CondizioneWhere condizione = new CondizioneWhere("tipodocumento", tipo);
			condizioni.add(condizione);
		}
		//Stato del carico
		String stato = filtro.getStato();
		if (stato != null && !stato.isEmpty()) {
			CondizioneWhere condizione = new CondizioneWhere("stato", stato);
			condizioni.add(condizione);
		}
		//Riferimento del cliente, ricerca parziale
		String riferimento = filtro.getRiferimento();
		if (riferimento != null && !riferimento.isEmpty()) {
			CondizioneWhere condizione = new CondizioneWhere("nrPaki", riferimento, Operatore.LIKE);
			condizioni.add(condizione);
		}
		//Fornitore 
		int fornitore = filtro.getFornitore();
		if (fornitore > 0) {
			CondizioneWhere condizione = new CondizioneWhere("idFornitore", fornitore);
			condizioni.add(condizione);
		}
		List<PakiTesta> entities = findAll(condizioni, 100);
		List<CaricoTestata> carichi = new LinkedList<>();
		for (PakiTesta entity : entities) {
			CaricoTestata json = serializza(entity);
			carichi.add(json);
		}
		return carichi;
	}
	
	protected CaricoTestata serializza(PakiTesta testata) {
		CaricoTestata json;
		if (testata != null) {
			json = new CaricoTestata();
			json.setDataArrivo(testata.getDataInizio());
			json.setDataArrivoPresunto(testata.getDataArrivo());
			json.setDocumentoData(testata.getDataPaki());
			json.setDocumentoRiferimento(testata.getNrDocInterno());
			json.setDocumentoTipo(testata.getTipoDoc());
			json.setFornitore(testata.getIdFornitore());
			json.setId(testata.getIdTestaPaki());
			json.setNote(testata.getNote());
			json.setProdottiEccedenti(testata.getAbilitaEccedenze().equals("SI"));
			json.setProdottiNonDichiarati(testata.getTipoPack().equals("INS"));
			json.setQuantitaDichiarataTotale(testata.getQtaTotAto());
			json.setQuantitaRiscontrataTotale(testata.getQtaTotAre());
			json.setRiferimento(testata.getNrPaki());
			json.setStagione(testata.getStagione());
			json.setStato(testata.getStato());
			json.setTipo(testata.getTipodocumento());
		} else {
			json = null;
		}
		return json;
	}
	
	protected PakiTesta deserializza(CaricoTestata json) {
		PakiTesta testata;
		if (json != null) {
			testata = new PakiTesta();
			testata.setDataInizio(new Timestamp(json.getDataArrivo() != null ? json.getDataArrivo().getTime() : new Date().getTime()));
			testata.setDataArrivo(new Timestamp(json.getDataArrivoPresunto() != null ? json.getDataArrivoPresunto().getTime() : new Date().getTime()));
			testata.setDataPaki(new Timestamp(json.getDocumentoData() != null ? json.getDocumentoData().getTime() : new Date().getTime()));
			testata.setNrDocInterno(json.getDocumentoRiferimento());
			testata.setTipoDoc(json.getDocumentoTipo());
			testata.setIdFornitore(json.getFornitore());
			testata.setIdTestaPaki(json.getId());
			testata.setNote(json.getNote());
			testata.setAbilitaEccedenze(json.isProdottiEccedenti() ? "SI" : "NO");
			testata.setTipoPack(json.isProdottiNonDichiarati() ? "INS" : "ART");
			testata.setQtaTotAto(json.getQuantitaDichiarataTotale());
			testata.setQtaTotAre(json.getQuantitaRiscontrataTotale());
			testata.setNrPaki(json.getRiferimento());
			testata.setStagione(json.getStagione());
			testata.setStato(json.getStato());
			testata.setTipodocumento(json.getTipo());
		} else {
			testata = null;
		}
		return testata;
	}

}
