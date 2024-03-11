package com.example.textr.entity;

import com.example.textr.enums.MessageStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "tb_messages")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_sender_id")
    private User senderId;

    @ManyToOne
    @JoinColumn(name = "fk_receiver_id")
    private User receiverId;

    private String message;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    private Timestamp createdOn;

}