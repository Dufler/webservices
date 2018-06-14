package it.ltc.services.sede.data.carico;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import it.ltc.database.dao.CondizioneWhere;
import it.ltc.database.dao.CondizioneWhere.Operatore;
import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.dao.legacy.FornitoreDao;
import it.ltc.database.dao.legacy.PakiArticoloDao;
import it.ltc.database.dao.legacy.PakiTestaDao;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.Fornitori;
import it.ltc.database.model.legacy.PakiArticolo;
import it.ltc.database.model.legacy.PakiTesta;
import it.ltc.model.shared.json.cliente.CaricoJSON;
import it.ltc.model.shared.json.cliente.DocumentoJSON;
import it.ltc.model.shared.json.cliente.IngressoDettaglioJSON;
import it.ltc.model.shared.json.cliente.IngressoJSON;
import it.ltc.services.custom.exception.CustomException;

public class CaricoLegacyDAOImpl extends PakiTestaDao implements CaricoDAO {
	
	private final PakiArticoloDao daoPakiArticolo;
	private final ArticoliDao daoProdotti;
	private final FornitoreDao daoFornitore;

	public CaricoLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		daoPakiArticolo = new PakiArticoloDao(persistenceUnit);
		daoProdotti = new ArticoliDao(persistenceUnit);
		daoFornitore = new FornitoreDao(persistenceUnit);
	}

	@Override
	public CaricoJSON trovaDaID(int idCarico, boolean dettagliato) {
		PakiTesta carico = trovaDaID(idCarico);
		List<PakiArticolo> dettagli;
		if (dettagliato) {
			dettagli = daoPakiArticolo.trovaRigheDaCarico(idCarico);
		} else {
			dettagli = null;
		}
		CaricoJSON json = serializzaCarico(carico, dettagli);
		return json;
	}

	@Override
	public List<IngressoJSON> trovaTutti(IngressoJSON filtro) {
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
		String riferimento = filtro.getRiferimentoCliente();
		if (riferimento != null && !riferimento.isEmpty()) {
			CondizioneWhere condizione = new CondizioneWhere("nrPaki", riferimento, Operatore.LIKE);
			condizioni.add(condizione);
		}
		//Fornitore 
		String fornitore = filtro.getFornitore();
		if (fornitore != null && !fornitore.isEmpty()) {
			CondizioneWhere condizione = new CondizioneWhere("codFornitore", fornitore, Operatore.LIKE);
			condizioni.add(condizione);
		}
		List<PakiTesta> entities = findAll(condizioni, 100);
		List<IngressoJSON> carichi = new LinkedList<>();
		for (PakiTesta entity : entities) {
			IngressoJSON json = serializzaIngresso(entity);
			carichi.add(json);
		}
		return carichi;
	}
	
	public PakiTesta deserializzaIngresso(CaricoJSON json) {
		PakiTesta carico = new PakiTesta();
		if (json != null) {
			IngressoJSON ingresso = json.getIngresso();
			//Controllo sul fornitore
			Fornitori fornitore = daoFornitore.trovaDaCodice(ingresso.getFornitore());
			if (fornitore != null) {
				carico.setCodFornitore(fornitore.getCodiceFornitore());
				carico.setIdFornitore(fornitore.getIdFornitore());
				carico.setRagSocFor(fornitore.getRagSoc());
			} else {
				throw new CustomException("Il fornitore indicato non esiste. ( " + ingresso.getFornitore() + " )");
			}
			//Altre info sul carico
			Date dataArrivo = ingresso.getDataArrivo() != null ? ingresso.getDataArrivo() : new Date();
			carico.setDataArrivo(new Timestamp(dataArrivo.getTime()));
			carico.setNrPaki(ingresso.getRiferimentoCliente());
			carico.setTipodocumento(ingresso.getTipo());
			carico.setQtaTotAto(ingresso.getPezziStimati());
			carico.setNote(ingresso.getNote());
			//Campi documento - Non vengono passati in update.
			DocumentoJSON documento = json.getDocumento();
			if (documento != null) {
				carico.setDataPaki(new Timestamp(documento.getData().getTime()));
				carico.setNrDocInterno(documento.getRiferimento());
				carico.setTipoDoc(documento.getTipo());
			}
		}
		return carico;
	}
	
	public CaricoJSON serializzaCarico(PakiTesta carico, List<PakiArticolo> dettagli) {
		CaricoJSON json;
		// Testata e Documento
		if (carico != null) {
			json = new CaricoJSON();
			IngressoJSON ingresso = serializzaIngresso(carico);
			json.setIngresso(ingresso);
			DocumentoJSON documento = serializzaDocumento(carico);
			json.setDocumento(documento);
			// Dettagli: controllo se mi sono stati passati dettagli ed
			// eventualmente li aggiungo
			if (dettagli != null) {
				List<IngressoDettaglioJSON> list = new LinkedList<>();
				for (PakiArticolo dettaglio : dettagli) {
					IngressoDettaglioJSON item = serializzaDettaglio(dettaglio);
					list.add(item);
				}
				json.setDettagli(list);
			}
		} else {
			json = null;
		}
		return json;
	}
	
	private DocumentoJSON serializzaDocumento(PakiTesta carico) {
		DocumentoJSON documento = new DocumentoJSON();
		documento.setData(carico.getDataPaki());
		documento.setRiferimento(carico.getNrDocInterno());
		documento.setTipo(carico.getTipoDoc());
		return documento;
	}
	
	public IngressoJSON serializzaIngresso(PakiTesta carico) {
		IngressoJSON ingresso = new IngressoJSON();
		ingresso.setDataArrivo(carico.getDataArrivo());
		ingresso.setFornitore(carico.getCodFornitore());
		ingresso.setId(carico.getIdTestaPaki());
		ingresso.setPezziLetti(carico.getQtaTotAre());
		ingresso.setPezziStimati(carico.getQtaTotAto());
		ingresso.setRiferimentoCliente(carico.getNrPaki());
		ingresso.setTipo(carico.getTipodocumento());
		ingresso.setStato(carico.getStato());
		ingresso.setNote(carico.getNote());
		return ingresso;
	}

	public IngressoDettaglioJSON serializzaDettaglio(PakiArticolo dettaglio) {
		IngressoDettaglioJSON item = new IngressoDettaglioJSON();
		item.setRiferimento(dettaglio.getNrOrdineFor());
		item.setRiga(dettaglio.getIdPakiArticolo()); //item.setRiga(dettaglio.getRigaPacki());
		item.setCollo(dettaglio.getBarcodeCollo());
		item.setMagazzino(dettaglio.getMagazzino());
		item.setProdotto(dettaglio.getCodArtStr());
		item.setQuantitaVerificata(dettaglio.getQtaVerificata());
		item.setQuantitaPrevista(dettaglio.getQtaPaki());
		item.setNote(dettaglio.getNrDispo());
		item.setMadeIn(dettaglio.getMadeIn());
		Articoli articolo = daoProdotti.trovaDaIDUnivoco(dettaglio.getCodUnicoArt());
		if (articolo != null)
			item.setIdProdotto(articolo.getIdArticolo());
		return item;
	}

	@Override
	public CaricoJSON nuovoCarico(CaricoJSON json) {
		PakiTesta carico = deserializzaIngresso(json);
		PakiTesta entity = insert(carico);
		json = serializzaCarico(entity, null);
		return json;
	}

	@Override
	public CaricoJSON aggiornaCarico(CaricoJSON json) {
		PakiTesta carico = deserializzaIngresso(json);
		PakiTesta entity = update(carico, carico.getIdTestaPaki());
		json = serializzaCarico(entity, null);
		return json;
	}

	@Override
	public CaricoJSON eliminaCarico(CaricoJSON json) {
		PakiTesta carico = deserializzaIngresso(json);
		PakiTesta entity = delete(carico.getIdTestaPaki());
		json = serializzaCarico(entity, null);
		return json;
	}

}
