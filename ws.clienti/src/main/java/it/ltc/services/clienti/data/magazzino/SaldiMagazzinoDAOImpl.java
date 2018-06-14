package it.ltc.services.clienti.data.magazzino;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;

import it.ltc.database.dao.Dao;
import it.ltc.database.dao.legacy.MagazzinoDao;
import it.ltc.database.model.legacy.Magazzini;
import it.ltc.model.shared.json.cliente.InfoProdotto;
import it.ltc.services.custom.exception.CustomException;

public class SaldiMagazzinoDAOImpl extends Dao implements SaldiMagazzinoDAO {
	
	private static final Logger logger = Logger.getLogger("SaldiMagazzinoDAOImpl");
	
	private final MagazzinoDao daoMagazzini;

	public SaldiMagazzinoDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		daoMagazzini = new MagazzinoDao(persistenceUnit);
	}
	
	private InfoProdotto getInfoProdotto(Object[] arrayInfo) {
		InfoProdotto info = new InfoProdotto();
		String sku = (String) arrayInfo[0];
		String taglia = (String) arrayInfo[1];
		String codiceArticolo = taglia != null && !taglia.isEmpty() ? sku + "|" + taglia : sku;
		info.setCodiceArticolo(codiceArticolo);
		info.setDisponibilità((int) arrayInfo[2]);
		info.setGiacenza((int) arrayInfo[3]);
		info.setMagazzino((String) arrayInfo[4]);
		return info;
	}
	
	@SuppressWarnings("unchecked")
	public List<InfoProdotto> getDisponibilita() {
		logger.info("Ottengo la disponibilità generale");
		EntityManager em = getManager();
		List<Object[]> results = em.createNativeQuery("SELECT a.codArtStr AS codiceArticolo, a.taglia AS taglia, m.disponibile as disponibilità, m.esistenza as giacenza, m.codMaga as magazzino FROM MagaSd m JOIN Articoli a ON m.idUniArticolo = a.idUniArticolo").getResultList();
		logger.info("Query completata.");
		em.close();
		List<InfoProdotto> listaInfo = new LinkedList<InfoProdotto>();
		for (Object[] result : results) {
			InfoProdotto info = getInfoProdotto(result);
			listaInfo.add(info);
		}
		return listaInfo;
	}
	
	@SuppressWarnings("unchecked")
	public List<InfoProdotto> getDisponibilita(String codificaMagazzino) {
		logger.info("Ottengo la disponibilità per il magazzino " + codificaMagazzino);
		Magazzini magazzino = daoMagazzini.trovaDaCodificaCliente(codificaMagazzino);
		if (magazzino == null)
			throw new CustomException("Il magazzino richiesto non esiste.");
		EntityManager em = getManager();
		List<Object[]> results = em.createNativeQuery("SELECT a.codArtStr AS codiceArticolo, a.taglia AS taglia, m.disponibile as disponibilità, m.esistenza as giacenza, m.codMaga as magazzino FROM MagaSd m JOIN Articoli a ON m.idUniArticolo = a.idUniArticolo WHERE m.codMaga = '" + magazzino.getCodiceMag() + "'").getResultList();
		logger.info("Query completata.");
		em.close();
		List<InfoProdotto> listaInfo = new LinkedList<InfoProdotto>();
		for (Object[] result : results) {
			InfoProdotto info = getInfoProdotto(result);
			listaInfo.add(info);
		}
		return listaInfo;
	}

}
