package it.ltc.services.sede.data.magazzino;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.jboss.logging.Logger;

import it.ltc.database.dao.Dao;
import it.ltc.database.dao.legacy.ColliCaricoDao;
import it.ltc.database.dao.legacy.ColliPackDao;
import it.ltc.database.dao.legacy.MagaMovDao;
import it.ltc.database.dao.legacy.MagaSdDao;
import it.ltc.database.dao.legacy.MagazzinoDao;
import it.ltc.database.dao.legacy.PakiArticoloDao;
import it.ltc.database.dao.legacy.PakiTestaDao;
import it.ltc.database.dao.legacy.TestataOrdiniDao;
import it.ltc.database.dao.legacy.UbicazioniDao;
import it.ltc.database.model.legacy.ColliCarico;
import it.ltc.database.model.legacy.ColliPack;
import it.ltc.database.model.legacy.MagaMov;
import it.ltc.database.model.legacy.MagaSd;
import it.ltc.database.model.legacy.Magazzini;
import it.ltc.database.model.legacy.PakiArticolo;
import it.ltc.database.model.legacy.PakiTesta;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.database.model.legacy.Ubicazioni;
import it.ltc.database.model.legacy.model.CausaliMovimento;
import it.ltc.services.custom.exception.CustomException;
import it.ltc.services.sede.model.magazzino.ColloInventarioConSeriali;
import it.ltc.services.sede.model.magazzino.ControlloSeriale;
import it.ltc.utility.miscellanea.time.DateConverter;
import it.ltc.utility.zpl.etichette.ltc.EtichettaColloInLTC_10x7;
import it.ltc.utility.zpl.etichette.ltc.ProdottoEtichetta;

public class InventarioLegacyColtortiDao extends Dao implements InventarioDao {
	
	private static final Logger logger = Logger.getLogger("InventarioLegacyColtortiDao");
	
	public static final String TIPO_CARICO = "INVENTARIO";
	public static final String TIPO_CARICO_ERRORI = "INVENTARIO_ERRORI";
	public static final String NOME_CLIENTE = "Ikonic";
	
	protected final ColliCaricoDao daoColli;
	//protected final ColliPackSerialiDao daoProdotti;
	protected final ColliPackDao daoProdotti;
	protected final PakiTestaDao daoCarichi;
	protected final PakiArticoloDao daoRighe;
	protected final MagaSdDao daoSaldi;
	protected final MagaMovDao daoMovimenti;
	protected final MagazzinoDao daoMagazzini;
	protected final UbicazioniDao daoUbicazioni;
	protected final TestataOrdiniDao daoOrdini;
	
	protected HashMap<String, PakiArticolo> mappaRighe;

	public InventarioLegacyColtortiDao(String persistenceUnit) {
		super(persistenceUnit);
		daoColli = new ColliCaricoDao(persistenceUnit);
		//daoProdotti = new ColliPackSerialiDao(persistenceUnit);
		daoProdotti = new ColliPackDao(persistenceUnit);
		daoCarichi = new PakiTestaDao(persistenceUnit);
		daoRighe = new PakiArticoloDao(persistenceUnit);
		daoSaldi = new MagaSdDao(persistenceUnit);
		daoMovimenti = new MagaMovDao(persistenceUnit);
		daoMagazzini = new MagazzinoDao(persistenceUnit);
		daoUbicazioni = new UbicazioniDao(persistenceUnit);
		daoOrdini = new TestataOrdiniDao(persistenceUnit);
	}
	
	protected Ubicazioni checkUbicazione(ColloInventarioConSeriali colloConSeriali) {
		Ubicazioni ubicazione = daoUbicazioni.trovaDaCodice(colloConSeriali.getUbicazione());
		if (ubicazione == null)
			throw new CustomException("L'ubicazione indicata non esiste. (" + colloConSeriali.getUbicazione() + ")");
		return ubicazione;
	}
	
	protected ColliCarico creaNuovoCollo(ColloInventarioConSeriali colloConSeriali) {
		ColliCarico collo = new ColliCarico();
		collo.setIdDocumento(colloConSeriali.getCarico());
		collo.setMagazzino(colloConSeriali.getMagazzino());
		collo.setNrCollo(daoColli.getProgressivoNrCollo());
		collo.setKeyUbicaCar(colloConSeriali.getUbicazione());
		collo.setFlagtc("1");
		collo.setUtente(utente);
		collo.setPrefissoCollo("CO"); //Fisso per Coltorti.
		return collo;
	}
	
	protected List<ColliPack> trovaProdotti(ColloInventarioConSeriali collo) {
		List<ColliPack> prodotti = new LinkedList<>();
		for (String seriale : collo.getSeriali()) {
			ColliPack prodotto = ricercaSeriale(seriale);
			//Controllo se il prodotto sia già stato generato nel carico. EDIT 08/10/2018: Antonio ha chiesto di rimuover questo controllo.
//			if (prodotto.getFlagtc() == 0)
//				throw new CustomException("Il seriale indicato non è ancora stato caricato a sistema. (" + seriale  + ")");
			prodotti.add(prodotto);
		}
		return prodotti;
	}
	
	protected List<ColliPack> copiaProdotti(List<ColliPack> prodotti) {
		List<ColliPack> prodottiCopia = new LinkedList<>();
		for (ColliPack prodotto : prodotti) {
			ColliPack prodottoCopia = new ColliPack();
			prodottoCopia.setCodArtStr(prodotto.getCodArtStr());
			prodottoCopia.setCodiceArticolo(prodotto.getCodiceArticolo());
			prodottoCopia.setDescrizione(prodotto.getDescrizione());
			prodottoCopia.setMagazzino(prodotto.getMagazzino());
			prodottoCopia.setOperatore(utente);
			prodottoCopia.setQta(prodotto.getQta());
			prodottoCopia.setQtaimpegnata(0); //La fisso a 0, la riga è sempre da 1 pezzo ed è disponibile.
			prodottoCopia.setSeriale(prodotto.getSeriale());
			prodottoCopia.setTaglia(prodotto.getTaglia());
			prodottoCopia.setFlagtc(prodotto.getFlagtc()); //EDIT 08/10/2018: Antonio ha voluto così.
			prodottiCopia.add(prodottoCopia);
		}
		return prodottiCopia;
	}
	
	/**
	 * Trova il carico, controlla che sia un'inventario e ne aggiorna la quantità riscontrata totale prima di restituirlo.
	 */
	protected PakiTesta trovaCarico(ColloInventarioConSeriali collo) {
		PakiTesta carico = daoCarichi.trovaDaID(collo.getCarico());
		//Controllo sul tipo di carico
		String tipoCarico = carico.getTipodocumento();
		if (!TIPO_CARICO.equals(tipoCarico) && !TIPO_CARICO_ERRORI.equals(tipoCarico))
			throw new CustomException("Il carico indicato non è un inventario. Tipo trovato: '" + tipoCarico + "'");
		//Aggiorno le quantità
		carico.setQtaTotAre(carico.getQtaTotAre() + collo.getSeriali().size());
		return carico;
	}
	
	protected Collection<PakiArticolo> trovaRighe(PakiTesta carico, ColloInventarioConSeriali collo, List<ColliPack> prodotti) {
		mappaRighe = new HashMap<>();
		for (ColliPack prodotto : prodotti) {
			if (!mappaRighe.containsKey(prodotto.getCodiceArticolo())) {
				List<PakiArticolo> righe = daoRighe.trovaRigheDaCaricoEProdotto(collo.getCarico(), prodotto.getCodiceArticolo());
				PakiArticolo riga = righe.isEmpty() ? nuovaRiga(carico, collo, prodotto) : righe.get(0);
				mappaRighe.put(prodotto.getCodiceArticolo(), riga);
				logger.info("Inserita nuova riga nel packing list");
			}
			PakiArticolo riga = mappaRighe.get(prodotto.getCodiceArticolo());
			logger.info("Quantità verificata nella riga: " + riga.getQtaVerificata());
			riga.setQtaVerificata(riga.getQtaVerificata() + 1); //L'incremento è fisso ad 1 perchè si tratta di seriali.
			logger.info("Quantità verificata nella riga dopo l'incremento: " + riga.getQtaVerificata());
		}		
		return mappaRighe.values();
	}
	
	protected PakiArticolo nuovaRiga(PakiTesta carico, ColloInventarioConSeriali collo, ColliPack prodotto) {
		//Recupero i dati necessari
		Magazzini magazzino = daoMagazzini.trovaDaCodiceLTC(collo.getMagazzino());
		if (magazzino == null)
			throw new CustomException("Nessun magazzino corrispondente al codice specificato. (" + collo.getMagazzino() + ")");
		PakiArticolo riga = new PakiArticolo();
		riga.setCodArtStr(prodotto.getCodArtStr());
		riga.setCodBarre(prodotto.getCodiceArticolo());
		riga.setCodMotivo("INSE");
		riga.setCodUnicoArt(prodotto.getCodiceArticolo());
		riga.setIdPakiTesta(carico.getIdTestaPaki());
		riga.setMagazzino(magazzino.getMagaCliente());
		riga.setMagazzinoltc(magazzino.getCodiceMag());
		riga.setQtaPaki(0);
		riga.setQtaVerificata(0);
		riga.setRigaPacki(0);
		riga.setUtente(utente);
		return riga;
	}
	
	protected void generaCodiceEtichettaZPL(ColloInventarioConSeriali collo, PakiTesta carico, ColliCarico nuovoCollo, List<ColliPack> prodottiCopia) {
		StringBuilder etichetta = new StringBuilder();
		int indexEtichetta = 0;
		int indexProdotto = 0;
		int numeroEtichette = (int) Math.ceil((double) prodottiCopia.size() / (double) EtichettaColloInLTC_10x7.MAX_OGGETTI);
		ProdottoEtichetta[][] prodotti = new ProdottoEtichetta[numeroEtichette][6];
		//Riempo l'array con le info
		for (int index = 0; index < prodottiCopia.size(); index++) {
			ColliPack prodotto = prodottiCopia.get(index);
			prodotti[indexEtichetta][indexProdotto] = new ProdottoEtichetta(prodotto.getCodArtStr(), prodotto.getTaglia(), prodotto.getDescrizione(), prodotto.getQta());			
			//Aggiorno gli indici
			indexProdotto++;
			if (indexProdotto >= EtichettaColloInLTC_10x7.MAX_OGGETTI) {
				indexProdotto = 0;
				indexEtichetta += 1;
			}
		}
		//Stampo le etichette
		for (int index = 0; index < numeroEtichette; index++) {
			EtichettaColloInLTC_10x7 label = new EtichettaColloInLTC_10x7(NOME_CLIENTE, carico.getNrPaki(), nuovoCollo.getBarcode(), nuovoCollo.getKeyUbicaCar(), nuovoCollo.getKeyColloCar(), prodotti[index], null);
			etichetta.append(label.toString());
		}
		collo.setEtichetta(etichetta.toString());
	}
	
	public HashMap<String, MagaSd> aggiornaSaldi(List<ColliPack> prodotti) {
		HashMap<String, MagaSd> saldiDaAggiornare = new HashMap<>();
		for (ColliPack prodotto : prodotti) {
			String key = prodotto.getCodiceArticolo() + prodotto.getMagazzino();
			if (!saldiDaAggiornare.containsKey(key)) {
				//trovo il saldo del magazzino di origine, controllo che la disponibilità sia maggiore dell'impegno e abbasso disponibilità e esistenza
				MagaSd saldoOrigine = daoSaldi.trovaDaArticoloEMagazzino(prodotto.getCodiceArticolo(), prodotto.getMagazzino());
				if (saldoOrigine == null || saldoOrigine.getImpegnato() > saldoOrigine.getDisponibile())
					throw new CustomException("Impossibile spostare il prodotto indicato dal magazzino attuale, la disponibilità non è sufficiente. Articolo: " + prodotto.getCodArtStr() + ", magazzino: " + prodotto.getMagazzino());
				if (saldoOrigine.getDisponibile() <= 0 || saldoOrigine.getEsistenza() <= 0)
					throw new CustomException("Impossibile spostare il prodotto indicato dal magazzino attuale, la disponibilità o esistenza è 0. Articolo: " + prodotto.getCodArtStr() + ", magazzino: " + prodotto.getMagazzino());
				saldiDaAggiornare.put(key, saldoOrigine);
			}
			MagaSd saldoOrigine = saldiDaAggiornare.get(key);
			saldoOrigine.setDisponibile(saldoOrigine.getDisponibile() - 1);
			saldoOrigine.setEsistenza(saldoOrigine.getEsistenza() - 1);		
		}
		return saldiDaAggiornare;
	}
	
	public List<MagaMov> creaMovimenti(PakiTesta carico, List<ColliPack> prodotti, HashMap<String, MagaSd> saldi) {
		List<MagaMov> movimenti = new LinkedList<>();
		for (ColliPack prodotto : prodotti) {
			String key = prodotto.getCodiceArticolo() + prodotto.getMagazzino();
			MagaSd saldo = saldi.get(key);
			MagaMov movimento = daoMovimenti.getNuovoMovimento("SPE", CausaliMovimento.ANNULLA_CARICO, carico.getNrPaki(), carico.getIdTestaPaki(), carico.getCreazione(), saldo, 1, "Inventario");
			movimenti.add(movimento);
		}
		return movimenti;
	}

	@Override
	public ColloInventarioConSeriali nuovoCollo(ColloInventarioConSeriali collo) {
		//check ubicazione
		checkUbicazione(collo);
		//nuovo collicarico
		ColliCarico nuovoCollo = creaNuovoCollo(collo);
		//update dei collipack
		List<ColliPack> prodotti = trovaProdotti(collo);
		//Creo dei collipack copia
		List<ColliPack> prodottiCopia = copiaProdotti(prodotti);
		//update pakitesta
		PakiTesta carico = trovaCarico(collo);
		//update/insert pakiarticolo
		Collection<PakiArticolo> righe = trovaRighe(carico, collo, prodotti);
		//Vado in scrittura sul DB
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			//Salvo il carico
			em.merge(carico);
			//Salvo le righe
			for (PakiArticolo riga : righe) {
				if (riga.getIdPakiArticolo() == 0)
					em.persist(riga);
				else
					em.merge(riga);
			}
			//Salvo il nuovo collo
			em.persist(nuovoCollo);
			//Aggiorno i collipack
			for (ColliPack prodotto : prodotti) {
				//Lo segno come non più disponibile
				prodotto.setFlagimp("I");
				//Cambio leggermente il seriale
				prodotto.setSeriale(prodotto.getSeriale() + "_INV");
				em.merge(prodotto);
			}
			//Inserisco i collipack copia
			for (ColliPack prodotto : prodottiCopia) {
				prodotto.setIdTestaPaki(carico.getIdTestaPaki());
				prodotto.setIdPakiarticolo(mappaRighe.get(prodotto.getCodiceArticolo()).getIdPakiArticolo());
				prodotto.setLotto(carico.getNrPaki());
				prodotto.setNrIdColloPk(nuovoCollo.getKeyColloCar());
				//Lo metto sempre come disponibile
				prodotto.setFlagimp("N");
				//prodotto.setFlagtc(1);
				em.persist(prodotto);
			}
			t.commit();
			//etichetta zpl
			generaCodiceEtichettaZPL(collo, carico, nuovoCollo, prodottiCopia);
		} catch (Exception e) {
			logger.error(e);
			collo = null;
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return collo;
	}

	@Override
	public ColloInventarioConSeriali nuovoColloDiScarico(ColloInventarioConSeriali collo) {
		//check ubicazione
		checkUbicazione(collo);
		//nuovo collicarico
		ColliCarico nuovoCollo = creaNuovoCollo(collo);
		//update dei collipack
		List<ColliPack> prodotti = trovaProdotti(collo);
		//Creo dei collipack copia
		List<ColliPack> prodottiCopia = copiaProdotti(prodotti);
		//update pakitesta
		PakiTesta carico = trovaCarico(collo);
		//update/insert pakiarticolo
		Collection<PakiArticolo> righe = trovaRighe(carico, collo, prodotti);
		//Aggiorno i saldi scaricando il prodotto
		HashMap<String, MagaSd> saldiDaAggiornare = aggiornaSaldi(prodotti);
		//Faccio i movimenti di scarico
		List<MagaMov> movimentiDiScarico = creaMovimenti(carico, prodotti, saldiDaAggiornare);
		//Vado in scrittura sul DB
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			//Salvo il carico
			em.merge(carico);
			//Salvo le righe
			for (PakiArticolo riga : righe) {
				if (riga.getIdPakiArticolo() == 0)
					em.persist(riga);
				else
					em.merge(riga);
			}
			//Salvo il nuovo collo
			em.persist(nuovoCollo);
			//Aggiorno i collipack
			for (ColliPack prodotto : prodotti) {
				//Per quelli da escludere vado a controllare e modificare lo stato di impegno
				String impegno = "N".equals(prodotto.getFlagimp()) ? "I" : "A"; //Se era già impegnato ma me lo trovo comunque li lo segno come anomalo.
				prodotto.setFlagimp(impegno);
				//Cambio leggermente il seriale
				prodotto.setSeriale(prodotto.getSeriale() + "_INV");
				em.merge(prodotto);
			}
			for (ColliPack prodotto : prodottiCopia) {
				prodotto.setIdTestaPaki(carico.getIdTestaPaki());
				prodotto.setIdPakiarticolo(mappaRighe.get(prodotto.getCodiceArticolo()).getIdPakiArticolo());
				prodotto.setLotto(carico.getNrPaki());
				prodotto.setNrIdColloPk(nuovoCollo.getKeyColloCar());
				//Lo metto sempre come disponibile
				prodotto.setFlagimp("E"); //EDIT 09/10/2018: imposto flagimp a "E" e flagtc a 0 per impedire che possano assegnare su collipack da scaricare.
				prodotto.setFlagtc(0);
				em.persist(prodotto);
			}
			//Aggiorno i saldi
			for (MagaSd saldo : saldiDaAggiornare.values()) {
				em.merge(saldo);
			}
			//Inserisco i movimenti
			for (MagaMov movimento : movimentiDiScarico) {
				em.persist(movimento);
			}
			t.commit();
			//etichetta zpl
			generaCodiceEtichettaZPL(collo, carico, nuovoCollo, prodottiCopia);
		} catch (Exception e) {
			logger.error(e);
			collo = null;
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return collo;
	}
	
	protected ColliPack ricercaSeriale(String seriale) {
		ColliPack prodotto = daoProdotti.trovaSeriale(seriale);
		if (prodotto == null) {
			//eseguo un secondo controllo sostituendo gli ultimi 2 caratteri con degli 0
			String serialeIncompleto = seriale.length() > 2 ? seriale.substring(0, seriale.length() - 2) : "";
			serialeIncompleto += "00";
			prodotto = daoProdotti.trovaSeriale(serialeIncompleto);
			if (prodotto == null)
				throw new CustomException("Il seriale indicato non esiste. (" + seriale + ")");
		}
		return prodotto;
	}

	@Override
	public boolean checkImpegnoSeriale(ControlloSeriale seriale) {
		//controllo che il seriale indicato esista in collipack, se ne esiste più di uno prendo il più recente.
		ColliPack prodotto = ricercaSeriale(seriale.getSeriale());		
		if (prodotto.getFlagtc() == 0)
			throw new CustomException("Il seriale indicato non è ancora stato caricato a sistema. Inserirlo tra le anomalie. (" + seriale.getSeriale()  + ")", 409);
		
		//Controllo inserito per il ricompattamento il 03/01//2019
		if (prodotto.getFlagimp().equals("S")) {
			//Vado a controllare lo stato della lista che lo impegna, se è IMPO o INIB significa che è un ordine in fase di prelievo e non posso spostarlo.
			TestataOrdini ordine = daoOrdini.trovaDaNumeroLista(prodotto.getListaimp());
			String stato = ordine != null ? ordine.getStato() : "";
			if (stato.equals("IMPO") || stato.equals("INIB"))
				throw new CustomException("Il seriale indicato è un prodotto che è stato impegnato. Lasciarlo dove sta.");
		}
		
		//Se mi viene richiesto controllo l'impegno
		if (seriale.isCheckImpegno()) {
			MagaSd saldi = daoSaldi.trovaDaArticoloEMagazzino(prodotto.getCodiceArticolo(), seriale.getMagazzino());
			if (saldi.getDisponibile() <= saldi.getImpegnato())
				throw new CustomException("Il seriale indicato è un prodotto che è stato impegnato. Inserirlo tra le anomalie.");
		}
		//Controllo se appartiene già al carico in questione
		if (prodotto.getIdTestaPaki() == seriale.getCarico())
			throw new CustomException("Il seriale indicato appartiene già a questo packing list. Controlla il bollone del collo di appartenenza. (" + prodotto.getNrIdColloPk() + ")", 405);
		//Controllo se è già stato inventariato, nel campo lotto compare l'nrpaki del pakitesta inventario.
		String carico = prodotto.getLotto();
		if (carico != null)
			throw new CustomException("Il seriale indicato appartiene già al packing list: '" + carico + "'", 406);
		return false;
	}

	@Override
	public boolean distruggiCollo(ColloInventarioConSeriali colloDaDistruggere) {
		ColliCarico collo = daoColli.trovaDaCodice(colloDaDistruggere.getCollo());
		if (collo == null)
			throw new CustomException("Nessun collo trovato con questa etichetta. (" + colloDaDistruggere.getCollo() + ")");
		//Se il collo appartiene al carico che mi viene indicato lo blocco subito
		if (collo.getIdDocumento() == colloDaDistruggere.getCarico())
			throw new CustomException("Questo collo appartiene al carico attualmente in uso.");
		List<ColliPack> prodotti = daoProdotti.trovaProdottiNelCollo(collo.getKeyColloCar());
		if (colloDaDistruggere.isForzaDistruzione()) {
			//Aggiorno lo stato d'impegno dei colli a "D"
			for (ColliPack prodotto : prodotti) {
				prodotto.setFlagimp("D");
				daoProdotti.aggiorna(prodotto);//FIXME: sarebbe meglio fare una sola transazione.
			}
		} else {
			boolean prodottoPresente = false;
			for (ColliPack prodotto : prodotti) {
				if ("N".equals(prodotto.getFlagimp())) {
					prodottoPresente = true;
				}
			}
			if (prodottoPresente)
				throw new CustomException("Il collo non è vuoto e non può essere distrutto.", 405);
		}		
		//Se i controlli sono andati bene vado a cancellare il collo
		Timestamp now = new Timestamp(new Date().getTime());
		collo.setCancellato("SI");
		collo.setOraDistruzione(DateConverter.getOraComeIntero(now));
		collo.setDataDistruzione(DateConverter.ripulisciTimestap(now));		
		collo.setUteDistruzione(utente);
		collo = daoColli.aggiorna(collo);
		boolean success = collo != null;
		return success;
	}

}
