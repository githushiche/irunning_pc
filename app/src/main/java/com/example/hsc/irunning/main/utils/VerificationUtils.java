package com.example.hsc.irunning.main.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证
 *
 * @author Diviner
 * @date 2018-5-11 上午9:33:57
 */
public class VerificationUtils {

    /**
     * 判断用户名是否正确的正则表达式
     *
     * @param userName
     * @return
     */
    public static boolean userNameRegularExpression(String userName) {
        if (!userName.equals("")) {
            /*
             * 用户名（4－8位字母加数字组成）,^表示正则开始,$表示正则结束
			 */
            Pattern pattern = Pattern.compile("^[a-zA-Z]\\w{4,8}$");// 验证用户名正则表达式
            Matcher matcher = pattern.matcher(userName);
            return matcher.matches();
        }
        return false;
    }

    /**
     * 判断密码是否正确的正则表达式
     *
     * @param passWord
     * @return
     */
    public static boolean passWordRegularExpression(String passWord) {
        if (!passWord.equals("")) {
			/*
			 * 用户密码（5－17位字母与数字组合）,^表示正则开始,$表示正则结束
			 */
            Pattern pattern = Pattern.compile("^[a-zA-Z]\\w{5,17}$");// 声明一个判断密码的正则表达式
            Matcher matcher = pattern.matcher(passWord);
            return matcher.matches();
        }
        return false;
    }

    /**
     * 判断邮箱是否正确的正则表达式
     *
     * @param Emali
     * @return
     */
    public static boolean EmaliRegularExpression(String Emali) {
        if (Emali != null) {
            Pattern pattern = Pattern
                    .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
            Matcher matcher = pattern.matcher(Emali);
            return matcher.matches();
        }
        return false;
    }
}
