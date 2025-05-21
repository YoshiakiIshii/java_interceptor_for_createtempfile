package com.example.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * AACacheFileHandlerは、指定されたファイルパスIDに基づいてキャッシュデータの保存および読み込みを行うユーティリティクラスです。
 * <p>
 * ファイルパスIDはSpringのApplicationContextから取得され、キャッシュデータはシリアライズされたオブジェクトとしてファイルに保存されます。
 * ファイルの存在確認やエラー処理も行い、キャッシュファイルの管理を簡便にします。
 * </p>
 *
 * <ul>
 *   <li>{@link #loadCacheFromFile(String)}: 指定されたファイルパスIDに対応するキャッシュファイルからオブジェクトを読み込みます。</li>
 *   <li>{@link #saveCacheToFile(String, Object)}: 指定されたファイルパスIDに対応するファイルへキャッシュデータを保存します。</li>
 * </ul>
 */
@Component
public class AACacheFileHandler {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 指定されたファイルパスIDに対応するキャッシュファイルからオブジェクトを読み込みます。
     * <p>
     * ファイルパスIDはSpringのApplicationContextから取得されます。<br>
     * ファイルが存在しない場合やファイルパスが空の場合はnullを返します。<br>
     * ファイルの読み込み中にエラーが発生した場合はRuntimeExceptionをスローします。
     * </p>
     *
     * @param filePathId ファイルパスを特定するためのBean ID
     * @return キャッシュファイルから読み込まれたオブジェクト、またはファイルが存在しない場合はnull
     * @throws RuntimeException キャッシュファイルの読み込みに失敗した場合
     */
    @SuppressWarnings("unchecked")
    public <T> T loadCacheFromFile(String filePathId) {
        String filePath = (String) applicationContext.getBean(filePathId);
        if (filePath != null && !filePath.isEmpty() && new File(filePath).exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
                return (T) ois.readObject();
            } catch (IOException | ClassNotFoundException | ClassCastException e) {
                throw new RuntimeException("キャッシュファイルの読み込みに失敗しました: " + filePath, e);
            }
        } else {
            return null;
        }
    }

    /**
     * 指定されたファイルパスIDに対応するファイルへキャッシュデータを保存します。
     *
     * <p>
     * ファイルパスIDからApplicationContextを通じてファイルパスを取得し、
     * 指定されたオブジェクトをシリアライズしてファイルに書き込みます。
     * </p>
     *
     * @param filePathId  ファイルパスを特定するためのBean ID
     * @param cacheData   保存するキャッシュデータ（シリアライズ可能なオブジェクト）
     * @throws RuntimeException キャッシュファイルの保存に失敗した場合
     */
    public <T> void saveCacheToFile(String filePathId, T cacheData) {
        String filePath = (String) applicationContext.getBean(filePathId);
        if (filePath != null && !filePath.isEmpty()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
                oos.writeObject(cacheData);
            } catch (IOException e) {
                throw new RuntimeException("キャッシュファイルの保存に失敗しました: " + filePath, e);
            }
        }
    }
}
