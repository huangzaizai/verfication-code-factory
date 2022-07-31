package com.juxinma.common;

/**
 * @Description 滑动拼图验证码相关参数
 * @Author 黄名富
 * @Date 2022/7/24 23:31
 * @Version 1.0
 **/
public class PuzzleParameter {

    /**
     * 默认模版在主图的开始位置：x坐标
     */
    private final int DEFAULT_START_X = 5;

    /**
     * 默认模版在主图的开始位置：y坐标
     */
    private final int DEFAULT_START_Y = 5;

    /**
     * 默认模版路径
     */
    private final String DEFAULT_TEMPLATE_PATH = "puzzleVerificationCode/";

    /**
     * 默认主图模版名称
     */
    private final String DEFAULT_TEMPLATE_MAIN_NAME = "main.jpg";

    /**
     * 默认抠图模版名称
     */
    private final String DEFAULT_TEMPLATE_CUTOUT_NAME = "cutout.png";

    /**
     * 主图对抠图的比例
     */
    private final double DEFAULT_MAIN_WITH_CUTOUT_RATE = 0.15;

    /**
     * 主图高度
     */
    private final int DEFAULT_MAIN_HEIGHT = 350;

    /**
     * 主图宽度
     */
    private final int DEFAULT_MAIN_WIDTH = 500;

    /**
     * 误差范围  + - 百分比
     */
    private final double TOLERANCE_SCOPE = 0.01;

    private PuzzleParameter() {}

    private static volatile PuzzleParameter instance = null;

    public static synchronized PuzzleParameter getInstance() {
        if ( instance == null ) {
            instance = new PuzzleParameter();
        }
        return instance;
    }

    /**
     * 模版在主图的开始位置：x坐标
     */
    private int startX = DEFAULT_START_X;

    /**
     * 模版在主图的开始位置：y坐标
     */
    private int startY = DEFAULT_START_Y;

    /**
     * 模版路径 主图名称:main.png,抠图名称:cutout.png
     */
    private String templatePath = DEFAULT_TEMPLATE_PATH;

    /** 误差范围  + - 百分比 */
    private double toleranceScope = TOLERANCE_SCOPE;

    /**
     * 主图模版名称
     */
    private String templateMainName = DEFAULT_TEMPLATE_MAIN_NAME;

    /**
     * 抠图模版名称
     */
    private String templateCutoutName = DEFAULT_TEMPLATE_CUTOUT_NAME;

    /**
     * 主图高度
     */
    private int mainHeight = DEFAULT_MAIN_HEIGHT;

    /**
     * 主图宽度
     */
    private int mainWidth = DEFAULT_MAIN_WIDTH;

    /**
     * 主图对抠图的比例
     */
    private double mainWithCutoutRate = DEFAULT_MAIN_WITH_CUTOUT_RATE;

    public int getStartX() {
        return startX;
    }

    public PuzzleParameter setStartX(int startX) {
        if(startX < 0) throw new RuntimeException("开始位置不能小于0");
        this.startX = startX;
        return instance;
    }

    public int getStartY() {
        return startY;
    }

    public PuzzleParameter setStartY(int startY) {
        if(startY < 0) throw new RuntimeException("开始位置不能小于0");
        this.startY = startY;
        return instance;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public PuzzleParameter setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
        return instance;
    }

    public double getToleranceScope() {
        return toleranceScope;
    }

    public PuzzleParameter setToleranceScope(double toleranceScope) {
        if(toleranceScope < 0 || toleranceScope >  0) throw new RuntimeException("误差范围不合法");
        this.toleranceScope = toleranceScope;
        return instance;
    }

    public String getTemplateMainName() {
        return templateMainName;
    }

    public PuzzleParameter setTemplateMainName(String templateMainName) {
        this.templateMainName = templateMainName;
        return instance;
    }

    public String getTemplateCutoutName() {
        return templateCutoutName;
    }

    public PuzzleParameter setTemplateCutoutName(String templateCutoutName) {
        this.templateCutoutName = templateCutoutName;
        return instance;
    }

    public int getMainHeight() {

        return mainHeight;
    }

    public PuzzleParameter setMainHeight(int mainHeight) {
        if(mainHeight  < 0)  throw new RuntimeException("高度不能小于0");
        this.mainHeight = mainHeight;
        return instance;
    }

    public int getMainWidth() {
        return mainWidth;
    }

    public PuzzleParameter setMainWidth(int mainWidth) {
        if(mainWidth < 0) throw new RuntimeException("宽度不能小于0");
        this.mainWidth = mainWidth;
        return instance;
    }

    public double getMainWithCutoutRate() {
        return mainWithCutoutRate;
    }

    public PuzzleParameter setMainWithCutoutRate(double mainWithCutoutRate) {
        if(mainWithCutoutRate < 0  || mainWithCutoutRate >=1) throw new RuntimeException("抠图与主图的的比例不合法");
        this.mainWithCutoutRate = mainWithCutoutRate;
        return instance;
    }
}
