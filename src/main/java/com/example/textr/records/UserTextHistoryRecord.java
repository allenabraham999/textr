package com.example.textr.records;

import lombok.*;

@Builder
public record UserTextHistoryRecord(Long id, String name,String email) {
}
