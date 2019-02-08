package it.ltc.services.sede.data.ordine;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;

import it.ltc.database.dao.CondizioneWhere;
import it.ltc.database.dao.CondizioneWhere.Condizione;
import it.ltc.database.dao.CondizioneWhere.Operatore;
import it.ltc.database.dao.legacy.DestinatariDao;
import it.ltc.database.dao.legacy.MagaMovDao;
import it.ltc.database.dao.legacy.MagaSdDao;
import it.ltc.database.dao.legacy.MittentiOrdineDao;
import it.ltc.database.dao.legacy.RighiImballoDao;
import it.ltc.database.dao.legacy.RighiOrdineDao;
import it.ltc.database.dao.legacy.RighiPrelievoDao;
import it.ltc.database.dao.legacy.RighiUbicPreDao;
import it.ltc.database.dao.legacy.TestataOrdiniDao;
import it.ltc.database.dao.legacy.TestataOrdiniLogStatoDao;
import it.ltc.database.dao.ordini.AssegnazioneOrdine;
import it.ltc.database.dao.ordini.ManagerAssegnazione;
import it.ltc.database.model.legacy.Destinatari;
import it.ltc.database.model.legacy.MagaMov;
import it.ltc.database.model.legacy.MagaSd;
import it.ltc.database.model.legacy.MittentiOrdine;
import it.ltc.database.model.legacy.RighiImballo;
import it.ltc.database.model.legacy.RighiOrdine;
import it.ltc.database.model.legacy.RighiPrelievo;
import it.ltc.database.model.legacy.Righiubicpre;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.database.model.legacy.TestataOrdiniLogStato;
import it.ltc.database.model.legacy.model.CausaliMovimento;
import it.ltc.model.shared.dao.IOrdineDao;
import it.ltc.model.shared.json.interno.OperatoreOrdine;
import it.ltc.model.shared.json.interno.OrdineStato;
import it.ltc.model.shared.json.interno.OrdineTestata;
import it.ltc.model.shared.json.interno.ProblemaFinalizzazioneOrdine;
import it.ltc.model.shared.json.interno.RisultatoAssegnazioneOrdine;
import it.ltc.model.shared.json.interno.RisultatoAssegnazioneOrdine.StatoAssegnazione;
import it.ltc.model.shared.json.interno.RisultatoAssegnazioneRigaOrdine;
import it.ltc.model.shared.json.interno.RisultatoAssegnazioneRigaOrdine.StatoAssegnazioneRiga;
import it.ltc.model.shared.json.interno.RisultatoFinalizzazioneOrdine;
import it.ltc.services.custom.exception.CustomException;

public class OrdineLegacyDAOImpl extends TestataOrdiniDao implements IOrdineDao {
	
	private static final Logger logger = Logger.getLogger("OrdineLegacyDAOImpl");
	
	protected final DestinatariDao daoDestinatari;
	protected final MittentiOrdineDao daoMittenti;
	protected final TestataOrdiniLogStatoDao daoStati;
	protected final RighiImballoDao daoImballo;
	protected final RighiPrelievoDao daoPrelievo;
	protected final RighiOrdineDao daoRighe;
	protected final RighiUbicPreDao daoAssegnazioni;
	protected final MagaSdDao daoSaldi;
	protected final MagaMovDao daoMovimenti;
	
	protected final ManagerAssegnazione managerAssegnazione;

	public OrdineLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		
		daoDestinatari = new DestinatariDao(persistenceUnit);
		daoMittenti = new MittentiOrdineDao(persistenceUnit);
		daoStati = new TestataOrdiniLogStatoDao(persistenceUnit);
		daoImballo = new RighiImballoDao(persistenceUnit);
		daoPrelievo = new RighiPrelievoDao(persistenceUnit);
		daoRighe = new RighiOrdineDao(persistenceUnit);
		daoAssegnazioni = new RighiUbicPreDao(persistenceUnit);
		daoSaldi = new MagaSdDao(persistenceUnit);
		daoMovimenti = new MagaMovDao(persistenceUnit);
		
		managerAssegnazione = new ManagerAssegnazione(persistenceUnit);
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
		//Controllo sullo stato
		if (!esistente.getStato().equals("INSE"))
			throw new CustomException("L'ordine non può più essere eliminato. (ID: " + json.getId() + ", stato: " + esistente.getStato() + ")");
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
	
	protected TestataOrdini checkAssegnazione(int idOrdine) {
		TestataOrdini esistente = trovaDaID(idOrdine);
		if (esistente == null)
			throw new CustomException("L'ID indicato per l'ordine non esiste. (" + idOrdine + ")");
		String stato = esistente.getStato();
		if (!("IMPO".equals(stato) || "ASSE".equals(stato)))
			throw new CustomException("L'ordine non può essere assegnato. (ID: " + idOrdine + ", stato: " + esistente.getStato() + ")");
		return esistente;
	}
	
	protected TestataOrdini checkFinalizzazione(int idOrdine) {
		TestataOrdini esistente = trovaDaID(idOrdine);
		if (esistente == null)
			throw new CustomException("L'ID indicato per l'ordine non esiste. (" + idOrdine + ")");
		String stato = esistente.getStato();
		if (!("INSE".equals(stato) || "ERRO".equals(stato)))
				throw new CustomException("L'ordine non può essere finalizzato. (ID: " + idOrdine + ", stato: " + esistente.getStato() + ")");
		return esistente;
	}
	
	@Override
	public RisultatoFinalizzazioneOrdine finalizza(int idOrdine) {
		TestataOrdini testata = checkFinalizzazione(idOrdine);
		List<ProblemaFinalizzazioneOrdine> problemi = finalizza(testata);
		RisultatoFinalizzazioneOrdine risultato = new RisultatoFinalizzazioneOrdine();
		risultato.setOrdine(serializza(testata)); //FIXME - Forse va ricaricata .
		risultato.setProblemi(problemi);
		return risultato;
	}
	
	protected List<ProblemaFinalizzazioneOrdine> finalizza(TestataOrdini ordine) {
		// Recupero la lista di prodotti richiesti e controllo la disponibilita'
		List<RighiOrdine> prodotti = daoRighe.trovaRigheDaIDOrdine(ordine.getIdTestaSped());
		logger.info("Verranno ora verificate " + prodotti.size() + " righe.");
		//EntityManager em = getManager();
		List<ProblemaFinalizzazioneOrdine> problemi = new LinkedList<>();
//		List<CustomErrorCause> righeNonValide = new LinkedList<>();
		HashMap<String, MagaSd> saldi = new HashMap<>();
		//List<MagaSd> saldi = new LinkedList<>();
		List<MagaMov> movimenti = new LinkedList<>();
		for (RighiOrdine prodotto : prodotti) {
			String keySaldo = prodotto.getIdUnicoArt() + "-" +  prodotto.getMagazzino();
			if (!saldi.containsKey(keySaldo)) {
				MagaSd saldo = daoSaldi.trovaDaArticoloEMagazzino(prodotto.getIdUnicoArt(), prodotto.getMagazzino());
				saldi.put(keySaldo, saldo);
			}
			MagaSd saldo = saldi.get(keySaldo);
			// Controllo ciò che ho trovato:
			// - se la lista è vuota oppure se non ho disponibilita' aggiungo il numero di riga a quelle non valide e boccio la transazione.
			// - altrimenti aggiorno la disponibilita' e l'impegno e genero il movimento di magazzino.
			if (saldo == null || saldo.getDisponibile() < prodotto.getQtaSpedizione()) {
				String message = "La riga " + prodotto.getNrRigo() + " non e' valida. (SKU: '" + prodotto.getCodiceArticolo() + "')";
				String reason = saldo != null ? "la quantita' disponibile è insufficiente (saldo disponibile: " + saldo.getDisponibile() + ", quantità richiesta: " + prodotto.getQtaSpedizione() + ")" : "Non è disponibile a magazzino.";
				logger.warn(message + reason);
//				CustomErrorCause cause = new CustomErrorCause(message, reason);
//				righeNonValide.add(cause);
				ProblemaFinalizzazioneOrdine rigaNonValida = new ProblemaFinalizzazioneOrdine();
				rigaNonValida.setIdOrdine(ordine.getIdTestaSped());
				rigaNonValida.setIdRiga(prodotto.getIdRigoOrdine());
				rigaNonValida.setNumeroRiga(prodotto.getNrRigo());
				rigaNonValida.setIdProdotto(prodotto.getIdArticolo());
				rigaNonValida.setSku(prodotto.getCodiceArticolo());
				rigaNonValida.setTaglia(prodotto.getTaglia());
				rigaNonValida.setDescrizione(prodotto.getDescrizione());
				rigaNonValida.setQuantitaDisponibile(saldo == null ? 0 : saldo.getDisponibile());
				rigaNonValida.setQuantitaRichiesta(prodotto.getQtaSpedizione());
				problemi.add(rigaNonValida);
				
			} else {
				int disponibile = saldo.getDisponibile() - prodotto.getQtaSpedizione();
				int impegnato = saldo.getImpegnato() + prodotto.getQtaSpedizione();
				saldo.setDisponibile(disponibile);
				saldo.setImpegnato(impegnato);
				//saldi.add(saldo);
				//MagaMov movimento = getMovimento(saldo, prodotto);
				MagaMov movimento = daoMovimenti.getNuovoMovimento(CausaliMovimento.IOS, ordine.getNrLista(), ordine.getIdTestaSped(), new Date(), saldo, prodotto.getIdUnicoArt(), prodotto.getMagazzino(), prodotto.getQtaSpedizione());
				movimenti.add(movimento);
			}
		}
		// Se non ho trovato problemi aggiorno tutti i saldi e inserisco i movimenti di magazzino
		// Altrimenti chiudo l'entity manager e restituisco la lista delle righe non valide.
		if (!problemi.isEmpty()) {
			EntityManager em = getManager();
			ordine = em.find(TestataOrdini.class, ordine.getIdTestaSped());
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				ordine.setStato("ERRO");
				em.merge(ordine);
				t.commit();
			} catch (Exception e) {
				logger.error(e);
				if (t != null && t.isActive())
					t.rollback();
			} finally {
				em.close();
			}
		} else {
			EntityManager em = getManager();
			ordine = em.find(TestataOrdini.class, ordine.getIdTestaSped());
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				ordine.setStato("IMPO");
				em.merge(ordine);
				for (MagaSd saldo : saldi.values()) {
					em.merge(saldo);
				}
				for (MagaMov movimento : movimenti) {
					em.persist(movimento);
				}
				t.commit();
			} catch (Exception e) {
				logger.error(e);
				if (t != null && t.isActive())
					t.rollback();
			} finally {
				em.close();
			}
		}
		return problemi;
	}
	
	@Override
	public RisultatoAssegnazioneOrdine assegna(int idOrdine) {
		TestataOrdini testata = checkAssegnazione(idOrdine);
		AssegnazioneOrdine assegnazione = managerAssegnazione.preparaAssegnazioneOrdine(testata);
		boolean ok = managerAssegnazione.assegnaOrdine(assegnazione);
		RisultatoAssegnazioneOrdine json = ok ? serializzaAssegnazione(idOrdine) : null;
		return json;
	}
	
	@Override
	public RisultatoAssegnazioneOrdine recuperaAssegnazione(int idOrdine) {
		checkAssegnazione(idOrdine);
		RisultatoAssegnazioneOrdine json = serializzaAssegnazione(idOrdine);
		return json;
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
	
	protected RisultatoAssegnazioneOrdine serializzaAssegnazione(int idOrdine) {
		RisultatoAssegnazioneOrdine risultato = new RisultatoAssegnazioneOrdine();
		//Recupero le info aggiornate sulla testata
		TestataOrdini testata = trovaDaID(idOrdine);
		risultato.setOrdine(serializza(testata));
		//Imposto il risultato dell'assegnazione
		StatoAssegnazione statoAssegnazione;
		switch (testata.getStatoUbicazione()) {
			case "UP" : statoAssegnazione = StatoAssegnazione.PARZIALE; break; 
			case "UT" : statoAssegnazione = StatoAssegnazione.OK; break;
			default : statoAssegnazione = StatoAssegnazione.NONDEFINITA;
		}
		risultato.setStato(statoAssegnazione);
		//Recupero le info su righiubicpre e le inserisco nel risultato
		List<RisultatoAssegnazioneRigaOrdine> assegnazioni = new LinkedList<>();
		List<Righiubicpre> righe = daoAssegnazioni.trovaDaLista(testata.getNrLista());
		for (Righiubicpre riga : righe) {
			//Recupero la riga d'ordine associata
			RighiOrdine rigaOrdine = daoRighe.trovaDaID(riga.getIdRigoOrdine());
			RisultatoAssegnazioneRigaOrdine assegnazione = new RisultatoAssegnazioneRigaOrdine();
			assegnazione.setIdRigaOrdine(riga.getIdRigoOrdine());
			assegnazione.setNumeroRiga(rigaOrdine.getNrRigo());
			assegnazione.setQuantitaAssegnata(rigaOrdine.getQtaAssegnata());
			assegnazione.setQuantitaRichiesta(rigaOrdine.getQtaSpedizione());
			assegnazione.setSku(rigaOrdine.getCodiceArticolo());
			assegnazione.setTaglia(rigaOrdine.getTaglia());
			assegnazione.setDescrizione(rigaOrdine.getDescrizione());			
			assegnazione.setCollo(riga.getNrcollo());
			assegnazione.setIdProdotto(riga.getIdArticolo());
			assegnazione.setIdRigaAssegnazione(riga.getIdubica());
			assegnazione.setQuantita(riga.getQuantita());
			assegnazione.setTotalePezzi(riga.getQuantita()); //FIXME - Qui andrebbe moltiplicato per il numero di pezzi della cassa.
			assegnazione.setUbicazione(riga.getUbicazione());
			assegnazione.setStato(StatoAssegnazioneRiga.valueOf(riga.getTipoAssegnazione()));
			assegnazione.setAnomalie(riga.getAnomalie());
			assegnazioni.add(assegnazione);
		}
		risultato.setAssegnazioni(assegnazioni);
		return risultato;
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
