package com.example.textr.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tb_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String name;

    @OneToMany(mappedBy = "senderId")
    List<Message> sentMessage;

    @OneToMany(mappedBy = "receiverId")
    List<Message> receivedMessage;

}
