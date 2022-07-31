package com.juxinma.common;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;

/**
 * @Description 图片路径提供者
 * @Author 黄名富
 * @Date 2022/7/24 23:48
 * @Version 1.0
 **/
public class TemplateFolderPathProvider {

    private final String[] templateFileNameList;
    private final Random random;

    private TemplateFolderPathProvider(){
        URL url = Thread.currentThread().getContextClassLoader().getResource(PuzzleParameter.getInstance().getTemplatePath());
        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        templateFileNameList = file.list();
        random = new Random();
    }

    private static TemplateFolderPathProvider instance = new TemplateFolderPathProvider();

    public static TemplateFolderPathProvider getInstance(){
        return instance;
    }

    public String randomTemplateFolderPath() {
        int pos = random.nextInt(templateFileNameList.length);
        return PuzzleParameter.getInstance().getTemplatePath() + templateFileNameList[pos];
    }

}
