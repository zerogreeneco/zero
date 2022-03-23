package com.zerogreen.zerogreeneco.webSocket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import zerogreen.eco.entity.userentity.BasicUser;
import zerogreen.eco.service.user.BasicUserServiceImpl;
import zerogreen.eco.webSocket.entity.Notice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AjaxController {
    private final BasicUserServiceImpl usersService;

    @GetMapping("/users/nameChk/{name}")
    public String nameChk(@PathVariable("name") String name){
        if(usersService.findByName(name)!= null){
            return "success";
        }
        return "false";
    }

    @GetMapping("/users/notice")
    public Map<String,Object> getNotice(String name){
        Map<String,Object> map = new HashMap<>();
        BasicUser user = usersService.findByName(name);
        List<String> contents = new ArrayList<>();
        List<String> links = new ArrayList<>();
        List<LocalDateTime> times = new ArrayList<>();
        List<Notice> notices = user.getNotices();
        for(Notice notice : notices){
            contents.add(notice.getContent());
            links.add(notice.getLink());
            times.add(notice.getTime());
        }
        map.put("content",contents);
        map.put("link",links);
        map.put("time",times);
        return map;
    }
}
