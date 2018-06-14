package it.ltc.services.sede.data.carico;

import it.ltc.services.sede.model.carico.ColloCaricoJSON;

public interface RiscontroColliDAO {
	
	public ColloCaricoJSON nuovoCollo(ColloCaricoJSON collo);
	
	public ColloCaricoJSON aggiornaCollo(ColloCaricoJSON collo);
	
	public ColloCaricoJSON eliminaCollo(ColloCaricoJSON collo);

}
