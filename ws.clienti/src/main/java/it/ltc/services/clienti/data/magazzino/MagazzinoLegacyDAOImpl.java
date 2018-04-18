package it.ltc.services.clienti.data.magazzino;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import it.ltc.database.dao.Dao;
import it.ltc.database.model.legacy.Magazzini;

public class MagazzinoLegacyDAOImpl extends Dao implements MagazzinoDAO<Magazzini> {

	public MagazzinoLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
	}

	@Override
	public Magazzini findByID(int idMagazzino) {
		EntityManager em = getManager();
		Magazzini magazzino = em.find(Magazzini.class, idMagazzino);
		em.close();
		return magazzino;
	}

	@Override
	public Magazzini findByCodiceLTC(String codice) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Magazzini> criteria = cb.createQuery(Magazzini.class);
        Root<Magazzini> member = criteria.from(Magazzini.class);
        criteria.select(member).where(cb.equal(member.get("codiceMag"), codice));
        List<Magazzini> list = em.createQuery(criteria).setMaxResults(1).getResultList();
        em.close();
        Magazzini magazzino = list.isEmpty() ? null : list.get(0);
		return magazzino;
	}

	@Override
	public Magazzini findByCodificaCliente(String codifica) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Magazzini> criteria = cb.createQuery(Magazzini.class);
        Root<Magazzini> member = criteria.from(Magazzini.class);
        criteria.select(member).where(cb.equal(member.get("magaCliente"), codifica));
        List<Magazzini> list = em.createQuery(criteria).setMaxResults(1).getResultList();
        em.close();
        Magazzini magazzino = list.isEmpty() ? null : list.get(0);
		return magazzino;
	}

	@Override
	public List<Magazzini> findAll() {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Magazzini> criteria = cb.createQuery(Magazzini.class);
        Root<Magazzini> member = criteria.from(Magazzini.class);
        criteria.select(member);
        List<Magazzini> list = em.createQuery(criteria).getResultList();
        em.close();
		return list;
	}

}
