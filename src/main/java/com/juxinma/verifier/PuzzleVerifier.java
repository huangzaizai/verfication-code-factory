package com.juxinma.verifier;

import com.juxinma.common.PuzzleParameter;
import com.juxinma.common.Aes;
import com.juxinma.common.StringUtils;
import com.juxinma.verifier.info.PuzzleVerifyInfo;
import com.juxinma.verifier.info.VerifyInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description 滑动验证码验证器
 * @Author 黄名富
 * @Date 2022/7/24 22:17
 * @Version 1.0
 **/
public class PuzzleVerifier extends Verifier{

    @Override
    public boolean verify(VerifyInfo verifyInfo) {
        PuzzleVerifyInfo info = (PuzzleVerifyInfo) verifyInfo;

        //检查参数
        checkParams(info);

        String decrypt = Aes.getInstance().decrypt(info.getToken());
        Map<String, String> tokenMap = StringUtils.strToMap(decrypt);

        //检查时间
        Date startSlidingTime = info.getStartSlidingTime();
        Date endSlidingTime = info.getEndSlidingTime();
        if(!checkTime(Long.valueOf(tokenMap.get("generateTime")),startSlidingTime,endSlidingTime)) return false;

        List<PuzzleVerifyInfo.TrackItem> trackItems = info.getTrackItems();

        //检查位置
        if(!checkEndPosition(info.getMainWidth(),trackItems.get(trackItems.size() - 1).getMoveX(),Double.valueOf(tokenMap.get("rate")))) return false;

        //检查滑动轨迹
        if(!checkTrack(trackItems)) return false;

        return true;
    }

    /**
     * 检查参数
     *
     * @param verifyInfo
     *          参数
     */
    private void checkParams(PuzzleVerifyInfo verifyInfo) {
        if(verifyInfo.getToken() == null) {
            throw new RuntimeException("校验令牌丢失");

        }
        if(verifyInfo.getStartSlidingTime() == null) {
            throw new RuntimeException("滑块开始运动时间不能为空");
        }
        if(verifyInfo.getEndSlidingTime() == null) {
            throw new RuntimeException("滑块结束运动时间不能为空");
        }
        if(verifyInfo.getMainWidth() == null) {
            throw new RuntimeException("滑块滑动范围不能为空");
        }
        if(verifyInfo.getTrackItems() == null || verifyInfo.getTrackItems().size() == 0) {
            throw new RuntimeException("滑块轨迹不能为空");
        }
    }

    /**
     * 检查滑块位置
     *
     * @param mainWidth
     *          主图宽度
     * @param position
     *          滑块x轴位置
     * @param rate
     *          真实百分比
     * @return 检查结果
     */
    private boolean checkEndPosition(double mainWidth, double position, double rate) {
        if(mainWidth == 0 || position == 0 || rate == 0) return  false;
        double realRate = position / mainWidth;
        double toleranceScope = PuzzleParameter.getInstance().getToleranceScope();
        return realRate <= rate + toleranceScope && realRate >= rate - toleranceScope;
    }

    /**
     * 检查时间
     *
     * @param generateTime
     *          拼图生成时间
     * @param startSlidingTime
     *          用户开始滑动时间
     * @param endSlidingTime
     *          用户结束滑动时间
     * @return 检查结果
     */
    private boolean checkTime(Long generateTime, Date startSlidingTime, Date endSlidingTime) {
        if(generateTime == null) return false;
        long startTime = startSlidingTime.getTime();
        long endTime = endSlidingTime.getTime();
        if(generateTime >= startTime || startTime >= endTime ) return false;
        // 验证码有效时间为5分钟
        if(startTime - generateTime > 5 * 60 * 1000) return false;
        // 滑动有效时间为5分钟
        if(endTime - startTime > 5 * 60 * 1000) return false;
        // 滑动时间小于0.5 秒
        if(endTime - startTime < 5 * 100) return false;
        return true;
    }

    /**
     * 运动轨迹校验 还未实现
     *
     * @param trackList
     *          运动轨迹
     * @return 检查结果
     */
    private boolean checkTrack(List<PuzzleVerifyInfo.TrackItem> trackList) {
        return true;
    }
}
