package it.ltc.services.sede.data.carico;

import it.ltc.database.model.legacy.model.StatoCarico;
import it.ltc.services.sede.model.carico.ColloInRiscontroJSON;

public interface RiscontroCaricoDAO {
	
	public boolean cambiaStatoCarico(int idCarico, StatoCarico stato);
	
	public ColloInRiscontroJSON nuovoCollo(ColloInRiscontroJSON collo);
	
	public ColloInRiscontroJSON aggiornaCollo(ColloInRiscontroJSON collo);
	
	public ColloInRiscontroJSON eliminaCollo(ColloInRiscontroJSON collo);

}
