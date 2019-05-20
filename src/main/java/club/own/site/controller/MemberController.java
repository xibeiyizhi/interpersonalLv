package club.own.site.controller;

import club.own.site.bean.Member;
import club.own.site.config.redis.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Slf4j
@RestController
public class MemberController extends BaseController {

    @Autowired
    private RedisClient redisClient;

    @PostMapping(value = "member/add")
    public String add(Member member, HttpServletRequest request){
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("photoArr");
        String relativePath = "static/img/welcome/";
        URL basePath = getClass().getClassLoader().getResource("");
        BufferedOutputStream stream = null;
        if (CollectionUtils.isNotEmpty(files) && basePath != null) {
            for (MultipartFile file : files) {
                if (file == null || file.isEmpty()) {
                    continue;
                }
                String fileUrl = relativePath + file.getOriginalFilename();
                if (StringUtils.isBlank(member.getFirstImgUrl())){
                    member.setFirstImgUrl(fileUrl);
                }
                try {
                    byte[] bytes = file.getBytes();
                    File dir = new File(basePath.getPath() + relativePath);
                    if (!dir.exists()) {
                        boolean mk = dir.mkdirs();
                        if (!mk) {
                            return "创建文件目录失败";
                        }
                    }
                    String filePath = dir.getAbsolutePath() + "/" + file.getOriginalFilename();
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
            }
        }
        return "success";
    }
}
