package it.ltc.services.logica.data.crm;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.AziendaContatti;
import it.ltc.database.model.centrale.Contatto;

@Repository
public class ContattoDAOImpl extends CRUDDao<Contatto> implements ContattoDAO {
	
	@Autowired
	private AssociazioneAziendaContattiDAO aziendaContattiDao;

	public ContattoDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, Contatto.class);
	}

	@Override
	public List<Contatto> trovaTutti() {
		List<Contatto> entities = findAll();
		return entities;
	}

	@Override
	public List<Contatto> trovaDaAzienda(int idAzienda) {
//		EntityManager em = getManager();
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<AziendaContatti> criteria = cb.createQuery(AziendaContatti.class);
//        Root<AziendaContatti> member = criteria.from(AziendaContatti.class);
//        criteria.select(member).where(cb.equal(member.get("azienda"), idAzienda));
//		List<AziendaContatti> lista = em.createQuery(criteria).getResultList();
//		em.close();
		List<AziendaContatti> lista = aziendaContattiDao.trovaDaAzienda(idAzienda);
		List<Contatto> contatti = new LinkedList<Contatto>();
		for (AziendaContatti match : lista) {
			Contatto contatto = trova(match.getContatto());
			if (contatto != null)
				contatti.add(contatto);
		}
		return contatti;
	}
	
	@Override
	public List<Contatto> trovaDaNome(String nome) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Contatto> criteria = cb.createQuery(Contatto.class);
        Root<Contatto> member = criteria.from(Contatto.class);
        Predicate condizioneNome = cb.like(member.get("nome"), nome + "%");
        Predicate condizioneCognome = cb.like(member.get("cognome"), nome + "%");
        criteria.select(member).where(cb.or(condizioneNome, condizioneCognome));
		List<Contatto> lista = em.createQuery(criteria).getResultList();
		em.close();
		return lista;
	}

	@Override
	public Contatto trova(int id) {
		Contatto entity = findByID(id);
		return entity;
	}

	@Override
	public Contatto inserisci(Contatto contatto) {
		Contatto entity = insert(contatto);
		return entity;
	}

	@Override
	public Contatto aggiorna(Contatto contatto) {
		Contatto entity = update(contatto, contatto.getId());
		return entity;
	}

	@Override
	public Contatto elimina(Contatto contatto) {
		Contatto entity = delete(contatto.getId());
		return entity;
	}

	@Override
	protected void updateValues(Contatto oldEntity, Contatto entity) {
		oldEntity.setCognome(entity.getCognome());
		//oldEntity.setDataDiNascita(entity.getDataDiNascita());
		//oldEntity.setEmail(entity.getEmail());
		oldEntity.setIndirizzo(entity.getIndirizzo());
		oldEntity.setNome(entity.getNome());
		oldEntity.setRuolo(entity.getRuolo());
		//oldEntity.setTelefono(entity.getTelefono());
		//oldEntity.setTitolo(entity.getTitolo());
		oldEntity.setDescrizione(entity.getDescrizione());
	}

}
