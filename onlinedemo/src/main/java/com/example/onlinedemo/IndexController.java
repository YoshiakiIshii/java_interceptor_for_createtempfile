package com.example.onlinedemo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.util.MenuEvents;

@RestController
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
 
    @Autowired
    private MenuEvents menuEvents;

    // "/test"でアクセスされたら、ページを表示する
    @RequestMapping("/test")
    public String index() {
        logger.debug("/testエンドポイントへのリクエストを受信しました");
        // MenuEventsからdestToMenuEventMapを取得し、テキスト化してリターンする
        Map<String, List<String>> destToMenuEventMap = menuEvents.getDestToMenuEventMap();
        logger.info("destToMenuEventMapを取得しました。宛先数: {}", destToMenuEventMap.size());
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h1>Menu Events</h1>");
        sb.append("<p>Generated at: " + LocalDateTime.now() + "</p>");
        sb.append("<table border='1'>");
        sb.append("<tr><th>Destination</th><th>Menu Events</th></tr>");
        for (Map.Entry<String, List<String>> entry : destToMenuEventMap.entrySet()) {
            String dest = entry.getKey();
            List<String> menuEventsList = entry.getValue();
            logger.debug("宛先: {} の処理を開始します。メニューイベント数: {}", dest, menuEventsList.size());
            sb.append("<tr><td>" + dest + "</td><td>");
            for (String menuEvent : menuEventsList) {
                sb.append(menuEvent + "<br>");
            }
            sb.append("</td></tr>");
        }
        sb.append("</table>");
        sb.append("</body></html>");
        logger.info("/testエンドポイントのレスポンスを正常に生成しました");
        return sb.toString();
    }

}
