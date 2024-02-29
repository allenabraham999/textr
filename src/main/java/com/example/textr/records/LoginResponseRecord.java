package com.example.textr.records;

import lombok.Builder;

@Builder
public record LoginResponseRecord(Long id, String email, String password, String name, String token) {
}
