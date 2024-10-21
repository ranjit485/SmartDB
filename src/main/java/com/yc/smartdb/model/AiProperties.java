package com.yc.smartdb.model;

import org.springframework.stereotype.Component;

@Component
public class AiProperties {
    String modalName;
    String apiKey;

    public String getModalName() {
        return modalName;
    }

    public void setModalName(String modalName) {
        this.modalName = modalName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
