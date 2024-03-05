package com.example.textr.records;

import lombok.Builder;

@Builder
public record MessageDataRecord(Long from, Long to, String text) {
}
