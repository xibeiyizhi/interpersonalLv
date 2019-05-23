package club.own.site.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Component
public class FileUploadUtils {

    private static String basePath;

    @Value(value = "${basePath}")
    public void setBasePath(String basePath){
        FileUploadUtils.basePath = basePath;
    }

    public static String getBasePath() {
        if (StringUtils.isBlank(basePath)) {
            return basePath;
        }
        if (!basePath.endsWith("/")) {
            basePath += "/";
        }
        return basePath;
    }

    public static boolean uploadFile(MultipartFile file, String dirPath, String fileName) {
        BufferedOutputStream stream = null;
        try {
            byte[] bytes = file.getBytes();
            File dir = new File(dirPath);
            if (!dir.exists()) {
                boolean mk = dir.mkdirs();
                if (!mk) {
                    log.error("创建目录失败");
                    return false;
                }
            }
            String filePath = dir.getAbsolutePath() + "/" + fileName;
            stream = new BufferedOutputStream(new FileOutputStream(
                    new File(filePath)));
            stream.write(bytes);
            stream.close();
        } catch (Exception e) {
            stream = null;
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                log.error("close BufferedOutputStream error ", e);
            }
        }
        return true;
    }
}
