package com.fourward.linkchart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDTO {

    private String user_id;
    private String user_name;
    private String password;
    private String email;
    private String addr;
    private String reg_dt;

    private String isExists;
}