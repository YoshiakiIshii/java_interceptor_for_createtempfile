package com.example.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "aa")
public class MenuEventsProperties {
    /**
     * menuEvents: {GAM0001: {E001: {...}, E002: {...}}, GAM0002: {...}}
     */
    private Map<String, Map<String, EventDetail>> menuEvents;

    public Map<String, Map<String, EventDetail>> getMenuEvents() {
        return menuEvents;
    }

    public void setMenuEvents(Map<String, Map<String, EventDetail>> menuEvents) {
        this.menuEvents = menuEvents;
    }
}