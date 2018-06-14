package it.ltc.services.logica.controller.trasporti;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.ltc.database.dao.common.CorriereDao;
import it.ltc.database.dao.common.CorriereServizioDao;
import it.ltc.database.model.centrale.Corriere;
import it.ltc.database.model.centrale.CorriereServizio;

@Controller
@RequestMapping("/corriere")
public class CorrieriController {
	
	private final CorriereDao daoCorrieri;
	private final CorriereServizioDao daoServizi;
    
    public CorrieriController() {
    	daoCorrieri = new CorriereDao();
    	daoServizi = new CorriereServizioDao();
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Corriere> getCorrieri() {
        return daoCorrieri.trovaTutti();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Corriere getCorriereById(@PathVariable("id") Integer id) {
        return daoCorrieri.trovaDaID(id);
    }
    
    @RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/servizio")
    public @ResponseBody List<CorriereServizio> getServizi() {
        return daoServizi.trovaTutti();
    }

}
