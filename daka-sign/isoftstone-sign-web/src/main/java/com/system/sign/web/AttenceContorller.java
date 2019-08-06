package com.system.sign.web;

import com.email.common.ResponseResult;
import com.system.sign.service.AttenceService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 打卡
 */
@RestController
@RequestMapping(value = "attence")
public class AttenceContorller {

    @Resource
    private AttenceService attenceService;

    @RequestMapping(value = "/sign/{signType}", method = RequestMethod.GET)
    public ResponseResult<String> domain2MX(@PathVariable Integer signType) {
        try {
            String userName = "hkhuangc";
            String pwd = "6buE5pyI5YWw";
            return ResponseResult.createFailResult("解析成功", attenceService.sign(userName, pwd, signType));
        } catch (Exception e) {
            return ResponseResult.createFailResult(e.getMessage(), null);
        }
    }

}

