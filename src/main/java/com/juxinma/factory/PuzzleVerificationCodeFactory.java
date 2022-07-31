package com.juxinma.factory;

import com.juxinma.common.Inspector;
import com.juxinma.verificationCode.PuzzleVerificationCode;
import com.juxinma.verificationCode.VerificationCode;
import com.juxinma.verifier.PuzzleVerifier;
import com.juxinma.verifier.Verifier;

/**
 * @Description 滑动验证码工厂
 * @Author 黄名富
 * @Date 2022/7/24 21:54
 * @Version 1.0
 **/
public class PuzzleVerificationCodeFactory implements VerificationCodeFactory {

    static {
        Inspector inspector = new Inspector();
        //组件的自定义参数、模版图片等进行自动检查。只执行一次检查。
        inspector.doCheck();
    }

    @Override
    public VerificationCode createVerificationCode() {
        return new PuzzleVerificationCode();
    }

    @Override
    public Verifier createVerifier() {
        return new PuzzleVerifier();
    }

}
