package com.zerogreen.zerogreeneco.webSocket.repository;

import org.springframework.data.repository.CrudRepository;
import zerogreen.eco.webSocket.entity.Notice;

public interface NoticeRepository extends CrudRepository<Notice,Long> {
    
}
