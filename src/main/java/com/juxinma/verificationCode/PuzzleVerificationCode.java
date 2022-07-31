package com.juxinma.verificationCode;

import com.juxinma.common.PuzzleParameter;
import com.juxinma.common.Aes;
import com.juxinma.common.ImageUtils;
import com.juxinma.common.TemplateFolderPathProvider;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 滑动验证码
 * @Author 黄名富
 * @Date 2022/7/24 22:08
 * @Version 1.0
 **/
public class PuzzleVerificationCode extends VerificationCode{

    /**
     * 主图 base64 格式
     */
    private String mainImage;

    /**
     * 抠图 base64 格式
     */
    private String cutoutImage;

    /**
     * 主图原图 base64 格式
     */
    private String originalImage;

    /**
     * 抠图在主图的起始位置Y轴坐标
     */
    private int positionY;

    /**
     * 主图宽度
     */
    private int mainWidth;

    /**
     * 主图长度
     */
    private int mainHeight;

    /**
     * 抠图宽度
     */
    private int cutoutWidth;

    /**
     * 抠图高度
     */
    private int cutoutHeight;

    /**
     * AES对称加密信息 内容: x 位置的百分比 抠图左边 x值 / 主图宽度 = 百分比;
     */
    private String token;

//    private PuzzleVerificationCode() {}

    @Override
    public VerificationCode generate() {

        String folderName = TemplateFolderPathProvider.getInstance().randomTemplateFolderPath();
        BufferedImage mainTemplate = ImageUtils.getImage(folderName,"main");
        BufferedImage cutoutTemplate = ImageUtils.getImage(folderName,"cutout");

        int positionX = randomPosition(mainTemplate.getWidth(),cutoutTemplate.getWidth(), PuzzleParameter.getInstance().getStartX());
        int positionY = randomPosition(mainTemplate.getHeight(), cutoutTemplate.getHeight(), PuzzleParameter.getInstance().getStartY());
        BufferedImage cutoutImage = ImageUtils.cutoutImage(mainTemplate, cutoutTemplate, positionX, positionY);
        BufferedImage overlayImage = ImageUtils.overlayImage(ImageUtils.copyImage(mainTemplate), cutoutTemplate, positionX, positionY);

        PuzzleVerificationCode verificationCode = new PuzzleVerificationCode();
        verificationCode.setMainImage(ImageUtils.coverImageToBase64(overlayImage));
        verificationCode.setCutoutImage(ImageUtils.coverImageToBase64(cutoutImage));
        verificationCode.setOriginalImage(ImageUtils.coverImageToBase64(mainTemplate));
        verificationCode.setPositionY(positionY);
        verificationCode.setMainWidth(mainTemplate.getWidth());
        verificationCode.setMainHeight(mainTemplate.getHeight());
        verificationCode.setCutoutWidth(cutoutImage.getWidth());
        verificationCode.setCutoutHeight(cutoutImage.getHeight());
        Double rateX = (double) positionX / (double) mainTemplate.getWidth();
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("rate",rateX.toString());
        tokenMap.put("generateTime",new Date().getTime());
        verificationCode.setToken(Aes.getInstance().encrypt(tokenMap.toString()));

        return verificationCode;

    }

    /**
     * 随机位置 左上角
     *
     * @param mainLength
     *          主图长度
     * @param templateLength
     *          模版图长度
     * @param startPosition
     *          起始点
     * @return 随机位
     */
    private int randomPosition(int mainLength, int templateLength, int startPosition) {
        if(templateLength <= 0 || startPosition <= 0 || mainLength <= (templateLength + startPosition)) {
            throw new RuntimeException("图片长度不合法");
        }
        return startPosition + (int) ((mainLength - startPosition - templateLength) * Math.random());
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getCutoutImage() {
        return cutoutImage;
    }

    public void setCutoutImage(String cutoutImage) {
        this.cutoutImage = cutoutImage;
    }

    public String getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(String originalImage) {
        this.originalImage = originalImage;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getMainWidth() {
        return mainWidth;
    }

    public void setMainWidth(int mainWidth) {
        this.mainWidth = mainWidth;
    }

    public int getMainHeight() {
        return mainHeight;
    }

    public void setMainHeight(int mainHeight) {
        this.mainHeight = mainHeight;
    }

    public int getCutoutWidth() {
        return cutoutWidth;
    }

    public void setCutoutWidth(int cutoutWidth) {
        this.cutoutWidth = cutoutWidth;
    }

    public int getCutoutHeight() {
        return cutoutHeight;
    }

    public void setCutoutHeight(int cutoutHeight) {
        this.cutoutHeight = cutoutHeight;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
