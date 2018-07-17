package com.email.common;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 邮件相关工具类
 * @author 黄汉坤
 * @date 2018-06-25 10:37
 * @version V1.0
 */
public class EmailUtils {
    /**
     * @Description:域名转换成MX记录
     * MX记录是邮件交换记录，它指向一个邮件服务器，用于电子邮件系统发邮件时根据 收信人的地址后缀来定位邮件服务器。
     * MX记录也叫做邮件路由记录，用户可以将该域名下的邮件服务器指向到自己的mail server上，然后即可自行操控所有的邮箱设置。
     * @param domain
     * @return: java.util.List<java.lang.String>
     * @auther: ex_huanghk5
     * @date: 2018-06-25 10:38
     */
    public static List<String> domain2MX(String domain) throws Exception {
        List<String> mxList = new ArrayList<>();
        Lookup lookup = null;
        Record[] answers = null;
        lookup = new Lookup(domain, Type.MX);
        lookup.run();
        if (lookup.getResult() != Lookup.SUCCESSFUL) {
            return null;
        } else {
            answers = lookup.getAnswers();
        }
        for (int i = 0; i < answers.length; i++) {
            mxList.add(answers[i].getAdditionalName().toString());
        }
        return mxList;
    }


}
