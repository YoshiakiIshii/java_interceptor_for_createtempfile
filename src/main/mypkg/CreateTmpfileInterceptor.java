package mypkg;

// java.nio.file.Files.createTempFile()に対するインターセプタクラス
// SpringFrameworkのAOPを使用
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermissions;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CreateTmpfileInterceptor {

    // Files.createTempFile()メソッドに対するインターセプタ
    // 4つ目以降の引数であるFileAttribute<?>... attrsが省略された場合に、
    // "rwxrwxrwx"のパーミッションを引数に追加してFiles.createTempFile()メソッドを実行
    @Around("execution(* java.nio.file.Files.createTempFile(java.nio.file.Path, String, String))")
    public Object createTempFile(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        try {
            // Files.createTempFile()メソッドの引数を取得
            Object[] args = joinPoint.getArgs();
            // パーミッションを追加して再設定
            // 引数のPathオブジェクトを取得
            Path path = (Path) args[0];
            // 引数のprefixを取得
            String prefix = (String) args[1];
            // 引数のsuffixを取得
            String suffix = (String) args[2];
            // 引数のFileAttribute<?>[]を作成
            FileAttribute<?>[] attrs = new FileAttribute<?>[] {
                    PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxrwxrwx")) };
            // Files.createTempFile()メソッドを再設定
            result = joinPoint.proceed(new Object[] { path, prefix, suffix, attrs });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
