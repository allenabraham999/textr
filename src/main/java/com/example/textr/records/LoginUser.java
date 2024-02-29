package com.example.textr.records;

import lombok.Builder;

@Builder
public record LoginUser(String email, String password) {
}
