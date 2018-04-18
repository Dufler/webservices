package it.ltc.services.logica.data.trasporti;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.JoinCommessaCorriere;

@Repository
public class CodiciClienteCorriereDAOImpl extends CRUDDao<JoinCommessaCorriere> implements CodiciClienteCorriereDAO {

	public CodiciClienteCorriereDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, JoinCommessaCorriere.class);
	}

	@Override
	public List<JoinCommessaCorriere> trovaTutti() {
		List<JoinCommessaCorriere> lista = findAll();
        return lista;
	}

	@Override
	public JoinCommessaCorriere trova(String codice) {
		JoinCommessaCorriere codiceClienteCorriere = findByID(codice);
		return codiceClienteCorriere;
	}

	@Override
	public JoinCommessaCorriere inserisci(JoinCommessaCorriere codice) {
		JoinCommessaCorriere entity = insert(codice);
		return entity;
	}

	@Override
	public JoinCommessaCorriere aggiorna(JoinCommessaCorriere codice) {
		JoinCommessaCorriere entity = update(codice, codice.getCodiceCliente());
		return entity;
	}
	
	@Override
	protected void updateValues(JoinCommessaCorriere oldEntity, JoinCommessaCorriere entity) {
		oldEntity.setCommessa(entity.getCommessa());
		oldEntity.setCorriere(entity.getCorriere());
		oldEntity.setDescrizione(entity.getDescrizione());
		oldEntity.setStato(entity.getStato());
	}

	@Override
	public JoinCommessaCorriere elimina(JoinCommessaCorriere codice) {
		JoinCommessaCorriere entity = delete(codice.getCodiceCliente());
		return entity;
	}

}
