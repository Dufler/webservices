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
import it.ltc.database.dao.legacy.ArticoliDao;
import it.ltc.database.dao.legacy.ColliImballoDao;
import it.ltc.database.dao.legacy.DestinatariDao;
import it.ltc.database.dao.legacy.ImballoDao;
import it.ltc.database.dao.legacy.MagaMovDao;
import it.ltc.database.dao.legacy.MagaSdDao;
import it.ltc.database.dao.legacy.MittentiOrdineDao;
import it.ltc.database.dao.legacy.RighiImballoDao;
import it.ltc.database.dao.legacy.RighiOrdineDao;
import it.ltc.database.dao.legacy.RighiPrelievoDao;
import it.ltc.database.dao.legacy.RighiUbicPreDao;
import it.ltc.database.dao.legacy.TempCorrDao;
import it.ltc.database.dao.legacy.TestaCorrDao;
import it.ltc.database.dao.legacy.TestataOrdiniDao;
import it.ltc.database.dao.legacy.TestataOrdiniLogStatoDao;
import it.ltc.database.dao.ordini.AssegnazioneOrdine;
import it.ltc.database.dao.ordini.ManagerAssegnazione;
import it.ltc.database.dao.ordini.ManagerResetOrdine;
import it.ltc.database.dao.ordini.ManagerResetOrdine.Reset;
import it.ltc.database.dao.ordini.ResetOrdineException;
import it.ltc.database.dao.ordini.uscita.ManagerMovimentiUscita;
import it.ltc.database.dao.ordini.uscita.RisultatoGenerazioneMovimentiUscita;
import it.ltc.database.model.legacy.Articoli;
import it.ltc.database.model.legacy.ColliImballo;
import it.ltc.database.model.legacy.Destinatari;
import it.ltc.database.model.legacy.Imballi;
import it.ltc.database.model.legacy.MagaMov;
import it.ltc.database.model.legacy.MagaSd;
import it.ltc.database.model.legacy.MittentiOrdine;
import it.ltc.database.model.legacy.RighiImballo;
import it.ltc.database.model.legacy.RighiOrdine;
import it.ltc.database.model.legacy.RighiPrelievo;
import it.ltc.database.model.legacy.Righiubicpre;
import it.ltc.database.model.legacy.TempCorr;
import it.ltc.database.model.legacy.TestaCorr;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.database.model.legacy.TestataOrdiniLogStato;
import it.ltc.model.interfaces.ordine.MContrassegno;
import it.ltc.model.interfaces.ordine.MInfoSpedizione;
import it.ltc.model.interfaces.ordine.TipoContrassegno;
import it.ltc.model.persistence.ordine.ControllerInfoSpedizioneSQLServer;
import it.ltc.model.shared.dao.IOrdineDao;
import it.ltc.model.shared.json.cliente.ImballoJSON;
import it.ltc.model.shared.json.cliente.ProdottoImballatoJSON;
import it.ltc.model.shared.json.interno.ordine.DatiSpedizione;
import it.ltc.model.shared.json.interno.ordine.OperatoreOrdine;
import it.ltc.model.shared.json.interno.ordine.OrdineStato;
import it.ltc.model.shared.json.interno.ordine.OrdineTestata;
import it.ltc.model.shared.json.interno.ordine.risultato.ProblemaFinalizzazioneOrdine;
import it.ltc.model.shared.json.interno.ordine.risultato.RisultatoAssegnazioneOrdine;
import it.ltc.model.shared.json.interno.ordine.risultato.RisultatoAssegnazioneOrdine.StatoAssegnazione;
import it.ltc.model.shared.json.interno.ordine.risultato.RisultatoAssegnazioneRigaOrdine;
import it.ltc.model.shared.json.interno.ordine.risultato.RisultatoAssegnazioneRigaOrdine.StatoAssegnazioneRiga;
import it.ltc.model.shared.json.interno.ordine.risultato.RisultatoFinalizzazioneOrdine;
import it.ltc.model.shared.json.interno.ordine.risultato.RisultatoGenerazioneMovimenti;
import it.ltc.services.custom.exception.CustomException;

public class OrdineLegacyDAOImpl extends TestataOrdiniDao implements IOrdineDao {
	
	private static final Logger logger = Logger.getLogger("OrdineLegacyDAOImpl");
	
	protected final DestinatariDao daoDestinatari;
	protected final MittentiOrdineDao daoMittenti;
	protected final TestataOrdiniLogStatoDao daoStati;
	protected final ImballoDao daoFormatiImballo;
	protected final ColliImballoDao daoColli;
	protected final RighiImballoDao daoImballo;
	protected final RighiPrelievoDao daoPrelievo;
	protected final RighiOrdineDao daoRighe;
	protected final RighiUbicPreDao daoAssegnazioni;
	protected final MagaSdDao daoSaldi;
	protected final MagaMovDao daoMovimenti;
	protected final ArticoliDao daoArticoli;
	
	protected final TempCorrDao daoDatiSpedizioni;
	protected final TestaCorrDao daoSpedizioni;
	
	protected final ControllerInfoSpedizioneSQLServer managerSpedizioni;
	
	protected final ManagerAssegnazione managerAssegnazione;
	protected final ManagerMovimentiUscita managerMovimentiUscita;
	protected final ManagerResetOrdine managerResetOrdine;

	public OrdineLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		
		daoDestinatari = new DestinatariDao(persistenceUnit);
		daoMittenti = new MittentiOrdineDao(persistenceUnit);
		daoStati = new TestataOrdiniLogStatoDao(persistenceUnit);
		daoFormatiImballo = new ImballoDao(persistenceUnit);
		daoColli = new ColliImballoDao(persistenceUnit);
		daoImballo = new RighiImballoDao(persistenceUnit);
		daoPrelievo = new RighiPrelievoDao(persistenceUnit);
		daoRighe = new RighiOrdineDao(persistenceUnit);
		daoAssegnazioni = new RighiUbicPreDao(persistenceUnit);
		daoSaldi = new MagaSdDao(persistenceUnit);
		daoMovimenti = new MagaMovDao(persistenceUnit);
		daoArticoli = new ArticoliDao(persistenceUnit);
		
		daoDatiSpedizioni = new TempCorrDao(persistenceUnit);
		daoSpedizioni = new TestaCorrDao(persistenceUnit);
		
		managerSpedizioni = new ControllerInfoSpedizioneSQLServer(persistenceUnit);
		
		managerAssegnazione = new ManagerAssegnazione(persistenceUnit);
		managerAssegnazione.setUtente(utente);
		managerMovimentiUscita = new ManagerMovimentiUscita(persistenceUnit);
		managerMovimentiUscita.setUtente(utente);
		managerResetOrdine = new ManagerResetOrdine(persistenceUnit);
		managerResetOrdine.setUtente(utente);
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
	
	protected TestataOrdini checkRecuperoAssegnazione(int idOrdine) {
		TestataOrdini esistente = trovaDaID(idOrdine);
		if (esistente == null)
			throw new CustomException("L'ID indicato per l'ordine non esiste. (" + idOrdine + ")");
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
				//MagaMov movimento = daoMovimenti.getNuovoMovimento(CausaliMovimento.IOS, ordine.getNrLista(), ordine.getIdTestaSped(), new Date(), saldo, prodotto.getIdUnicoArt(), prodotto.getMagazzino(), prodotto.getQtaSpedizione());
				MagaMov movimento = daoMovimenti.getNuovoMovimentoImpegnoOrdine(ordine, saldo, prodotto.getQtaSpedizione());
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
		checkRecuperoAssegnazione(idOrdine);
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
		List<TestataOrdini> entities = findAll(condizioni, 100, "idTestaSped", false);
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
			json.setPeso(testata.getPesoTotale());
			json.setVolume(testata.getVolumetot());
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

	@Override
	public RisultatoGenerazioneMovimenti generaMovimentiUscita(int id) {
		TestataOrdini testata = trovaDaID(id);
		OrdineTestata ordine = serializza(trovaDaID(id));
		RisultatoGenerazioneMovimenti risultato = new RisultatoGenerazioneMovimenti();
		logger.info("Avvio l'elaborazione dei movimenti d'uscita.");
		RisultatoGenerazioneMovimentiUscita generazione = managerMovimentiUscita.elaboraMovimentiUscita(testata);
		risultato.setEsito(generazione.getProblemi().isEmpty());
		risultato.setMessaggi(generazione.getProblemi());
		risultato.setOrdine(ordine);
		logger.info("Elaborazione movimenti completata con successo.");
		return risultato;
	}

	@Override
	public DatiSpedizione trovaDatiSpedizione(int id) {
		DatiSpedizione dati;
		TestataOrdini testata = trovaDaID(id);
		//Controllo se esiste un testacorr collegato attraverso l'ID o il numero di lista.
		TestaCorr spedizione = testata.getIdTestaCorr() > 0 ? daoSpedizioni.trovaDaID(testata.getIdTestaCorr()) : daoSpedizioni.trovaDaNumeroLista(testata.getNrLista());
		//Se non l'ho trovata allora controllo se esiste un tempcorr collegato attraverso il numero di lista.
		if (spedizione == null) {
			TempCorr datiSpedizione = daoDatiSpedizioni.trovaDaNumeroLista(testata.getNrLista());
			if (datiSpedizione != null) {
				dati = serializza(datiSpedizione);
				List<OrdineTestata> ord = new LinkedList<>();
				ord.add(serializza(testata));
				dati.setOrdini(ord);
			} else {
				//Recupero il destinatario
				//Destinatari destinatario = daoDestinatari.trovaDaID(testata.getIdDestina());
				//Li prendo dall'ordine e dal destinatario collegato.				
				List<OrdineTestata> ordini = new LinkedList<>();
				ordini.add(serializza(testata));
				dati = new DatiSpedizione();
				dati.setCodiceCorriere(testata.getCodiceClienteCorriere());
				dati.setColli(testata.getTotaleColli());
				dati.setCorriere(testata.getCorriere());
				dati.setDataConsegna(testata.getDataConsegna());
				dati.setDataDocumento(testata.getDataDoc());
				dati.setNote(testata.getNote());
				dati.setPeso(testata.getPesoTotale());
				dati.setPezzi(testata.getQtaimballata());
				dati.setRiferimentoDocumento(testata.getRifOrdineCli());
				dati.setServizioCorriere(testata.getTipoTrasporto());
				dati.setTipoContrassegno(testata.getTipoIncasso());
				dati.setValoreContrassegno(testata.getValContrassegno());
				dati.setValutaContrassegno("EUR");
				dati.setTipoDocumento(testata.getTipoDoc());
				dati.setValoreDoganale(testata.getValoreDoganale());
				dati.setVolume(testata.getVolumetot());
				dati.setOrdini(ordini);
			}
		} else {
			dati = serializza(spedizione);
			//Set<Integer> ids = trovaOrdiniRaggruppati(spedizione.getIdTestaCor());
			//dati.setOrdini(ids);
			List<TestataOrdini> ordini = trovaDaSpedizione(spedizione.getIdTestaCor());
			List<OrdineTestata> ord = new LinkedList<>();
			for (TestataOrdini ordine : ordini)
				ord.add(serializza(ordine));
			dati.setOrdini(ord);
		}
		return dati;
	}

	@Override
	public DatiSpedizione generaDatiSpedizione(DatiSpedizione json) {
		MInfoSpedizione model = serializza(json);
		model = managerSpedizioni.inserisci(model);
		DatiSpedizione risposta = deserializza(model);
		return risposta;
	}
	
	protected MInfoSpedizione serializza(DatiSpedizione json) {
		MInfoSpedizione model = new MInfoSpedizione();
//		model.setAbilitaPartenza(json.isAbilitaPartenza());
		model.setCodiceCorriere(json.getCodiceCorriere());
		model.setCodiceTracking(json.getCodiceTracking());
		//Contrassegno
		Double valoreContrassegno = json.getValoreContrassegno();
		if (valoreContrassegno != null && valoreContrassegno > 0 ) {
			TipoContrassegno tipo;
			try { tipo = TipoContrassegno.valueOf(json.getTipoContrassegno()); } catch (Exception e) { throw new CustomException("Il tipo di contrassegno non è valido."); }
			MContrassegno contrassegno = new MContrassegno();
			contrassegno.setTipo(tipo);
			contrassegno.setValore(valoreContrassegno);
			contrassegno.setValuta(json.getValutaContrassegno());
			model.setContrassegno(contrassegno);
		}	
		model.setCorriere(json.getCorriere());
		model.setDataConsegna(json.getDataConsegna());
		model.setDataDocumento(json.getDataDocumento());
		model.setForzaAccoppiamentoDestinatari(json.isForzaAccoppiamentoDestinatari());
		model.setId(json.getId());
		model.setNote(json.getNote());
		model.setRiferimentoDocumento(json.getRiferimentoDocumento());
		model.setServizioCorriere(json.getServizioCorriere());
		model.setTipoDocumento(json.getTipoDocumento());
		model.setValoreDoganale(json.getValoreDoganale());
		//Riferimenti ordini
		for (OrdineTestata ordine : json.getOrdini()) {
//			TestataOrdini testata = trovaDaID(idOrdine);
//			if (testata == null)
//				throw new CustomException("L'ID indicato per l'ordine da spedire non esiste. (ID: " + idOrdine + ")");
//			model.aggiungiRiferimentoOrdine(testata.getRifOrdineCli());
			model.aggiungiRiferimentoOrdine(ordine.getRiferimento());
		}
		return model;
	}
	
	protected DatiSpedizione deserializza(MInfoSpedizione model) {
		DatiSpedizione json = new DatiSpedizione();
//		json.setAbilitaPartenza(model.isAbilitaPartenza());
		json.setCodiceCorriere(model.getCodiceCorriere());
		json.setCodiceTracking(model.getCodiceTracking());
		json.setColli(model.getColli());
		json.setCorriere(model.getCorriere());
		json.setDataConsegna(model.getDataConsegna());
		json.setDataDocumento(model.getDataDocumento());
		json.setForzaAccoppiamentoDestinatari(model.isForzaAccoppiamentoDestinatari());
		json.setId(model.getId());
		json.setNote(model.getNote());
		json.setPeso(model.getPeso());
		json.setPezzi(model.getPezzi());
		json.setRiferimentoDocumento(model.getRiferimentoDocumento());
		json.setServizioCorriere(model.getServizioCorriere());
		//contrassegno
		MContrassegno contrassegno = model.getContrassegno(); 
		if (contrassegno != null) {
			json.setTipoContrassegno(contrassegno.getTipo().name());
			json.setValoreContrassegno(contrassegno.getValore());
			json.setValutaContrassegno(contrassegno.getValuta());
		}
		json.setTipoDocumento(model.getTipoDocumento());
		json.setValoreDoganale(model.getValoreDoganale());
		json.setVolume(model.getVolume());
		//ordini collegati
		//Set<Integer> ordini = new HashSet<>();
		List<OrdineTestata> ordini = new LinkedList<>();
		for (String riferimento : model.getRiferimentiOrdini()) {
			TestataOrdini testata = trovaDaRiferimento(riferimento);
//			if (testata == null)
//				throw new CustomException("Il riferimento indicato per l'ordine da spedire non esiste. (Riferimento: " + riferimento + ")");
//			ordini.add(testata.getIdTestaSped());
			ordini.add(serializza(testata));
		}
		json.setOrdini(ordini);
		return json;
	}
	
	protected DatiSpedizione serializza(TestaCorr spedizione) {
		DatiSpedizione dati = new DatiSpedizione();
//		dati.setAbilitaPartenza(spedizione.getTrasmesso() > 0);
		dati.setCorriere(spedizione.getCorriere());
		dati.setServizioCorriere(spedizione.getServizio());
		dati.setCodiceCorriere(spedizione.getCodMittente());
		dati.setCodiceTracking(spedizione.getTrackingNumber());
		dati.setDataConsegna(spedizione.getDataConsegnaTassativa());
		dati.setDataDocumento(spedizione.getDocumentoData());
		dati.setId(spedizione.getIdTestaCor());
		dati.setNote(spedizione.getNote1() + spedizione.getNote2());
		dati.setRiferimentoDocumento(spedizione.getDocumentoRiferimento());
		dati.setTipoContrassegno(spedizione.getTipoIncasso());
		dati.setValoreContrassegno(spedizione.getContrassegno());
		dati.setValutaContrassegno(spedizione.getValutaIncasso());
		dati.setTipoDocumento(spedizione.getDocumentoTipo());
		dati.setValoreDoganale(spedizione.getValoreMerce());
		dati.setForzaAccoppiamentoDestinatari(false);
		dati.setPezzi(spedizione.getPezzi());
		dati.setColli(spedizione.getNrColli());
		dati.setPeso(spedizione.getPeso());
		dati.setVolume(spedizione.getVolume());
		dati.setStato(spedizione.getStato());
		//campi indirizzo
		dati.setRiferimento(spedizione.getMittenteAlfa());
		dati.setRagioneSociale(spedizione.getRagSocDest());
		dati.setIndirizzo(spedizione.getIndirizzo());
		dati.setLocalita(spedizione.getLocalita());
		dati.setProvincia(spedizione.getProvincia());
		dati.setCap(spedizione.getCap());
		dati.setNazione(spedizione.getNazione());
		return dati;
	}
	
	protected DatiSpedizione serializza(TempCorr spedizione) {
		DatiSpedizione dati = new DatiSpedizione();
//		dati.setAbilitaPartenza(false);
		dati.setCorriere(spedizione.getCodcorriere());
		dati.setServizioCorriere(spedizione.getServizio());
		dati.setCodiceCorriere(spedizione.getCodCliente());
		dati.setCodiceTracking(null);
		dati.setDataConsegna(spedizione.getDataConsegna());
		dati.setDataDocumento(spedizione.getDataDocu());
		//dati.setId(spedizione.getIdTempCor()); //Uso tempCorr solo al momento di recuperare i dati la prima volta quando sono solo li.
		dati.setNote(spedizione.getNote());
		dati.setRiferimentoDocumento(spedizione.getRiferimento());
		dati.setRiferimento(spedizione.getRiferimento());
		dati.setTipoContrassegno(spedizione.getTipoIncasso());
		dati.setValoreContrassegno(spedizione.getValContra());
		dati.setValutaContrassegno("EUR");
		dati.setTipoDocumento(spedizione.getTipoDocu());
		dati.setValoreDoganale(spedizione.getValoreDoganale());
		dati.setForzaAccoppiamentoDestinatari(false);
		dati.setPezzi(spedizione.getPezzi());
		dati.setColli(spedizione.getNrColli());
		dati.setPeso(spedizione.getPesoKg());
		dati.setVolume(spedizione.getVolume());
		dati.setStato("NONE");
		return dati;
	}
	
	@Override
	public List<ImballoJSON> ottieniDettagliImballo(int idOrdine) {
		//Recupero l'ordine
		TestataOrdini ordine = trovaDaID(idOrdine);
		if (ordine == null)
			throw new CustomException("Nessun ordine trovato con l'ID specificato (" + idOrdine + ")");
		//Recupero e inserisco le informazioni sugli imballi.
		List<ColliImballo> colliImballati = daoColli.trovaDaNumeroLista(ordine.getNrLista());
		List<RighiImballo> righe = daoImballo.trovaDaNumeroLista(ordine.getNrLista());
		List<ImballoJSON> imballi = new LinkedList<>();
		for (ColliImballo collo : colliImballati) {
			ImballoJSON imballo = new ImballoJSON();
			//Info generali sul collo
			imballo.setRiferimento(collo.getKeyColloSpe());
			imballo.setBarcode(collo.getBarCodeImb());
			imballo.setPeso(collo.getPesoKg() != null ? collo.getPesoKg() : 0.0);
			double volume = ((double) collo.getVolume()) / 1000000.0;
			imballo.setVolume(volume);
			//formato dell'imballo, se presente.
			Imballi formatoImballo = daoFormatiImballo.trovaDaCodice(collo.getCodFormato());
			if (formatoImballo != null) {
				imballo.setH(formatoImballo.getH());
				imballo.setL(formatoImballo.getL());
				imballo.setZ(formatoImballo.getZ());
				imballo.setFormato(collo.getCodFormato());
			}
			//Prodotti e seriali
			List<ProdottoImballatoJSON> prodotti = new LinkedList<>();
			List<String> seriali = new LinkedList<>();
			HashMap<String, Integer> mappaProdottiQuantita = new HashMap<>();
			for (RighiImballo riga : righe) {
				//Controllo se appartiene a quel collo
				if (riga.getKeyColloSpe().equals(collo.getKeyColloSpe())) {
					//Aggiorno la giusta quantita per il codiceUnivoco
					String idUnivocoArticolo = riga.getIdUniArticolo();
					int quantita = mappaProdottiQuantita.containsKey(idUnivocoArticolo) ? mappaProdottiQuantita.get(idUnivocoArticolo) : 0;
					quantita += riga.getQtaImballata();
					mappaProdottiQuantita.put(idUnivocoArticolo, quantita);
					//Aggiungo il seriale alla lista
					seriali.add(riga.getSeriale());
				}
			}
			//Aggiungo ogni SKU trovato e la relativa quantita alla lista dei prodotti.
			for (String idUnivocoArticolo : mappaProdottiQuantita.keySet()) {
				//Recupero l'anagrafica articolo necessaria
				Articoli articolo = daoArticoli.trovaDaIDUnivoco(idUnivocoArticolo);
				if (articolo == null) throw new CustomException("Nessun articolo trovato con ID univoco: " + idUnivocoArticolo);
				ProdottoImballatoJSON prodottoImballato = new ProdottoImballatoJSON();
				prodottoImballato.setProdotto(articolo.getCodArtStr());
				prodottoImballato.setDescrizione(articolo.getDescrizione());
				prodottoImballato.setTaglia(articolo.getTaglia());
				prodottoImballato.setQuantitaImballata(mappaProdottiQuantita.get(idUnivocoArticolo));
				prodotti.add(prodottoImballato);
			}
			imballo.setProdotti(prodotti);
			imballo.setSeriali(seriali);
			imballi.add(imballo);
		}
		return imballi;
	}

	@Override
	public OrdineTestata annullaImballo(int idOrdine) {
		boolean reset;
		try {
			reset = managerResetOrdine.resetta(idOrdine, Reset.IMBALLO);
		} catch (ResetOrdineException e) {
			logger.error(e.getMessage());
			throw new CustomException(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new CustomException(e.getMessage());
		}
		OrdineTestata result = reset ? serializza(trovaDaID(idOrdine)) : null;
		return result;
	}

	@Override
	public OrdineTestata annullaAssegnazioneConRiposizionamento(int idOrdine) {
		boolean reset;
		try {
			reset = managerResetOrdine.resetta(idOrdine, Reset.ASSEGNAZIONE_RIPOSIZIONA);
		} catch (ResetOrdineException e) {
			logger.error(e.getMessage());
			throw new CustomException(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new CustomException(e.getMessage());
		}
		OrdineTestata result = reset ? serializza(trovaDaID(idOrdine)) : null;
		return result;
	}

	@Override
	public OrdineTestata annullaAssegnazioneConNuovoCarico(int idOrdine) {
		boolean reset;
		try {
			reset = managerResetOrdine.resetta(idOrdine, Reset.ASSEGNAZIONE_NUOVOCARICO);
		} catch (ResetOrdineException e) {
			logger.error(e.getMessage());
			throw new CustomException(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new CustomException(e.getMessage());
		}
		OrdineTestata result = reset ? serializza(trovaDaID(idOrdine)) : null;
		return result;
	}

	@Override
	public OrdineTestata annullaImportazione(int idOrdine) {
		boolean reset;
		try {
			reset = managerResetOrdine.resetta(idOrdine, Reset.IMPORTAZIONE);
		} catch (ResetOrdineException e) {
			logger.error(e.getMessage());
			throw new CustomException(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new CustomException(e.getMessage());
		}
		OrdineTestata result = reset ? serializza(trovaDaID(idOrdine)) : null;
		return result;
	}

}
