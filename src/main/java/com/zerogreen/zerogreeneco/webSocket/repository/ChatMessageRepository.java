package com.zerogreen.zerogreeneco.webSocket.repository;

import org.springframework.data.repository.CrudRepository;
import com.zerogreen.zerogreeneco.webSocket.entity.ChatMessage;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {
}
