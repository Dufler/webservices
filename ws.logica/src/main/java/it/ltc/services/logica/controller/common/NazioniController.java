package it.ltc.services.logica.controller.common;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.ltc.database.dao.common.NazioneDao;
import it.ltc.database.model.centrale.Nazione;

@Controller
@RequestMapping("/nazione")
public class NazioniController {
	
	private final NazioneDao dao;
    
    public NazioniController() {
    	dao = NazioneDao.getInstance();
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Nazione> listAllMembers() {
        return dao.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Nazione lookupMemberById(@PathVariable("id") String id) {
        return dao.findByCodiceISO3(id);
    }

}
