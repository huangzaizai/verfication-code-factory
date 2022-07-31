package com.juxinma.factory;

import com.juxinma.verificationCode.VerificationCode;
import com.juxinma.verifier.Verifier;

/**
 * @Description 验证码工厂
 * @Author 黄名富
 * @Date 2022/7/24 21:52
 * @Version 1.0
 **/
public interface VerificationCodeFactory {

    VerificationCode createVerificationCode();

    Verifier createVerifier();

}
