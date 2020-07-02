package com.rlf.module.design.responsibilityChain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: RU
 * @Date: 2020/4/18 16:45
 */
@RestController
@RequestMapping("/chain")
@Slf4j
public class ResponsibilityChainController {
    @GetMapping("test")
    public String aaaa(@RequestParam("temp") Integer temp) {
        CommonManager commonManager = new CommonManager("尼古拉斯·经理");
        Majordomo majordomo = new Majordomo("尼古拉斯·总监");
        GeneralManager generalManager = new GeneralManager("尼古拉斯·总经理");

        //设置上级
        commonManager.setSuperior(majordomo);
        majordomo.setSuperior(generalManager);

//        Request request = new Request();
//        request.setRequestType("请假");
//        request.setRequestContent("adam请假");
//        request.setNumber(temp);
//        commonManager.handlerRequest(request);

        Request request1 = new Request();
        request1.setRequestType("加薪");
        request1.setRequestContent("rlf加薪");
        request1.setNumber(temp);
        commonManager.handlerRequest(request1);

        return null;
    }

}
