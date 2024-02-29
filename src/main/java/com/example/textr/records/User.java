package com.example.textr.records;

import lombok.Builder;

@Builder
public record User(String email, String password, String name) {
}
