package com.example.util;

import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.function.Function;

@Component
public class MenuEvents {
    @Autowired
    private MenuEventsProperties menuEventsProperties;

    private Map<String, List<String>> destToMenuEventMap = new HashMap<>();

    public Map<String, List<String>> getDestToMenuEventMap() {
        return destToMenuEventMap;
    }

    @AACacheableInit(cacheFilePathId = "cacheFilePath", dataField = "destToMenuEventMap")
    @PostConstruct
    public void init() {
        Map<String, Map<String, EventDetail>> menuEvents = menuEventsProperties.getMenuEvents();
        for (Map.Entry<String, Map<String, EventDetail>> entry : menuEvents.entrySet()) {
            String menuId = entry.getKey();
            Map<String, EventDetail> eventDetails = entry.getValue();
            for (Map.Entry<String, EventDetail> eventEntry : eventDetails.entrySet()) {
                String eventId = eventEntry.getKey();
                EventDetail eventDetail = eventEntry.getValue();
                String dest = eventDetail.getDest();
                if (dest != null) {
                    destToMenuEventMap.computeIfAbsent(dest, k -> new ArrayList<>()).add(menuId + ":" + eventId);
                }
            }
        }
    }
}
