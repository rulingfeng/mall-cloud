package com.rlf.module.controller;

import com.alibaba.fastjson.JSONObject;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.PmsBrand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.rlf.module.entity.User;
import com.rlf.module.service.HystrixService;
import com.rlf.module.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author RU
 * @date 2020/7/3
 * @Desc RestTemplate示例Controller
 */
@Api(tags = "RestTemplateDemoController", description = "RestTemplate示例")
@Controller
@RequestMapping("/template")
public class RestTemplateDemoController {

    @Resource
    RestTemplate restTemplate;

    String HOST_MALL_ADMIN = "http://mall-admin";

    @Resource
    HystrixService hystrixService;
    @Resource
    UserService userService;

    @RequestMapping(value = "/testSeata", method = RequestMethod.GET)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Object testSeata() {
        User user = new User();
        user.setId(1);
        user.setUserName("你好");
        user.setAge("18");
        userService.insert(user);
        String url = HOST_MALL_ADMIN + "/subject/save";
        ResponseEntity<CommonResult> responseEntity = restTemplate.getForEntity(url, CommonResult.class, new HashMap<>());
        System.out.println(JSONObject.toJSONString(responseEntity.getBody()));
        return user;
    }



    @RequestMapping(value = "/getProductList", method = RequestMethod.GET)
    @ResponseBody
    public Object getProductList() {
        String url = HOST_MALL_ADMIN + "/product/list";
        Map<String, Object> params = new HashMap<>();
        params.put("pageSize", 5);
        params.put("pageNum", 1);
        ResponseEntity<CommonResult> responseEntity = restTemplate.getForEntity(url, CommonResult.class, params);
        return responseEntity.getBody();
    }

    //HystrixCommand注解形式 服务降级
    @RequestMapping(value = "/getProductList1/{id}", method = RequestMethod.GET)
    @ResponseBody
    @HystrixCommand(fallbackMethod = "aaa")
    public Object getProductList1(@PathVariable Long id) {
        System.out.println("进入到getProductList1方法");
        String url = HOST_MALL_ADMIN + "/product/list";
        Map<String, Object> params = new HashMap<>();
        params.put("pageSize", 5);
        params.put("pageNum", 1);
        ResponseEntity<CommonResult> responseEntity = restTemplate.getForEntity(url, CommonResult.class, params);
        return responseEntity.getBody();
    }

    public Object aaa(@PathVariable Long id){
        return CommonResult.failed("HystrixCommand-aaa注解进到降级,id="+id+",name=");
    }

    public Object bbb(@PathVariable Long id){
        return CommonResult.failed("HystrixCommand-bbb注解进到降级,id="+id+",name=");
    }


    //
    @RequestMapping(value = "/getProductList2/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object getProductList2(@PathVariable Long id) {
        System.out.println("进入到getProductList2方法");
        hystrixService.getList(id);
        hystrixService.getList(id);
        return hystrixService.getList(id);
    }

    //@HystrixCollapser实现请求合并
    @RequestMapping(value = "/getProductList3/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object getProductList3(@PathVariable Long id) throws Exception{
        System.out.println("进入到getProductList3方法");
        long l = System.currentTimeMillis();
        Future<Object> a1 = hystrixService.hystrixCollapser(1L);
        Future<Object> a2 = hystrixService.hystrixCollapser(2L);
        System.out.println(System.currentTimeMillis()-l);
        a1.get();
        a2.get();
        TimeUnit.MILLISECONDS.sleep(200);
        Future<Object> a3 = hystrixService.hystrixCollapser(3L);
        a3.get();
        return a1.get();  // 1
    }












    @ApiOperation("getForEntity url")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object getForEntity(@PathVariable Long id) {
        String url = HOST_MALL_ADMIN + "/brand/{id}";
        ResponseEntity<CommonResult> responseEntity = restTemplate.getForEntity(url, CommonResult.class, id);
        return responseEntity.getBody();
    }

    @ApiOperation("getForEntity params")
    @RequestMapping(value = "/get2/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object getForEntity2(@PathVariable Long id) {
        String url = HOST_MALL_ADMIN + "/brand/{id}";
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        ResponseEntity<CommonResult> responseEntity = restTemplate.getForEntity(url, CommonResult.class, params);
        return responseEntity.getBody();
    }

    @ApiOperation("getForEntity Uri")
    @RequestMapping(value = "/get3/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object getForEntity3(@PathVariable Long id) {
        String url = HOST_MALL_ADMIN + "/brand/{id}";
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url).build().expand(id).encode();
        ResponseEntity<CommonResult> responseEntity = restTemplate.getForEntity(uriComponents.toUri(), CommonResult.class);
        return responseEntity.getBody();
    }

    @ApiOperation("getForObject url")
    @RequestMapping(value = "/get4/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object getForObject(@PathVariable Long id) {
        String url = HOST_MALL_ADMIN + "/brand/{id}";
        CommonResult commonResult = restTemplate.getForObject(url, CommonResult.class, id);
        return commonResult;
    }

    @ApiOperation("postForEntity jsonBody")
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    @ResponseBody
    public Object postForEntity(@RequestBody PmsBrand brand) {
        String url = HOST_MALL_ADMIN + "/brand/create";
        ResponseEntity<CommonResult> responseEntity = restTemplate.postForEntity(url, brand, CommonResult.class);
        return responseEntity.getBody();
    }

    @ApiOperation("postForEntity jsonBody")
    @RequestMapping(value = "/post2", method = RequestMethod.POST)
    @ResponseBody
    public Object postForObject(@RequestBody PmsBrand brand) {
        String url = HOST_MALL_ADMIN + "/brand/create";
        CommonResult commonResult = restTemplate.postForObject(url, brand, CommonResult.class);
        return commonResult;
    }

    @ApiOperation("postForEntity form")
    @RequestMapping(value = "/post3", method = RequestMethod.POST)
    @ResponseBody
    public Object postForEntity3(@RequestParam String name) {
        String url = HOST_MALL_ADMIN + "/productAttribute/category/create";
        //设置头信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //构造表单参数
        MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
        params.add("name", name);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<CommonResult> responseEntity = restTemplate.postForEntity(url, requestEntity, CommonResult.class);
        CommonResult body = responseEntity.getBody();
        return body;
    }
}
