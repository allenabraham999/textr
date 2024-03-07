package com.example.textr.repository;

import com.example.textr.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value = "select * from tb_messages tm \n" +
            "where \n" +
            "(tm.fk_sender_id  = :sender and tm.fk_receiver_id  = :receiver)\n" +
            "or\n" +
            "(tm.fk_sender_id = :receiver and tm.fk_receiver_id = :sender)\n" +
            "order by tm.created_on ",nativeQuery = true)
    public List<Message> getMessages(@Param("sender")Long sender, @Param("receiver")Long receiver);
}