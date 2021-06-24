package com.demo.domain;

import lombok.Getter;

public enum Title {
    ENGINEER("工程師"), MANAGER("管理師"), DESIGNER("設計師");

    @Getter
    private final String text;

    Title(String text) {
        this.text = text;
    }
}
