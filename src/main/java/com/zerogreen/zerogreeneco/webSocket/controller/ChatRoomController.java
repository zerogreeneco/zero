package com.zerogreen.zerogreeneco.webSocket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zerogreen.eco.entity.userentity.BasicUser;
import zerogreen.eco.security.auth.PrincipalDetails;
import zerogreen.eco.service.user.BasicUserServiceImpl;
import zerogreen.eco.webSocket.entity.ChatMessage;
import zerogreen.eco.webSocket.entity.ChatRoom;
import zerogreen.eco.webSocket.entity.ChatRoomJoin;
import zerogreen.eco.webSocket.service.ChatRoomJoinService;
import zerogreen.eco.webSocket.service.ChatRoomService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {
    private final BasicUserServiceImpl usersService;
    private final ChatRoomJoinService chatRoomJoinService;
    private final ChatRoomService chatRoomService;


    @GetMapping("/chat")
    public String chatHome(@AuthenticationPrincipal PrincipalDetails sessionUser, Model model){
        model.addAttribute("myId",sessionUser.getUsername());
        BasicUser user = usersService.findByName(sessionUser.getUsername());
        log.info("sessionUser.getName()"+sessionUser.getUsername());
        log.info("user"+user);
        List<ChatRoomJoin> chatRoomJoins = chatRoomJoinService.findByUser(user);
        List<ChatRoomForm> chatRooms = chatRoomService.setting(chatRoomJoins,user);
        model.addAttribute("chatRooms",chatRooms);
        if(user == null){
            model.addAttribute("userName","");
            model.addAttribute("userId",0);
        }
        else{
            model.addAttribute("userName",user.getUsername());
            model.addAttribute("userId",user.getId());
        }
        return "chat/chatMain";
    }

    @PostMapping("/chat/newChat")
    public String newChat(@RequestParam("receiver") String user1, @RequestParam("sender") String user2){
        Long chatRoomId = chatRoomJoinService.newRoom(user1,user2);
        return "redirect:/personalChat/" + chatRoomId;
    }

    @RequestMapping("/personalChat/{chatRoomId}")
    public String goChat(@PathVariable("chatRoomId") Long chatRoomId,Model model,@AuthenticationPrincipal PrincipalDetails sessionUser){
        Optional<ChatRoom> opt = chatRoomService.findById(chatRoomId);
        ChatRoom chatRoom = opt.get();
        List<ChatMessage> messages = chatRoom.getMessages();
        Collections.sort(messages, (t1, t2) -> {
            if(t1.getId() > t2.getId()) return -1;
            else return 1;
        });
        if(sessionUser == null){
            model.addAttribute("userName","");
            model.addAttribute("userId",0);
        }
        else{
            model.addAttribute("userName",sessionUser.getUsername());
            model.addAttribute("userId",sessionUser.getId());
        }
        List<ChatRoomJoin> list = chatRoomJoinService.findByChatRoom(chatRoom);
        model.addAttribute( "messages",messages);
        log.info(messages+"yjyjyjyjyjyjyjyjyjjy");
        model.addAttribute("myId",sessionUser.getUsername());
        model.addAttribute("chatRoomId",chatRoomId);
        int cnt = 0;
        for(ChatRoomJoin join : list){
            if(join.getUser().getUsername().equals(sessionUser.getUsername()) == false){
                model.addAttribute("receiver",join.getUser().getUsername());
                ++cnt;
            }
        }
        if(cnt >= 2){
            return "redirect:/chat";
        }
        if(cnt == 0){
            model.addAttribute("receiver","");
        }
        return "chat/chatRoom";
    }
}
