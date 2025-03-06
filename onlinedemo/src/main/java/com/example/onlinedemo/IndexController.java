package com.example.onlinedemo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    // "/test"でアクセスされたら、ページを表示する
    @RequestMapping("/test")
    public String index() {
        // "/tmp"にFiles.createTempFileでファイルを作成し、タイムスタンプを書き出す
        // prefixはtimestamp、suffixは.txtとする
        try {
            Path path = Files.createTempFile(Paths.get("/tmp"), "timestamp", ".txt");
            Files.writeString(path, LocalDateTime.now().toString());
            // ファイルのパスとパーミッションを表示する
            return """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <title>表示完了</title>
                    </head>
                    <body>
                        <h1>正常に表示が完了しました</h1>
                        <p>タイムスタンプ: %s</p>
                        <p>ファイルのパス: %s</p>
                        <p>ファイルのパーミッション: %s</p>
                    </body>
                    </html>
                    """.formatted(LocalDateTime.now(), path, Files.getPosixFilePermissions(path));
        } catch (IOException e) {
            e.printStackTrace();
            return "ファイルの作成に失敗しました";
        }
    }

}
