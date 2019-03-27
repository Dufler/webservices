package it.ltc.services.logica.controller.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.ltc.database.dao.costanti.NazioneDao;
import it.ltc.database.model.costanti.Nazione;


@Controller
@RequestMapping("/nazione")
public class NazioniController {
	
	@Autowired
	private NazioneDao dao;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Nazione> listAllMembers() {
        return dao.trovaTutte();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Nazione lookupMemberById(@PathVariable("id") String id) {
        return dao.trovaDaCodiceISO3(id);
    }

}
