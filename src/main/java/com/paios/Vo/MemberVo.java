package com.paios.Vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberVo {
    private Long id;
    private String email;
    private String password;
    private String role;
    private String chPwYn;
    private String certNum;
}
