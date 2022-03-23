package com.zerogreen.zerogreeneco.webSocket.repository;

import org.springframework.data.repository.CrudRepository;
import com.zerogreen.zerogreeneco.webSocket.entity.Notice;

public interface NoticeRepository extends CrudRepository<Notice,Long> {
    
}
