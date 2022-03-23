package com.zerogreen.zerogreeneco.webSocket.repository;

import org.springframework.data.repository.CrudRepository;
import zerogreen.eco.webSocket.entity.ChatMessage;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {
}
