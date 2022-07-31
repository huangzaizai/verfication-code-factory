package com.juxinma.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @Description 自动执行检查类: 对组件的自定义参数、模版图片等进行自动检查
 * @Author 黄名富
 * @Date 2022/7/31 17:46
 * @Version 1.0
 **/
public class Inspector{

    public void doCheck() {
        checkPuzzleParameter();
        checkTemplate();
    }

    /**
     * 自定义参数检查
     */
    private void checkPuzzleParameter() {
        PuzzleParameter puzzleParameter = PuzzleParameter.getInstance();

        int mainWidth = puzzleParameter.getMainWidth();
        double cutoutWidth = mainWidth *  puzzleParameter.getMainWithCutoutRate();
        if(puzzleParameter.getStartX() +  cutoutWidth >  mainWidth)  throw new RuntimeException("模版在主图X轴的开始位置不合法");
        if(puzzleParameter.getStartY() >  puzzleParameter.getMainHeight()) throw new RuntimeException("模版在主图Y轴的开始位置不合法");
    }

    /**
     * 自定义模版图片检查
     */
    private void checkTemplate()  {
        URL url = Thread.currentThread().getContextClassLoader().getResource(PuzzleParameter.getInstance().getTemplatePath());
        if(url ==  null) throw new RuntimeException("模版不存在");
        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        String[] folderList = file.list();
        String templatePath = PuzzleParameter.getInstance().getTemplatePath();
        if(folderList == null || folderList.length == 0) throw new RuntimeException("模版不存在");
        for(String folderName : folderList)  {
            folderName = templatePath + folderName;
            BufferedImage mainTemplate = ImageUtils.getImage(folderName,"main");
            BufferedImage cutoutTemplate = ImageUtils.getImage(folderName,"cutout");
            if(mainTemplate == null || cutoutTemplate == null) {
                throw new RuntimeException("模版图片不合法");
            }
        }
    }

}
