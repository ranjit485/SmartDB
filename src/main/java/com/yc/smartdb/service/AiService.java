package com.yc.smartdb.service;

import com.yc.smartdb.App;
import com.yc.smartdb.model.AiProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    public void setAiProps(String apiKey){
        ApplicationContext context = App.getContext();
        AiProperties aiProperties = context.getBean(AiProperties.class);
//        TODO Scanner add here
    }
}
