package it.ltc.services.logica.controller.common;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.ltc.database.dao.common.FeatureDao;
import it.ltc.database.model.centrale.Feature;

@Controller
@RequestMapping("/feature")
public class FeatureController {
	
	private final FeatureDao dao;
    
    public FeatureController() {
    	dao = new FeatureDao();
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Feature> listAllMembers() {
        return dao.trovaTutte();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Feature lookupMemberById(@PathVariable("id") String id) {
        return dao.trovaDaNome(id);
    }

}
