package it.ltc.services.sede.data.ordine;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;

import it.ltc.database.dao.CondizioneWhere;
import it.ltc.database.dao.CondizioneWhere.Condizione;
import it.ltc.database.dao.CondizioneWhere.Operatore;
import it.ltc.database.dao.legacy.ColliPrelevaDao;
import it.ltc.database.dao.legacy.DeliveryDao;
import it.ltc.database.dao.legacy.RigaCorrDao;
import it.ltc.database.dao.legacy.TestaCorrDao;
import it.ltc.database.model.legacy.ColliPreleva;
import it.ltc.database.model.legacy.Delivery;
import it.ltc.database.model.legacy.RigaCorr;
import it.ltc.database.model.legacy.TestaCorr;
import it.ltc.model.shared.dao.ISpedizioneDao;
import it.ltc.model.shared.json.interno.ordine.DatiSpedizione;
import it.ltc.model.shared.json.interno.ordine.DeliveryJSON;
import it.ltc.services.custom.exception.CustomException;

public class SpedizioneLegacyDao extends TestaCorrDao implements ISpedizioneDao {
	
	private static final Logger logger = Logger.getLogger(SpedizioneLegacyDao.class);
	
	protected final RigaCorrDao daoRighe;
	protected final ColliPrelevaDao daoColli;
	protected final DeliveryDao daoDelivery;

	public SpedizioneLegacyDao(String persistenceUnit) {
		super(persistenceUnit);
		
		daoRighe = new RigaCorrDao(persistenceUnit);
		daoColli = new ColliPrelevaDao(persistenceUnit);
		daoDelivery = new DeliveryDao(persistenceUnit);
	}

	@Override
	public DatiSpedizione abilita(DatiSpedizione spedizione, boolean abilita) {
		//Trovo i record da modificare
		TestaCorr testata = findByID(spedizione.getId());
		List<RigaCorr> righe = daoRighe.trovaDaNumeroLista(testata.getNrLista());
		List<ColliPreleva> colli = daoColli.trovaDaNumeroLista(testata.getNrLista());
		boolean successo = abilita ? abilita(testata, righe, colli) : disabilita(testata, righe, colli);
		if (!successo)
			spedizione = null;
		return spedizione;
	}
	
	protected boolean abilita(TestaCorr testata, List<RigaCorr> righe, List<ColliPreleva> colli) {
		boolean esito;
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			//TestaCorr: trasmesso = 1/2, Stato = ABILITATA, ricalcola stringa BRT
			testata.setTrasmesso(1);
			testata.setStato("ABILITATA");
			em.merge(testata);
			//RigaCorr collegate: ricalcola stringa BRT
			for (RigaCorr riga : righe) {
				riga.generaStringaBartolini();
				em.merge(riga);
			}
			//ColliPreleva collegati: abilita = SI, gruppo = YYYYMMDD
			for (ColliPreleva collo : colli) {
				collo.setAbilita("SI");
				em.merge(collo);
			}
			t.commit();
			esito = true;
		} catch (Exception e) {
			esito = false;
			if (t != null && t.isActive())
				t.rollback();
		}		
		return esito;
	}
	
	protected boolean disabilita(TestaCorr testata, List<RigaCorr> righe, List<ColliPreleva> colli) {
		boolean esito;
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			//TestaCorr: trasmesso = 0, Stato = INSERITA, ricalcola stringa BRT
			testata.setTrasmesso(0);
			testata.setStato("INSERITA");
			em.merge(testata);
			//RigaCorr collegate: ricalcola stringa BRT
			for (RigaCorr riga : righe) {
				riga.generaStringaBartolini();
				em.merge(riga);
			}
			//ColliPreleva collegati: abilita = NO, gruppo = YYYYMMDD (automatico)
			for (ColliPreleva collo : colli) {
				collo.setAbilita("NO");
				em.merge(collo);
			}
			t.commit();
			esito = true;
		} catch (Exception e) {
			esito = false;
			if (t != null && t.isActive())
				t.rollback();
		}		
		return esito;
	}

	@Override
	public DatiSpedizione elimina(DatiSpedizione spedizione) {
		//Trovo i record da eliminare
		TestaCorr testata = findByID(spedizione.getId());
		List<RigaCorr> righe = daoRighe.trovaDaNumeroLista(testata.getNrLista());
		List<ColliPreleva> colli = daoColli.trovaDaNumeroLista(testata.getNrLista());
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.remove(em.contains(testata) ? em.find(TestaCorr.class, testata.getIdTestaCor()) : testata);
			for (RigaCorr riga : righe)
				em.remove(em.contains(riga) ? em.find(RigaCorr.class, riga.getIdColliCorr()) : riga);
			for (ColliPreleva collo : colli)
				em.remove(em.contains(collo) ? em.find(ColliPreleva.class, collo.getIdColliPreleva()) : collo);
			t.commit();
		} catch (Exception e) {
			spedizione = null;
			if (t != null && t.isActive())
				t.rollback();
		}
		return spedizione;
	}

	@Override
	public DatiSpedizione trovaPerID(int id) {
		TestaCorr entity = trovaDaID(id);
		return serializza(entity);
	}

	@Override
	public List<DatiSpedizione> trovaCorrispondenti(DatiSpedizione filtro) {
		List<CondizioneWhere> condizioni = new LinkedList<>();
		//Riferimento del cliente, ricerca parziale
		String riferimento = filtro.getRiferimentoDocumento();
		if (riferimento != null && !riferimento.isEmpty()) {
			CondizioneWhere condizioneNumeroLista = new CondizioneWhere("nrLista", riferimento, Operatore.EQUAL, Condizione.OR);
			condizioni.add(condizioneNumeroLista);
			CondizioneWhere condizione = new CondizioneWhere("documentoRiferimento", riferimento, Operatore.LIKE, Condizione.OR);
			condizioni.add(condizione);
			CondizioneWhere condizioneRagioneSociale = new CondizioneWhere("ragSocDest", riferimento, Operatore.LIKE, Condizione.OR);
			condizioni.add(condizioneRagioneSociale);
		}
		//Stato
		String stato = filtro.getStato();
		if (stato != null && !stato.isEmpty()) {
			CondizioneWhere condizioneCorriere = new CondizioneWhere("stato", stato, Operatore.EQUAL, Condizione.AND);
			condizioni.add(condizioneCorriere);
		}
		//Corriere
		String corriere = filtro.getCorriere();
		if (corriere != null && !corriere.isEmpty()) {
			CondizioneWhere condizioneCorriere = new CondizioneWhere("corriere", corriere, Operatore.EQUAL, Condizione.AND);
			condizioni.add(condizioneCorriere);
		}
		//Data creazione da
		Date da = filtro.getDa();
		if (da != null) {
			CondizioneWhere condizione = new CondizioneWhere("dataGenerazione", da, Operatore.GREATER_OR_EQUAL, Condizione.AND);
			condizioni.add(condizione);
		}
		//Data creazione fino a
		Date a = filtro.getA();
		if (a != null) {
			CondizioneWhere condizione = new CondizioneWhere("dataGenerazione", a, Operatore.LESSER_OR_EQUAL, Condizione.AND);
			condizioni.add(condizione);
		}		
		List<TestaCorr> entities = findAll(condizioni, 100, "idTestaCor", false);
		List<DatiSpedizione> spedizioni = new LinkedList<>();
		for (TestaCorr entity : entities) {
			DatiSpedizione json = serializza(entity);
			spedizioni.add(json);
		}
		return spedizioni;
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

	@Override
	public DeliveryJSON inserisciDelivery(DeliveryJSON json) {
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			//Cerco tutte le spedizioni presenti nella delivery a partire dal loro ID.
			Delivery delivery = new Delivery();
			List<TestaCorr> spedizioni = new LinkedList<>();
			int totaleSpedizioni = 0;
			int totaleColli = 0;
			double totalePeso = 0;
			double totaleVolume = 0;
			for (Integer idTestaCorr : json.getSpedizioni()) {
				TestaCorr spedizione = findByID(idTestaCorr);
				if (spedizione == null)
					throw new CustomException("Impossibile trovare la spedizione con l'ID specificato. (" + idTestaCorr + ")");
				spedizioni.add(spedizione);
				//ne sommo i valori per inserirli nella delivery.
				totaleSpedizioni += 1;
				totaleColli += spedizione.getNrColli();
				totalePeso += spedizione.getPeso();
				totaleVolume += spedizione.getVolume();
			}
			//inserisco la delivery
			delivery.setCorriere(json.getCorriere());
			delivery.setTotaleSpedizioni(totaleSpedizioni);
			delivery.setTotaleColli(totaleColli);
			delivery.setTotalePeso(totalePeso);
			delivery.setTotaleVolume(totaleVolume);
			delivery.setUtente(utente);
			//vado in scrittura
			em.persist(delivery);
			for (TestaCorr spedizione : spedizioni) {
				//aggiorno lo stato delle spedizioni a "DELIVERY"
				spedizione.setIdDelivery(delivery.getIdDelivery());
				spedizione.setStato("DELIVERY");
				em.merge(spedizione);
			}
			json.setId(delivery.getIdDelivery());
			t.commit();
		} catch (CustomException e) {
			logger.error(e);
			//Re-invio l'errore all'utente.
			json = null;
			throw new CustomException(e.getMessage());
		}catch (Exception e) {
			logger.error(e);
			json = null;
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return json;
	}

	@Override
	public DeliveryJSON trovaDettagliDelivery(int id) {
		//Recupero la delivery
		Delivery delivery = daoDelivery.trovaDaID(id);
		if (delivery == null)
			throw new CustomException("Impossibile trovare la delivery con l'ID specificato. (" + id + ")");
		//Recupero i testacorr collegati
		List<TestaCorr> spedizioni = trovaDaIDDelivery(id);
		Set<Integer> idSpedizioni = new HashSet<>();
		for (TestaCorr spedizione : spedizioni)
			idSpedizioni.add(spedizione.getIdTestaCor());
		DeliveryJSON json = serializza(delivery);
		json.setSpedizioni(idSpedizioni);
		return json;
	}

	@Override
	public List<DeliveryJSON> trovaDelivery(DeliveryJSON filtro) {
		List<CondizioneWhere> condizioni = new LinkedList<>();
		//Riferimento del cliente, ricerca parziale
		//Corriere
		String corriere = filtro.getCorriere();
		if (corriere != null && !corriere.isEmpty()) {
			CondizioneWhere condizioneCorriere = new CondizioneWhere("corriere", corriere, Operatore.EQUAL, Condizione.AND);
			condizioni.add(condizioneCorriere);
		}
		//Data creazione da
		Date da = filtro.getDa();
		if (da != null) {
			CondizioneWhere condizione = new CondizioneWhere("dataCreazione", da, Operatore.GREATER_OR_EQUAL, Condizione.AND);
			condizioni.add(condizione);
		}
		//Data creazione fino a
		Date a = filtro.getA();
		if (a != null) {
			CondizioneWhere condizione = new CondizioneWhere("dataCreazione", a, Operatore.LESSER_OR_EQUAL, Condizione.AND);
			condizioni.add(condizione);
		}		
		List<Delivery> entities = daoDelivery.trovaDaFiltro(condizioni, 100, "idDelivery", false);
		List<DeliveryJSON> deliveries = new LinkedList<>();
		for (Delivery entity : entities) {
			DeliveryJSON json = serializza(entity);
			deliveries.add(json);
		}
		return deliveries;
	}
	
	protected DeliveryJSON serializza(Delivery delivery) {
		DeliveryJSON json = new DeliveryJSON();
		json.setCorriere(delivery.getCorriere());
		json.setDataGenerazione(delivery.getDataCreazione());
		json.setId(delivery.getIdDelivery());
		json.setTotalePeso(delivery.getTotalePeso());
		json.setTotaleVolume(delivery.getTotaleVolume());
		json.setTotaleColli(delivery.getTotaleColli());
		json.setTotaleSpedizioni(delivery.getTotaleSpedizioni());
		json.setUtente(delivery.getUtente());
		return json;
	}

	@Override
	public DeliveryJSON aggiornaDelivery(DeliveryJSON delivery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeliveryJSON eliminaDelivery(DeliveryJSON delivery) {
		// TODO Auto-generated method stub
		return null;
	}

}
