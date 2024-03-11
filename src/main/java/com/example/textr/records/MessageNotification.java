package com.example.textr.records;

import lombok.*;


@Builder
public record MessageNotification(Long senderId, Long receiverId) {
}
