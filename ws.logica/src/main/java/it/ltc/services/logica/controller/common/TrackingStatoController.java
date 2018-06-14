package it.ltc.services.logica.controller.common;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.ltc.database.dao.common.TrackingStatoDao;
import it.ltc.database.model.centrale.TrackingStato;

@Controller
@RequestMapping("/trackingstato")
public class TrackingStatoController {
	
	private final TrackingStatoDao dao;
    
    public TrackingStatoController() {
    	dao = new TrackingStatoDao();
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<TrackingStato> listAllMembers() {
        return dao.trovaTutti();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody TrackingStato lookupMemberById(@PathVariable("id") String id) {
        return dao.trovaDaCodice(id);
    }

}
