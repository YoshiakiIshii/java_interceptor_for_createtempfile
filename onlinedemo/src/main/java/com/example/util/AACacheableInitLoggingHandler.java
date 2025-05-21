package com.example.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

/**
 * {@code AACacheableInitLoggingHandler} は、Logbackのルートロガーのログレベルを一時的に変更し、
 * 元のログレベルに復元するためのユーティリティクラスです。
 * <p>
 * 主にキャッシュ初期化時など、一時的にログ出力レベルを変更したい場合に利用します。
 * </p>
 *
 * <ul>
 *   <li>{@link #elevateRootLogLevel()} でルートロガーのログレベルをINFOに変更し、元のレベルを保存します。</li>
 *   <li>{@link #restoreRootLogLevel()} で保存しておいた元のログレベルに戻します。</li>
 * </ul>
 *
 * <p>
 * このクラスはスレッドセーフではありません。単一スレッドでの利用を想定しています。
 * </p>
 */
public class AACacheableInitLoggingHandler {
    private ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory
            .getLogger(Logger.ROOT_LOGGER_NAME);
    private Level originalLogLevel;

    /**
     * ルートロガーのログレベルをINFOに一時的に引き上げます。
     * <p>
     * 現在のルートロガーのログレベルを{@code originalLogLevel}に保存し、
     * その後ルートロガーのログレベルを{@link ch.qos.logback.classic.Level#INFO}に設定します。
     * </p>
     * <p>
     * 元のログレベルは後で{@link #restoreRootLogLevel()}で復元できます。
     * </p>
     */
    public void elevateRootLogLevel() {
        // 現在のルートログレベルを取得
        originalLogLevel = rootLogger.getLevel();
        // ルートログレベルをINFOに設定
        rootLogger.setLevel(Level.INFO);
    }

    /**
     * ルートロガーのログレベルを元の値に復元します。
     * <p>
     * このメソッドは、一時的に変更されたルートロガーのログレベルを、
     * 事前に保存しておいた元のログレベルに戻します。
     * 主にログ設定の一時的な変更を元に戻す際に利用します。
     * </p>
     */
    public void restoreRootLogLevel() {
        // 退避しておいたルートログレベルに戻す
        rootLogger.setLevel(originalLogLevel);
    }
}
