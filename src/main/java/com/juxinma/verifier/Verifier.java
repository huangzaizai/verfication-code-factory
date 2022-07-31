package com.juxinma.verifier;

import com.juxinma.verifier.info.VerifyInfo;

/**
 * @Description 验证器： 抽象类
 * @Author 黄名富
 * @Date 2022/7/24 21:38
 * @Version 1.0
 **/
public abstract class Verifier {

    /**
     * 验证
     *
     * @param verifyInfo
     *          验证信息
     * @return 验证结果
     */
    public abstract boolean verify(VerifyInfo verifyInfo);

}
