package it.ltc.services.logica.data.crm;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.AziendaBrand;
import it.ltc.database.model.centrale.Brand;

@Repository
public class BrandDAOImpl extends CRUDDao<Brand> implements BrandDAO {
	
	@Autowired
	private AssociazioneAziendaBrandDAO aziendaBrandDao;

	public BrandDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, Brand.class);
	}

	@Override
	public List<Brand> trovaTutti() {
		List<Brand> entities = findAll();
		return entities;
	}

	@Override
	public List<Brand> trovaDaAzienda(int idAzienda) {
		List<AziendaBrand> lista = aziendaBrandDao.trovaDaAzienda(idAzienda);
		List<Brand> brands = new LinkedList<Brand>();
		for (AziendaBrand match : lista) {
			Brand brand = trova(match.getBrand());
			if (brand != null)
				brands.add(brand);
		}
		return brands;
	}
	
	@Override
	public List<Brand> trovaDaNome(String nome) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Brand> criteria = cb.createQuery(Brand.class);
        Root<Brand> member = criteria.from(Brand.class);
        criteria.select(member).where(cb.like(member.get("nome"), nome + "%"));
		List<Brand> lista = em.createQuery(criteria).getResultList();
		em.close();
		return lista;
	}

	@Override
	public Brand trova(int id) {
		Brand entity = findByID(id);
		return entity;
	}

	@Override
	public Brand inserisci(Brand brand) {
		Brand entity = insert(brand);
		return entity;
	}

	@Override
	public Brand aggiorna(Brand brand) {
		Brand entity = update(brand, brand.getId());
		return entity;
	}

	@Override
	public Brand elimina(Brand brand) {
		Brand entity = delete(brand.getId());
		return entity;
	}

	@Override
	protected void updateValues(Brand oldEntity, Brand entity) {
		oldEntity.setNome(entity.getNome());
		oldEntity.setDescrizione(entity.getDescrizione());
	}

}
