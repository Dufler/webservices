package it.ltc.services.sede.data.carico;

import java.util.List;

import it.ltc.services.sede.model.carico.ColloCaricoJSON;

public interface RiscontroColliDAO {
	
	public ColloCaricoJSON nuovoCollo(ColloCaricoJSON collo);
	
	public ColloCaricoJSON aggiornaCollo(ColloCaricoJSON collo);
	
	public ColloCaricoJSON eliminaCollo(ColloCaricoJSON collo);

	public List<ColloCaricoJSON> trovaColli(ColloCaricoJSON collo);

}
