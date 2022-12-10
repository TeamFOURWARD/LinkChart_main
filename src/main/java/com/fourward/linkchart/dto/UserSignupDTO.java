package com.fourward.linkchart.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserSignupDTO {
    @NotBlank
    private String user_id;
    @NotBlank
    private String user_name;
    @NotBlank
    private String user_password;
    @NotBlank
    private String user_email;
    @NotBlank
    private String user_addr;
    @NotBlank
    private String mobile;
}
