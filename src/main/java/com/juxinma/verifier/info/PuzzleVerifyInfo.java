package com.juxinma.verifier.info;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description 滑动验证码 验证信息
 * @Author 黄名富
 * @Date 2022/7/24 22:18
 * @Version 1.0
 **/
public class PuzzleVerifyInfo extends VerifyInfo{

    /** 主图宽度 */
    private Double mainWidth;

    /** 开始滑动时间 */
    private Date startSlidingTime;

    /** 结束滑动时间 */
    private Date endSlidingTime;

    /** 运动轨迹 */
    private List<TrackItem> trackItems;

    /** AES对称加密信息 内容: x 位置的百分比 抠图左边 x值 / 主图宽度 = 百分比; */
    private String token;

    public Double getMainWidth() {
        return mainWidth;
    }

    public void setMainWidth(Double mainWidth) {
        this.mainWidth = mainWidth;
    }

    public Date getStartSlidingTime() {
        return startSlidingTime;
    }

    public void setStartSlidingTime(Date startSlidingTime) {
        this.startSlidingTime = startSlidingTime;
    }

    public Date getEndSlidingTime() {
        return endSlidingTime;
    }

    public void setEndSlidingTime(Date endSlidingTime) {
        this.endSlidingTime = endSlidingTime;
    }

    public List<TrackItem> getTrackItems() {
        return trackItems;
    }

    public void setTrackItems(List<TrackItem> trackItems) {
        this.trackItems = trackItems;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class TrackItem implements Serializable {

        /** X轴 运动距离 */
        Double moveX;

        /** Y轴 运动距离 */
        Double moveY;

        /** 运动时间 */
        Integer time;

        public Double getMoveX() {
            return moveX;
        }

        public void setMoveX(Double moveX) {
            this.moveX = moveX;
        }

        public Double getMoveY() {
            return moveY;
        }

        public void setMoveY(Double moveY) {
            this.moveY = moveY;
        }

        public Integer getTime() {
            return time;
        }

        public void setTime(Integer time) {
            this.time = time;
        }
    }
}
