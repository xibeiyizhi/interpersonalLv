package club.own.site.controller;

import club.own.site.bean.BlogItem;
import club.own.site.bean.Member;
import club.own.site.bean.ProdItem;
import club.own.site.config.redis.RedisClient;
import club.own.site.enums.ProductionTypeEnum;
import club.own.site.utils.EncodeUtils;
import club.own.site.utils.FileUploadUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static club.own.site.constant.ProjectConstant.*;
import static club.own.site.utils.FileUploadUtils.getBasePath;

@Slf4j
@Controller
public class ProdController extends BaseController{

    @Autowired
    private RedisClient redisClient;

    @GetMapping(value = "prod/uploadpage")
    public ModelAndView uploadPage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("productionTypes", ProductionTypeEnum.values());
        mav.setViewName("uploadprod");
        return mav;
    }

    @PostMapping(value = "prod/upload")
    public @ResponseBody String upload(HttpServletRequest request) {
        Map<String, String> params = parseRequestParam(request.getParameterMap());
        int typeCode = Integer.valueOf(params.get("code"));
        if (typeCode == 0) {
            typeCode = 3;
        }
        String typeName = ProductionTypeEnum.getNameByCode(typeCode);
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("photoArr");
        String relativePath = "static/img/prod/";
        if (CollectionUtils.isNotEmpty(files)) {
            for (int i=0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                if (file == null || file.isEmpty()) {
                    continue;
                }
                long id = System.currentTimeMillis();
                String desFileName = id + ".jpg";
                String dirPath = getBasePath() + relativePath;
                boolean isSuccess = FileUploadUtils.uploadFile(file, dirPath, desFileName);
                if (isSuccess) {
                    redisClient.rpush(PROD_CATE_LIST_KEY + typeName, String.valueOf(id));
                }
            }
        }

        return "OK";
    }
}
