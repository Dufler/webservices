package it.ltc.services.clienti.data.magazzino;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;

import it.ltc.database.dao.Dao;
import it.ltc.database.model.legacy.Magazzini;
import it.ltc.services.clienti.model.prodotto.InfoProdotto;
import it.ltc.services.custom.exception.CustomException;

public class SaldiMagazzinoDAOImpl extends Dao implements SaldiMagazzinoDAO {
	
	private final MagazzinoLegacyDAOImpl daoMagazzini;
	//private final ProdottoLegacyDAOImpl daoProdotti;

	public SaldiMagazzinoDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		daoMagazzini = new MagazzinoLegacyDAOImpl(persistenceUnit);
		//daoProdotti = new ProdottoLegacyDAOImpl(persistenceUnit);
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public List<InfoProdotto> getDisponibilita(String codificaMagazzino) {
//		Magazzini magazzino = daoMagazzini.findByCodificaCliente(codificaMagazzino);
//		if (magazzino == null)
//			throw new CustomException("Il magazzino richiesto non esiste.");
//		EntityManager em = getManager();
//		List<Object[]> results = em.createQuery("SELECT a.codArtStr, m.disponibile, m.esistenza, m.codMaga FROM MagaSd m JOIN Articoli a ON m.idUniArticolo = a.idUniArticolo WHERE m.codMaga = '" + magazzino.getCodiceMag() + "'").getResultList();
//		em.getTransaction().commit();
//		em.close();
//		List<InfoProdotto> listaInfo = new LinkedList<InfoProdotto>();
//		for (Object[] result : results) {
//			String codiceArticolo = (String) result[0];
//			int disponibile = (int) result[1];
//			int esistenza = (int) result[2];
//			String codiceMagazzino = (String) result[3];
//			InfoProdotto info = new InfoProdotto();
//			info.setCodiceArticolo(codiceArticolo);
//			info.setDisponibilità(disponibile);
//			info.setGiacenza(esistenza);
//			info.setMagazzino(codiceMagazzino);
//			listaInfo.add(info);
//		}
//		return listaInfo;
//	}
	
	private InfoProdotto getInfoProdotto(Object[] arrayInfo) {
		InfoProdotto info = new InfoProdotto();
		info.setCodiceArticolo((String) arrayInfo[0]);
		info.setDisponibilità((int) arrayInfo[1]);
		info.setGiacenza((int) arrayInfo[2]);
		info.setMagazzino((String) arrayInfo[3]);
		return info;
	}
	
	@SuppressWarnings("unchecked")
	public List<InfoProdotto> getDisponibilita() {
		EntityManager em = getManager();
		List<Object[]> results = em.createNativeQuery("SELECT a.codArtStr AS codiceArticolo, m.disponibile as disponibilità, m.esistenza as giacenza, m.codMaga as magazzino FROM MagaSd m JOIN Articoli a ON m.idUniArticolo = a.idUniArticolo").getResultList();
		List<InfoProdotto> listaInfo = new LinkedList<InfoProdotto>();
		for (Object[] result : results) {
			InfoProdotto info = getInfoProdotto(result);
			listaInfo.add(info);
		}
		return listaInfo;
	}
	
	@SuppressWarnings("unchecked")
	public List<InfoProdotto> getDisponibilita(String codificaMagazzino) {
		Magazzini magazzino = daoMagazzini.findByCodificaCliente(codificaMagazzino);
		if (magazzino == null)
			throw new CustomException("Il magazzino richiesto non esiste.");
		EntityManager em = getManager();
		List<Object[]> results = em.createNativeQuery("SELECT a.codArtStr AS codiceArticolo, m.disponibile as disponibilità, m.esistenza as giacenza, m.codMaga as magazzino FROM MagaSd m JOIN Articoli a ON m.idUniArticolo = a.idUniArticolo WHERE m.codMaga = '" + magazzino.getCodiceMag() + "'").getResultList();
		List<InfoProdotto> listaInfo = new LinkedList<InfoProdotto>();
		for (Object[] result : results) {
			InfoProdotto info = getInfoProdotto(result);
			listaInfo.add(info);
		}
		return listaInfo;
	}
	
//	public List<InfoProdotto> getDisponibilita(String codificaMagazzino) {
//		Magazzini magazzino = daoMagazzini.findByCodificaCliente(codificaMagazzino);
//		if (magazzino == null)
//			throw new CustomException("Il magazzino richiesto non esiste.");
//		EntityManager em = getManager();
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<MagaSd> criteria = cb.createQuery(MagaSd.class);
//		Root<MagaSd> member = criteria.from(MagaSd.class);
//		Predicate condizioneMagazzino = cb.equal(member.get("codMaga"), magazzino.getCodiceMag());
//		criteria.select(member).where(condizioneMagazzino);
//		List<MagaSd> saldi = em.createQuery(criteria).getResultList();
//		List<InfoProdotto> listaInfo = new LinkedList<InfoProdotto>();
//		for (MagaSd saldo : saldi) {
//			Articoli prodotto = daoProdotti.findByIDUnivoco(saldo.getIdUniArticolo());
//			InfoProdotto info = new InfoProdotto();
//			info.setCodiceArticolo(prodotto.getCodArtStr());
//			info.setDisponibilità(saldo.getDisponibile());
//			info.setGiacenza(saldo.getEsistenza());
//			info.setMagazzino(saldo.getCodMaga());
//			listaInfo.add(info);
//		}
//		return listaInfo;
//	}

}
