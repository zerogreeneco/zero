package com.zerogreen.zerogreeneco.webSocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.zerogreen.zerogreeneco.entity.userentity.BasicUser;
import com.zerogreen.zerogreeneco.service.user.BasicUserServiceImpl;
import com.zerogreen.zerogreeneco.webSocket.entity.ChatRoom;
import com.zerogreen.zerogreeneco.webSocket.entity.Notice;
import com.zerogreen.zerogreeneco.webSocket.repository.NoticeRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final BasicUserServiceImpl usersService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void addMessageNotice(ChatRoom chatRoom, BasicUser sender, String receiver, LocalDateTime time) {
        //sender가 receiver에게 chatRoom에서 time에 메세지 전송
        Notice notice = new Notice(usersService.findByName(receiver),
                sender.getUsername() + "님이 메세지를 보냈습니다.", "/personalChat/" + chatRoom.getId(),time);
        noticeRepository.save(notice);
        sendMessage(receiver,"message");
    }

    @MessageMapping
    public void sendMessage(String name, String message){
        simpMessagingTemplate.convertAndSend("/topic/" + name, message);
    }
}
