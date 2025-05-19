package com.example.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.stereotype.Component;

@Component
public class AACacheFileHandler {
    /**
     * キャッシュファイルが存在するかチェックする。
     * @param filePath チェックするファイルのパス
     * @return 存在する場合はtrue、存在しない場合はfalse
     */
    public boolean isCacheFileExists(String filePath) {
        return filePath != null && !filePath.isEmpty() && new File(filePath).exists();
    }
    /**
     * キャッシュをファイルから読み込む。
     * @param filePath 読み込むファイルのパス
     * @return 読み込んだキャッシュデータ
     * @throws IOException 入出力エラー
     * @throws ClassNotFoundException クラスが見つからないエラー
     */
    public Object loadCacheFromFile(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return ois.readObject();
        }
    }
    /**
     * キャッシュをファイルに保存する。
     * @param filePath 保存するファイルのパス
     * @param cacheData 保存するキャッシュデータ
     * @throws IOException 入出力エラー
     */
    public void saveCacheToFile(String filePath, Object cacheData) throws IOException {
        if (filePath != null && !filePath.isEmpty()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
                oos.writeObject(cacheData);
            }
        }
    }

}
