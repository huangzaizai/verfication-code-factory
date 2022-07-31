package com.juxinma.common;

import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 图片工具类
 * @Author 黄名富
 * @Date 2022/7/24 22:56
 * @Version 1.0
 **/
public class ImageUtils {


    private static PuzzleParameter puzzleParameter  =  PuzzleParameter.getInstance();
    private static Map<String, BufferedImage> images = new HashMap<>();

    /**
     * 获取图片
     *
     * @param folderName
     *          文件夹路径
     * @param type
     *         类型 main 主图  cutout 抠图
     * @return  图片
     */
    public static BufferedImage getImage(String folderName, String type) {
        String key = folderName + type;
        if(images.get(key) == null) {
            BufferedImage image = getImageFromResourcePath(folderName + "/" + (type.equals("main") ? puzzleParameter.getTemplateMainName() : puzzleParameter.getTemplateCutoutName()));
            double height = 0;
            double width = 0;
            if(type.equals("main")) {
                width = puzzleParameter.getMainWidth();
                height = puzzleParameter.getMainHeight();
                if((height / width) < (image.getHeight() / image.getWidth())) {
                    throw new RuntimeException("主图高宽比大于:" + height /  width);
                }
            } else {
                width = puzzleParameter.getMainWidth() * puzzleParameter.getMainWithCutoutRate();
                height = width * (image.getHeight() / image.getWidth());
                if(height > puzzleParameter.getMainHeight()) {
                    throw new RuntimeException("抠图模版高宽比大于：" +  puzzleParameter.getMainHeight() / (puzzleParameter.getMainHeight() * puzzleParameter.getMainWithCutoutRate()));
                }
            }
            images.put(key,optimizeImage(image,width,height));
        }
        return images.get(key);
    }

    /**
     * 优化图片
     *
     * @param image
     *         待优化图片
     * @param width
     *          图片宽度
     * @param height
     *          图片高度
     *
     * @return 优化后的图片
     */
    private static BufferedImage optimizeImage(BufferedImage image, double width, double height) {
        BufferedImage newImage = new BufferedImage((int)width, (int)height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = newImage.getGraphics();
        int tempHeight = (int)(width * image.getHeight() / image.getWidth());
        graphics.drawImage(image.getScaledInstance((int)width, tempHeight, Image.SCALE_SMOOTH), 0, 0, null);
        graphics.dispose();
        return newImage;
    }

    /**
     * 抠图
     *
     * @param mainImage
     *        主图
     * @param templateImage
     *        抠图模版
     * @param x
     *        x轴位置
     * @param y
     *        y轴位置
     * @return 抠图
     */
    public static BufferedImage cutoutImage(BufferedImage mainImage, BufferedImage templateImage, int x, int y) {

        Shape imageShape = null;
        try {
            imageShape = getImageShape(templateImage);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }

        int templateWidth = templateImage.getWidth();
        int templateHeight = templateImage.getHeight();
        BufferedImage image = new BufferedImage(templateWidth, templateHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.clip(imageShape);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        graphics.drawImage(mainImage, -x, -y , mainImage.getWidth(), mainImage.getHeight(), null);

        graphics.dispose();
        return image;
    }

    /**
     * 图片覆盖
     *
     * @param mainImage
     *          主图
     * @param coverImage
     *          覆盖图
     * @param x
     *          x轴位置
     * @param y
     *          y轴位置
     * @return
     */
    public static BufferedImage overlayImage(BufferedImage mainImage, BufferedImage coverImage,int x, int y) {
        Graphics2D graphics = mainImage.createGraphics();
        graphics.drawImage(coverImage,x,y,coverImage.getWidth(),coverImage.getHeight(),null);
        graphics.dispose();
        return mainImage;
    }

    /**
     * 将图片转化成base64格式
     *
     * @param image
     *          图片流
     * @return base64字符串
     */
    public static String coverImageToBase64(BufferedImage image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        byte[] bytes = byteArrayOutputStream.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        String png_base64 = encoder.encodeBuffer(bytes).trim();
        png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");
        return "data:image/jpg;base64," + png_base64;
    }

    /**
     * 拷贝图片
     * @param image
     *          原版
     * @return 拷贝图片
     */
    public static BufferedImage copyImage(BufferedImage image) {
        final BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = newImage.getGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return newImage;
    }

    /**
     * 获取图片形状(从"天爱有情 https://www.tianai.cloud/"处拷贝该方法)
     *
     * @param img
     *         图片
     * @return 图片形状
     */
    private static Shape getImageShape(Image img) throws InterruptedException {
        ArrayList<Integer> x = new ArrayList<>();
        ArrayList<Integer> y = new ArrayList<>();
        int width = img.getWidth(null);
        int height = img.getHeight(null);

        // 首先获取图像所有的像素信息
        PixelGrabber pgr = new PixelGrabber(img, 0, 0, -1, -1, true);
        pgr.grabPixels();
        int[] pixels = (int[]) pgr.getPixels();

        // 循环像素
        for (int i = 0; i < pixels.length; i++) {
            // 筛选，将不透明的像素的坐标加入到坐标ArrayList x和y中
            int alpha = (pixels[i] >> 24) & 0xff;
            if (alpha != 0) {
                x.add(i % width > 0 ? i % width - 1 : 0);
                y.add(i % width == 0 ? (i == 0 ? 0 : i / width - 1) : i / width);
            }
        }

        // 建立图像矩阵并初始化(0为透明,1为不透明)
        int[][] matrix = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] = 0;
            }
        }

        // 导入坐标ArrayList中的不透明坐标信息
        for (int c = 0; c < x.size(); c++) {
            matrix[y.get(c)][x.get(c)] = 1;
        }

        /*
         * 逐一水平"扫描"图像矩阵的每一行，将透明（这里也可以取不透明的）的像素生成为Rectangle，
         * 再将每一行的Rectangle通过Area类的rec对象进行合并， 最后形成一个完整的Shape图形
         */
        Area rec = new Area();
        int temp = 0;
        //生成Shape时是1取透明区域还是取非透明区域的flag
        int flag = 1;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (matrix[i][j] == flag) {
                    if (temp == 0) {
                        temp = j;
                    }
                } else {
                    if (temp != 0) {
                        rec.add(new Area(new Rectangle(temp, i, j - temp, 1)));
                        temp = 0;
                    }
                }
            }
            temp = 0;
        }
        return rec;
    }

    /**
     * 获取图片
     *
     * @param path
     *       resources 路径
     * @return 图片
     */
    private static BufferedImage getImageFromResourcePath(String path) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        try {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("图片创建失败:" + e.getMessage());
        }
    }

}
