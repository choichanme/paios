package com.paios.Vo.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    BUYER("ROLE_BUYER"),
    SUPPLIER("ROLE_SUPPLIER"),
    FORWARDING("ROLE_FORWARDING");


    private String value;
}
