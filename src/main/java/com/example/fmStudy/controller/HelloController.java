package com.example.fmStudy.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.fmStudy.dto.User;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 王蕾<br>
 * <b>mail</b> wanglei@syncsoft.com.cn<br>
 * <b>date</b> 2023年12月08日<br>
 * @version 1.0.0
 *
 * <pre>
 * 修改记录
 *  版本号		修订日期		修改人		bug编号		修改内容
 *  1.0.0		2023/12/08	王蕾	    		新建
 * </pre>
 */
@Controller
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping("world")
    @ResponseBody
    public String helloWorld() {
        return "helloWorld";
    }

    @RequestMapping(method = RequestMethod.GET, value = "getNotParam")
    @ResponseBody
    public String getNotParam() {
        return "get请求,没有请求参数";
    }

    @RequestMapping(method = RequestMethod.GET, value = "getByParams")
    @ResponseBody
    public String getByParams(@RequestParam String pageNum, @RequestParam String pageSize) {
        return "get请求，请求参数为pageNum：" + pageNum + "，pageSize：" + pageSize;
    }

    @RequestMapping(method = RequestMethod.POST, value = "postNotParam")
    @ResponseBody
    public String postNotParam() {
        return "post请求，没有请求参数";
    }

    @RequestMapping(method = RequestMethod.POST, value = "postByParams")
    @ResponseBody
    public String postByParams(@RequestParam String pageNum, @RequestParam String pageSize) {
        return "post请求，请求参数为pageNum：" + pageNum + "，pageSize：" + pageSize;
    }

    @RequestMapping(method = RequestMethod.POST, value = "postByJson")
    @ResponseBody
    public String postByJson(@RequestBody User user) {
        return "post请求，请求类型为JSON，请求参数为：" + user.toString();
    }

    @RequestMapping(value = "postByParamsAndJson", method = RequestMethod.POST)
    @ResponseBody
    public String postByParamsAndJson(@RequestBody User user, @RequestParam String pageNum, @RequestParam String pageSize) {
        return "post请求，请求参数为pageNum：" + pageNum + "，pageSize：" + pageSize + "，JSON请求参数" + user;
    }

    @RequestMapping(method = RequestMethod.POST, value = "postByXml")
    @ResponseBody
    public String postByXml(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        InputStream is = null;
        BufferedReader bufferedReader = null;
        StringBuffer buffer = new StringBuffer();
        try {
            is = request.getInputStream();
            if (is != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    buffer.append(charBuffer, 0, bytesRead);
                }
            } else {
                buffer.append("");
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != bufferedReader) {
                bufferedReader.close();
            }
            if (null != is) {
                is.close();
            }
        }
        return "post请求，请求类型为xml，请求参数为：" + buffer.toString();
    }

    @RequestMapping(value = "postByFile", method = RequestMethod.POST)
    @ResponseBody
    public String postByFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("files");
        File saveFile = new File("D:\\1_copy.txt");
        FileCopyUtils.copy(multipartFile.getBytes(), saveFile);
        return "post请求，请求类型为文件";
    }

    @RequestMapping(value = "postByFileAndParam", method = RequestMethod.POST)
    @ResponseBody
    public String postByFileAndParam(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("files");
        File saveFile = new File("D:\\2_copy.txt");
        FileCopyUtils.copy(multipartFile.getBytes(), saveFile);
        return "post请求，请求类型为文件和param，请求参数为pageNum：" + pageNum + "，pageSize：" + pageSize;
    }

    @RequestMapping(value = "postByFileAndJson", method = RequestMethod.POST)
    @ResponseBody
    public String postByFileAndJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
        // 获取文件
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("files");
        File saveFile = new File("D:\\3_copy.txt");
        FileCopyUtils.copy(multipartFile.getBytes(), saveFile);
        // 获取json
        String jsonData = request.getParameter("jsonData");
        User user = JSONObject.parseObject(jsonData, User.class);
        return "post请求，请求类型为文件和json，JSON值：" + user;
    }

    @RequestMapping(value = "postByFileAndParamAndJson", method = RequestMethod.POST)
    @ResponseBody
    public String postByFileAndParamAndJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取param
        String pageSize = request.getParameter("pageSize");
        String pageNum = request.getParameter("pageNum");
        // 获取json
        String jsonData = request.getParameter("jsonData");
        User user = JSONObject.parseObject(jsonData, User.class);
        // 获取文件
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("files");
        File saveFile = new File("D:\\4_copy.txt");
        FileCopyUtils.copy(multipartFile.getBytes(), saveFile);
        return "post请求，文件类型为文件，param和json，请求参数为pageNum：" + pageNum + "，pageSize：" + pageSize + "，JSON值：" + user;
    }

    @RequestMapping(value = "postByJsonAndReturnObj", method = RequestMethod.POST)
    @ResponseBody
    public User postByJsonAndReturnObj(@RequestBody User user) {
        // 请求类型为JSON，返回为对象
        return user;
    }
}
