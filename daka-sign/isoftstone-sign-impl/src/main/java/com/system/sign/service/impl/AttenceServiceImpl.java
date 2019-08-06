package com.system.sign.service.impl;


import com.alibaba.fastjson.JSON;
import com.email.common.HttpUtils;
import com.system.aop.model.AopLog;
import com.system.sign.service.AttenceService;
import com.system.sign.service.model.Attence;
import com.system.sign.service.model.EmployeeInfo;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class AttenceServiceImpl implements AttenceService, ApplicationListener<ContextRefreshedEvent> {

    public static String userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 9_3 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile/13E230 WMall/3.0 NetType/WIFI";
    private final Logger LOGGER = LoggerFactory.getLogger(AttenceServiceImpl.class);
    @Resource(name = "employeeMap")
    private Map<String, String> employeeMap;

    /**
     * @Description:获取用户名
     * @param empDomainName
     * @param empPassword
     * @return: java.lang.String
     * @auther: ex_huanghk5
     * @date: 2018-07-12 17:35
     */
    private static String getUserName(String empDomainName, String empPassword) throws IOException {
        CloseableHttpResponse httpsPost = HttpUtils.doHttpsPost("https://passport.isoftstone.com/?DomainUrl=http://ipsapro.isoftstone.com&ReturnUrl=%2fPortal%2f", "{\"emp_DomainName\":\"" + empDomainName + "\",\"emp_Password\":\"" + empPassword + "\",\"RemeberMe\":true}", null);
        Header[] setCookie = httpsPost.getHeaders("Set-Cookie");
        String userName = null;
        for (Header header : setCookie) {
            if (header.getValue() != null && header.getValue().startsWith("UserName=")) {
                String strs = header.getValue();
                userName = strs.substring(strs.indexOf("=") + 1, strs.indexOf(";"));
                break;
            }
        }
        return userName;
    }

    /**
     * @Description:打卡
     * @param userName
     * @param signType
     * @return: java.util.List<java.lang.String>
     * @auther: ex_huanghk5
     * @date: 2018-06-25 10:43
     */
    @Override
    @AopLog(before = "进入打卡", after = "完成打卡", funModule = "打卡")
    public String sign(String userName, String pwd, int signType) {
        LOGGER.info("软通用户名:" + userName);
        LOGGER.info("用户名密码:" + pwd);
        try {
            httpIsoftStone(userName, pwd, signType);
            return "打卡成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "打卡失败";
        }

    }

    /**
     * 获取员工信息
     * @param headersMap
     * @return
     * @throws IOException
     */
    private EmployeeInfo getEmployeeInfo(String userName, Map<String, String> headersMap) throws IOException {
        CloseableHttpResponse userNamePost = HttpUtils.doHttpsPost("http://i.isoftstone.com/apigateway/UserInfoService/GetForMobile", "{\"UserName\":\"" + userName + "\"}", headersMap);
        String respJson = EntityUtils.toString(userNamePost.getEntity());
        EmployeeInfo employeeInfo = JSON.parseObject(respJson, EmployeeInfo.class);
        return employeeInfo;
    }

    /**
     * @Description:创建请求头
     * @param
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @auther: ex_huanghk5
     * @date: 2018-07-12 13:55
     */
    private Map<String, String> createHeaders() {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Accept", "application/json, text/plain, */*");
        headersMap.put("Content-Type", "application/json;charset=UTF-8");
        headersMap.put("Host", "i.isoftstone.com");
        headersMap.put("Origin", "http://i.isoftstone.com");
        headersMap.put("Referer", "http://i.isoftstone.com/kaoqindakaweixinDTG/");
        headersMap.put("Authorization", "iPSA");
        headersMap.put("User-Agent", userAgent);
        return headersMap;
    }

    /**
     * @Description:签到和签退
     * @param employeeInfo 员工信息
     * @param inOrBack 1、签到,2、签退
     * @param headersMap 请求头信息
     * @return: void
     * @auther: ex_huanghk5
     * @date: 2018-07-12 13:59
     */
    private void signInOrBack(EmployeeInfo employeeInfo, int inOrBack, Map<String, String> headersMap) {
        try {
            Attence attence = new Attence();
            attence.setAttenceDeviceID(System.currentTimeMillis());
            attence.setAttenceRemark(inOrBack == 1 ? "签到" : "签退");
            attence.setAttenceEmpName(employeeInfo.getName());
            attence.setAttenceEmpNo(employeeInfo.getNo());
            attence.setAttenceType(1);
            String attenceJson = JSON.toJSON(attence).toString();
            CloseableHttpResponse saveAttenceRecord = HttpUtils.doHttpsPost("http://i.isoftstone.com/api/DTGAttenceService/SaveAttenceRecord", attenceJson, headersMap);
            String respJson = EntityUtils.toString(saveAttenceRecord.getEntity());
            LOGGER.info("签到签退返回报文 ： " + respJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description:软通签到整个流程
     * @param
     * @return: void
     * @auther: ex_huanghk5
     * @date: 2018-07-12 17:36
     */
    private void httpIsoftStone(String uName, String pwd, int signType) throws Exception {
        String userName = getUserName(uName, pwd);
        Map<String, String> headersMap = createHeaders();
        EmployeeInfo employeeInfo = getEmployeeInfo(userName, headersMap);
        signInOrBack(employeeInfo, signType, headersMap);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        LOGGER.info("############################# 打卡员工 #############################");

        for (Map.Entry<String, String> entry : employeeMap.entrySet()) {
            LOGGER.info("员工域账号:" + entry.getKey());
            LOGGER.info("域账号密码:" + entry.getValue());
        }
    }

    /**
     * 定时任务，每天早上08:15签到
     */
    @Scheduled(cron = "0 15 8 * * ?")
    public void signInTask() {
        LOGGER.info("############################# 签到 #############################");
        for (Map.Entry<String, String> entry : employeeMap.entrySet()) {
            LOGGER.info("员工域账号:" + entry.getKey());
            LOGGER.info("域账号密码:" + entry.getValue());
            try {
                httpIsoftStone(entry.getKey(), entry.getValue(), 1);
            } catch (Exception e) {
                LOGGER.error("打卡失败", e);
            }
        }
    }

    /**
     * 定时任务，每天18:15签退
     */
    @Scheduled(cron = "0 15 18 * * ?")
    public void signOutTask() {
        LOGGER.info("############################# 签退 #############################");
        for (Map.Entry<String, String> entry : employeeMap.entrySet()) {
            LOGGER.info("员工域账号:" + entry.getKey());
            LOGGER.info("域账号密码:" + entry.getValue());
            try {
                httpIsoftStone(entry.getKey(), entry.getValue(), 2);
            } catch (Exception e) {
                LOGGER.error("打卡失败", e);
            }
        }
    }

}
