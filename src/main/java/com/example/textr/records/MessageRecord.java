package com.example.textr.records;

import lombok.Builder;

@Builder
public record MessageRecord(Long sendTo, String message) {
}
