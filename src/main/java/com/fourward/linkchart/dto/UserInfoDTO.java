package com.fourward.linkchart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDTO {

    private String user_id;
    private String user_name;
    private String user_password;
    private String user_email;
    private String user_addr;
    private String reg_dt;

    private String isExist;
}