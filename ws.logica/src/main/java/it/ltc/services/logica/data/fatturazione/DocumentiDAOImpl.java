package it.ltc.services.logica.data.fatturazione;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.Documento;
import it.ltc.database.model.centrale.Documento.TipoDocumento;

@Repository
public class DocumentiDAOImpl extends CRUDDao<Documento> implements DocumentiDAO {

	public DocumentiDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, Documento.class);
	}

	@Override
	public List<Documento> trovaTutti() {
		List<Documento> list = findAll();
		return list;
	}

	@Override
	public Documento trova(int id) {
		Documento entity = findByID(id);
		return entity;
	}
	
	public Documento cerca(int idCommessa, String riferimento, TipoDocumento tipo) {
		EntityManager em = getManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Documento> criteria = cb.createQuery(Documento.class);
        Root<Documento> member = criteria.from(Documento.class);
        Predicate condizioneCommessa = cb.equal(member.get("idCommessa"), idCommessa);
        Predicate condizioneRiferimento = cb.equal(member.get("riferimentoCliente"), riferimento);
        Predicate condizioneTipo = cb.equal(member.get("tipo"), tipo);
        criteria.select(member).where(cb.and(condizioneCommessa, condizioneRiferimento, condizioneTipo));
		List<Documento> lista = em.createQuery(criteria).setMaxResults(1).getResultList();
		em.close();
		Documento documento = lista.isEmpty() ? null : lista.get(0);
		return documento;
	}

	@Override
	public Documento inserisci(Documento documento) {
		Documento esistente = cerca(documento.getIdCommessa(), documento.getRiferimentoCliente(), documento.getTipo());
		if (esistente == null) {
			esistente = insert(documento);
		}
		return esistente;
	}

	@Override
	public Documento aggiorna(Documento documento) {
		Documento entity = update(documento, documento.getId());
		return entity;
	}

	@Override
	public Documento elimina(Documento documento) {
		Documento entity = delete(documento.getId());
		return entity;
	}

	@Override
	protected void updateValues(Documento oldEntity, Documento entity) {
		oldEntity.setDataCreazione(entity.getDataCreazione());
		oldEntity.setIdCommessa(entity.getIdCommessa());
		oldEntity.setRiferimentoCliente(entity.getRiferimentoCliente());
		oldEntity.setRiferimentoInterno(entity.getRiferimentoInterno());
		oldEntity.setTipo(entity.getTipo());
	}

}
