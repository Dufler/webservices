package it.ltc.services.sede.data.ordine;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import it.ltc.database.dao.CondizioneWhere;
import it.ltc.database.dao.CondizioneWhere.Condizione;
import it.ltc.database.dao.CondizioneWhere.Operatore;
import it.ltc.database.dao.legacy.DestinatariDao;
import it.ltc.database.dao.legacy.MittentiOrdineDao;
import it.ltc.database.dao.legacy.RighiImballoDao;
import it.ltc.database.dao.legacy.RighiPrelievoDao;
import it.ltc.database.dao.legacy.TestataOrdiniDao;
import it.ltc.database.dao.legacy.TestataOrdiniLogStatoDao;
import it.ltc.database.model.legacy.Destinatari;
import it.ltc.database.model.legacy.MittentiOrdine;
import it.ltc.database.model.legacy.RighiImballo;
import it.ltc.database.model.legacy.RighiPrelievo;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.database.model.legacy.TestataOrdiniLogStato;
import it.ltc.model.shared.dao.IOrdineDao;
import it.ltc.model.shared.json.interno.OperatoreOrdine;
import it.ltc.model.shared.json.interno.OrdineStato;
import it.ltc.model.shared.json.interno.OrdineTestata;
import it.ltc.services.custom.exception.CustomException;

public class OrdineLegacyDAOImpl extends TestataOrdiniDao implements IOrdineDao {
	
	//private static final Logger logger = Logger.getLogger("OrdineLegacyDAOImpl");
	
	protected final DestinatariDao daoDestinatari;
	protected final MittentiOrdineDao daoMittenti;
	protected final TestataOrdiniLogStatoDao daoStati;
	protected final RighiImballoDao daoImballo;
	protected final RighiPrelievoDao daoPrelievo;

	public OrdineLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		
		daoDestinatari = new DestinatariDao(persistenceUnit);
		daoMittenti = new MittentiOrdineDao(persistenceUnit);
		daoStati = new TestataOrdiniLogStatoDao(persistenceUnit);
		daoImballo = new RighiImballoDao(persistenceUnit);
		daoPrelievo = new RighiPrelievoDao(persistenceUnit);
	}
	
	protected TestataOrdini checkInserimento(OrdineTestata json) {
		TestataOrdini testata = deserializza(json, new TestataOrdini());
		//Check sul riferimento
		TestataOrdini esistente = trovaDaRiferimento(testata.getRifOrdineCli());
		if (esistente != null)
			throw new CustomException("Il riferimento indicato per l'ordine è già esistente. (" + testata.getRifOrdineCli() + ")");
		return testata;
	}

	@Override
	public OrdineTestata inserisci(OrdineTestata json) {
		TestataOrdini testata = checkInserimento(json);
		TestataOrdini entity = insert(testata);
		return serializza(entity);
	}
	
	protected TestataOrdini checkAggiornamento(OrdineTestata json) {
		TestataOrdini esistente = trovaDaID(json.getId());
		if (esistente == null)
			throw new CustomException("L'ID indicato per l'ordine non esiste. (" + json.getId() + ")");
		//Controllo sullo stato etc.
		TestataOrdini testata = deserializza(json, esistente);
		return testata;
	}

	@Override
	public OrdineTestata aggiorna(OrdineTestata json) {
		TestataOrdini testata = checkAggiornamento(json);
		TestataOrdini entity = update(testata, testata.getIdTestaSped());
		return serializza(entity);
	}
	
	protected TestataOrdini checkEliminazione(OrdineTestata json) {
		TestataOrdini esistente = trovaDaID(json.getId());
		if (esistente == null)
			throw new CustomException("L'ID indicato per l'ordine non esiste. (" + json.getId() + ")");
		//Controllo sullo stato etc.
		return esistente;
	}

	@Override
	public OrdineTestata elimina(OrdineTestata json) {
		TestataOrdini testata = checkEliminazione(json);
		TestataOrdini entity = delete(testata.getIdTestaSped());
		return serializza(entity);
	}

	@Override
	public OrdineTestata modificaStato(OrdineTestata json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrdineTestata trovaPerID(int id) {
		TestataOrdini entity = findByID(id);
		return serializza(entity);
	}

	@Override
	public List<OrdineTestata> trovaCorrispondenti(OrdineTestata filtro) {
		List<CondizioneWhere> condizioni = new LinkedList<>();
		//Tipo di carico
		String tipo = filtro.getTipo();
		if (tipo != null && !tipo.isEmpty()) {
			CondizioneWhere condizione = new CondizioneWhere("tipoOrdine", tipo);
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
			CondizioneWhere condizioneNumeroLista = new CondizioneWhere("nrLista", riferimento, Operatore.EQUAL, Condizione.OR);
			condizioni.add(condizioneNumeroLista);
			CondizioneWhere condizione = new CondizioneWhere("rifOrdineCli", riferimento, Operatore.LIKE, Condizione.OR);
			condizioni.add(condizione);
			CondizioneWhere condizioneRagioneSociale = new CondizioneWhere("ragioneSocialeDestinatario", riferimento, Operatore.LIKE, Condizione.OR);
			condizioni.add(condizioneRagioneSociale);
		}
		//Data creazione da
		Date da = filtro.getDa();
		if (da != null) {
			CondizioneWhere condizione = new CondizioneWhere("dataOrdine", da, Operatore.GREATER_OR_EQUAL, Condizione.AND);
			condizioni.add(condizione);
		}
		//Data creazione fino a
		Date a = filtro.getA();
		if (a != null) {
			CondizioneWhere condizione = new CondizioneWhere("dataOrdine", a, Operatore.LESSER_OR_EQUAL, Condizione.AND);
			condizioni.add(condizione);
		}		
		List<TestataOrdini> entities = findAll(condizioni, 100);
		List<OrdineTestata> ordini = new LinkedList<>();
		for (TestataOrdini entity : entities) {
			OrdineTestata json = serializza(entity);
			ordini.add(json);
		}
		return ordini;
	}

	@Override
	public List<OrdineStato> trovaStati(int idTestata) {
		List<OrdineStato> stati = new LinkedList<>();
		List<TestataOrdiniLogStato> entities = daoStati.trovaStati(idTestata);
		for (TestataOrdiniLogStato entity : entities) {
			OrdineStato stato = new OrdineStato();
			stato.setData(entity.getDataOra());
			stato.setOrdine(entity.getIdTestaSped());
			stato.setStato(entity.getStato());
			stati.add(stato);
		}
		return stati;
	}
	
	protected OrdineTestata serializza(TestataOrdini testata) {
		OrdineTestata json;
		if (testata != null) {
			json = new OrdineTestata();
			//Effettua una conversione implicita da Timestamp a Date, se è null va in NullPointerException.
			if (testata.getDataConsegna() != null)
				json.setDataConsegna(testata.getDataConsegna());
			json.setDataOrdine(testata.getDataOrdine());
			json.setDataCreazione(testata.getDataArrivoFile());
			json.setDestinatario(testata.getIdDestina());
			json.setDocumentoData(testata.getDataDoc());
			json.setDocumentoRiferimento(testata.getNrDoc());
			json.setDocumentoTipo(testata.getTipoDoc());
			json.setId(testata.getIdTestaSped());
			json.setMittente(testata.getIdMittente());
			json.setNote(testata.getNote());
			json.setPriorita(testata.getPriorita());
			json.setColli(testata.getTotaleColli());
			json.setQuantitaImballataTotale(testata.getQtaimballata());
			json.setQuantitaOrdinataTotale(testata.getQtaTotaleSpedire());
			json.setQuantitaAssegnataTotale(testata.getQtaAssegnata());
			json.setQuantitaPrelevataTotale(testata.getQtaprelevata());
			json.setRagioneSocialeDestinatario(testata.getRagioneSocialeDestinatario());
			json.setRiferimento(testata.getRifOrdineCli());
			json.setStato(testata.getStato());
			json.setTipo(testata.getTipoOrdine());
			json.setNumeroLista(testata.getNrLista());
		} else {
			json = null;
		}		
		return json;
	}
	
	protected TestataOrdini deserializza(OrdineTestata json, TestataOrdini testata) {
		//Preparo i valori necessari
		GregorianCalendar dataDocumento = new GregorianCalendar();
		dataDocumento.setTime(json.getDocumentoData() != null ? json.getDocumentoData() : new Date());
		GregorianCalendar dataOrdine = new GregorianCalendar();
		dataOrdine.setTime(json.getDataOrdine() != null ? json.getDataOrdine() : new Date());
		//Controllo e ricavo il cliente a cui è destinata la merce
		Destinatari destinatario = daoDestinatari.trovaDaID(json.getDestinatario());
		if (destinatario == null)
			throw new CustomException("Non è stato trovato nessun destinatario con ID " + json.getDestinatario());
		//Controllo e ricavo il mittente
		MittentiOrdine mittente = daoMittenti.trovaDaID(json.getMittente());
		if (mittente == null)
			throw new CustomException("Non è stato trovato nessun mittente con ID " + json.getMittente());
		//Compilo i campi
		testata.setIdTestaSped(json.getId());
		testata.setAnnodoc(dataDocumento.get(Calendar.YEAR));
		testata.setAnnoOrdine(dataOrdine.get(Calendar.YEAR));
		testata.setCodCliente(destinatario.getCodDestina());
		testata.setDataConsegna(json.getDataConsegna() != null ? new Timestamp(json.getDataConsegna().getTime()) : null);
		testata.setDataDoc(new Timestamp(dataDocumento.getTimeInMillis()));
		testata.setDataOrdine(new Timestamp(dataOrdine.getTimeInMillis()));
		testata.setIdDestina(destinatario.getIdDestina());
		testata.setIdMittente(mittente.getIdMittente());
		testata.setNote(json.getNote());
		testata.setNrDoc(json.getDocumentoRiferimento());
		testata.setNrLista(json.getNumeroLista());
		testata.setNrOrdine(json.getRiferimento());
		testata.setPercAssegnata(json.getQuantitaOrdinataTotale() > 0 ? 100 * json.getQuantitaAssegnataTotale() / json.getQuantitaOrdinataTotale() : 0);
		testata.setPriorita(json.getPriorita());
		//testata.setQtaAssegnata(json.getQuantitaAssegnataTotale());
		//testata.setQtaimballata(json.getQuantitaImballataTotale());
		//testata.setQtaTotaleSpedire(json.getQuantitaOrdinataTotale());
		testata.setRagioneSocialeDestinatario(json.getRagioneSocialeDestinatario());
		testata.setRifOrdineCli(json.getRiferimento());
		testata.setStato(json.getStato());
		testata.setTipoDoc(json.getDocumentoTipo());
		testata.setTipoOrdine(json.getTipo());
		testata.setOperatore(utente);		
		return testata;
	}

	@Override
	public List<OperatoreOrdine> trovaOperatori(int idOrdine) {
		//Check sull'ordine
		TestataOrdini ordine = trovaDaID(idOrdine);
		if (ordine == null)
			throw new CustomException("Nessun ordine trovato con l'ID specificato (" + idOrdine + ")");
		List<OperatoreOrdine> operatori = new LinkedList<>();
		//Cerco chi ha prelevato
		HashMap<String, Integer> operatoriPrelievo = new HashMap<>();
		List<RighiPrelievo> prelievi = daoPrelievo.trovaDaNumeroLista(ordine.getNrLista());
		for (RighiPrelievo prelievo : prelievi) {
			int pezzi = operatoriPrelievo.containsKey(prelievo.getOperatore()) ? operatoriPrelievo.get(prelievo.getOperatore()) : 0;
			pezzi += prelievo.getQtaPrelevata();
			operatoriPrelievo.put(prelievo.getOperatore(), pezzi);
		}
		for (String operatore : operatoriPrelievo.keySet()) {
			OperatoreOrdine imballatore = new OperatoreOrdine();
			imballatore.setOperatore(operatore);
			imballatore.setOperazione("Prelievo");
			imballatore.setQuantita(operatoriPrelievo.get(operatore));
			imballatore.setOrdine(idOrdine);
			operatori.add(imballatore);
		}
		//Cerco chi ha imballato
		HashMap<String, Integer> operatoriImballo = new HashMap<>();
		List<RighiImballo> imballi = daoImballo.trovaDaNumeroLista(ordine.getNrLista());
		for (RighiImballo imballo : imballi) {
			int pezzi = operatoriImballo.containsKey(imballo.getOperatore()) ? operatoriImballo.get(imballo.getOperatore()) : 0;
			pezzi += imballo.getQtaImballata();
			operatoriImballo.put(imballo.getOperatore(), pezzi);
		}
		for (String operatore : operatoriImballo.keySet()) {
			OperatoreOrdine imballatore = new OperatoreOrdine();
			imballatore.setOperatore(operatore);
			imballatore.setOperazione("Imballo");
			imballatore.setQuantita(operatoriImballo.get(operatore));
			imballatore.setOrdine(idOrdine);
			operatori.add(imballatore);
		}
		return operatori;
	}

}
