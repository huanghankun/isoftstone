package com.system.sign.service;

import java.util.List;

/**
 * @Description: 打卡
 * @author 黄汉坤
 * @date 2018-06-25 10:37
 * @version V1.0
 */
public interface AttenceService {
    /**
     * @Description:打卡
     * @param userName
     * @param signType
     * @return: java.util.List<java.lang.String>
     * @auther: ex_huanghk5
     * @date: 2018-06-25 10:43
     */
    String sign(String userName,String pwd,int signType);
}
