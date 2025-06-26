package com.example.util;

import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Component
public class MenuEvents {
    private static final Logger logger = LoggerFactory.getLogger(MenuEvents.class);

    @Autowired
    private MenuEventsProperties menuEventsProperties;

    private Map<String, List<String>> destToMenuEventMap;

    public Map<String, List<String>> getDestToMenuEventMap() {
        return destToMenuEventMap;
    }

    @Autowired
    private AACacheFileHandler cacheFileHandler;

    @PostConstruct
    public void init() {
        // 追加要素1: キャッシュ出力処理でD層のデバッグログが大量出力されるので、低レベルログを不要に設定する
        MDC.put("suppress", "true");

        logger.info("MenuEventsの初期化を開始します");

        // 追加要素2: キャッシュファイルの読み込み
        // 読み込みができない場合はnullを返すため、クラスフィールドでnewせず、以下のif文内でnewする
        destToMenuEventMap = cacheFileHandler.loadCacheFromFile("cacheFilePath");
        if (destToMenuEventMap == null) {

            // ---------- ここからが既存のキャッシュ作成処理 ----------
            logger.info("キャッシュファイルが存在しないため、初期化を行います");
            destToMenuEventMap = new HashMap<>();
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
                        // このデバッグログ出力が大量出力されるので、上記の追加要素1でログレベルを上げ、出力を抑制する
                        logger.debug("dest={} に {}:{} を追加", dest, menuId, eventId);
                    }
                }
            }
            // ---------- ここまでが既存のキャッシュ作成処理 ----------

            // 追加要素3: キャッシュファイルへの保存
            cacheFileHandler.saveCacheToFile("cacheFilePath", destToMenuEventMap);
        } else {
            // このelse文は無くても構わない
            logger.info("キャッシュファイルからデータを読み込みました");
        }
        logger.info("MenuEventsの初期化が完了しました");

        // 追加要素4: 低レベルログ不要設定を解除する
        MDC.remove("suppress");
    }
}
