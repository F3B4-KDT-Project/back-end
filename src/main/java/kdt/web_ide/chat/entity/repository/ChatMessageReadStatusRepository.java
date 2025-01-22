package kdt.web_ide.chat.entity.repository;

import kdt.web_ide.chat.entity.ChatMessageReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageReadStatusRepository extends JpaRepository<ChatMessageReadStatus, Long> {
}
