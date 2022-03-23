package com.zerogreen.zerogreeneco.webSocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zerogreen.zerogreeneco.service.user.BasicUserServiceImpl;
import com.zerogreen.zerogreeneco.webSocket.controller.ChatMessageForm;
import com.zerogreen.zerogreeneco.webSocket.entity.ChatMessage;
import com.zerogreen.zerogreeneco.webSocket.repository.ChatMessageRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final BasicUserServiceImpl usersService;
    private final ChatRoomService chatRoomService;
    private final NoticeService noticeService;

    @Transactional
    public void save(ChatMessageForm message) {
        ChatMessage chatMessage = new ChatMessage(message.getMessage(), LocalDateTime.now(),chatRoomService.findById(message.getChatRoomId()).get()
                ,usersService.findByName(message.getSender()));
        chatMessageRepository.save(chatMessage);
        noticeService.addMessageNotice(chatMessage.getChatRoom(),chatMessage.getWriter(), message.getReceiver(),chatMessage.getTime());
    }
}
