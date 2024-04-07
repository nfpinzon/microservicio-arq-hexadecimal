package com.npinzon.microservice.msproducto.application;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.npinzon.microservice.msproducto.domain.models.documents.Repositorio;

@Service
public class DroolsService {

    @Autowired
    private KieContainer kieContainer;

    public void executeRuleRepo(Repositorio repo) {
     KieSession kieSession = kieContainer.newKieSession();
        kieSession.insert(repo);
        kieSession.fireAllRules();
        kieSession.dispose();
    }

}

