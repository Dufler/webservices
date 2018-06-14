package it.ltc.services.sede.data.carico;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.jboss.logging.Logger;

import it.ltc.database.dao.legacy.ColliCaricoDao;
import it.ltc.database.dao.legacy.ColliPackDao;
import it.ltc.database.dao.legacy.MagazzinoDao;
import it.ltc.database.dao.legacy.PakiArticoloDao;
import it.ltc.database.dao.legacy.PakiTestaDao;
import it.ltc.database.model.legacy.ColliCarico;
import it.ltc.database.model.legacy.ColliPack;
import it.ltc.database.model.legacy.Magazzini;
import it.ltc.database.model.legacy.PakiArticolo;
import it.ltc.database.model.legacy.PakiTesta;
import it.ltc.database.model.legacy.model.StatoCarico;
import it.ltc.services.custom.exception.CustomException;
import it.ltc.services.sede.model.carico.ColloCaricoJSON;

public class RiscontroColliLegacyDAOImpl extends ColliCaricoDao implements RiscontroColliDAO {
	
	private static final Logger logger = Logger.getLogger("RiscontroColliLegacyDAOImpl");

	protected final PakiTestaDao daoCarichi;
	protected final PakiArticoloDao daoRighe;
	protected final MagazzinoDao daoMagazzini;
	protected final ColliPackDao daoColliPack;
	
	public RiscontroColliLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		daoCarichi = new PakiTestaDao(persistenceUnit);
		daoRighe = new PakiArticoloDao(persistenceUnit);
		daoMagazzini = new MagazzinoDao(persistenceUnit);
		daoColliPack = new ColliPackDao(persistenceUnit);
	}
	
	protected PakiTesta checkCarico(int idCarico) {
		PakiTesta carico = daoCarichi.trovaDaID(idCarico);
		if (carico == null)
			throw new CustomException("Il carico indicato non esiste (ID: " + idCarico + ")");
		else if (!carico.getStato().equals(StatoCarico.IN_LAVORAZIONE.getNome()))
			throw new CustomException("Il carico specificato non e' lavorabile. (Stato: " + carico.getStato() + ")");
		return carico;
	}
	
	protected void checkMagazzino(String codice) {
		Magazzini magazzino = daoMagazzini.trovaDaCodiceLTC(codice);
		if (magazzino == null)
			throw new CustomException("Il magazzino indicato non esiste (" + codice + ")");
	}
	
	protected ColliCarico checkCollo(int idCollo) {
		ColliCarico collo = trovaDaID(idCollo);
		if (collo == null)
			throw new CustomException("Il collo indicato non esiste (ID: " + idCollo + ")");
		return collo;
	}

	@Override
	public ColloCaricoJSON nuovoCollo(ColloCaricoJSON collo) {
		//eseguo i controlli
		checkCarico(collo.getCarico());
		checkMagazzino(collo.getMagazzino());
		//Preparo le info necessarie
		ColliCarico nuovoCollo = new ColliCarico();
		nuovoCollo.setId_Box(collo.getBarcodeCliente());
		nuovoCollo.setIdDocumento(collo.getCarico());
		nuovoCollo.setMagazzino(collo.getMagazzino());
		nuovoCollo.setNrCollo(getProgressivoNrCollo());
		//Inserisco il collo a sistema
		nuovoCollo = inserisci(nuovoCollo);
		if (nuovoCollo != null) {
			collo.setId(nuovoCollo.getIdCollo());
			collo.setEtichetta("Etichetta ZPL");
		} else {
			collo = null;
		}
		return collo;
	}

	@Override
	public ColloCaricoJSON aggiornaCollo(ColloCaricoJSON collo) {
		//eseguo i controlli
		checkCarico(collo.getCarico());
		checkMagazzino(collo.getMagazzino());
		ColliCarico esistente = checkCollo(collo.getId());
		List<ColliPack> prodotti = daoColliPack.trovaProdottiNelCollo(esistente.getKeyColloCar());
		//Eseguo l'aggiornamento
		esistente.setId_Box(collo.getBarcodeCliente());
		esistente.setIdDocumento(collo.getCarico());
		esistente.setMagazzino(collo.getMagazzino());
		for (ColliPack prodotto : prodotti) {
			prodotto.setMagazzino(collo.getMagazzino());
			prodotto.setIdTestaPaki(collo.getCarico());
		}
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.merge(esistente);
			for (ColliPack prodotto : prodotti)
				em.merge(prodotto);
			t.commit();
		} catch (Exception e) {
			collo = null;
			logger.error(e);
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return collo;
	}

	@Override
	public ColloCaricoJSON eliminaCollo(ColloCaricoJSON collo) {
		PakiTesta carico = checkCarico(collo.getCarico());
		ColliCarico esistente = checkCollo(collo.getId());
		List<ColliPack> prodotti = daoColliPack.trovaProdottiNelCollo(esistente.getKeyColloCar());
		//List<PakiArticolo> righe = new LinkedList<>();
		//Decremento le quantità in pakitesta e i pakiarticolo coinvolti, elimino il collicarico, elimino i collipack
		EntityManager em = getManager();
		EntityTransaction t = em.getTransaction();
		try {
			t.begin();
			em.remove(em.contains(esistente) ? esistente : em.find(ColliCarico.class, esistente.getIdCollo()));
			for (ColliPack prodotto : prodotti) {
				//Trovo la riga e abbasso la quantità verificata, anche sulla testata.
				PakiArticolo riga = em.find(PakiArticolo.class, prodotto.getIdPakiarticolo());
				riga.setQtaVerificata(riga.getQtaVerificata() - prodotto.getQta());
				carico.setQtaTotAre(carico.getQtaTotAre() - prodotto.getQta());
				//Se la riga è rimasta con quantità verificata a 0 e il dichiarato non c'era la elimino.
				if (riga.getQtaPaki() == 0 && riga.getQtaVerificata() == 0)	em.remove(riga);
				else em.merge(riga);
				em.remove(em.contains(prodotto) ? prodotto : em.find(ColliPack.class, prodotto.getIdColliPack()));
			}
			em.merge(carico);
			t.commit();
		} catch (Exception e) {
			collo = null;
			logger.error(e);
			if (t != null && t.isActive())
				t.rollback();
		} finally {
			em.close();
		}
		return collo;
	}

}
