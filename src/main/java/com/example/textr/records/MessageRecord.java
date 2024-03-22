package com.example.textr.records;

import lombok.Builder;

@Builder
public record MessageRecord(Long from, Long sendTo, String message) {
}
