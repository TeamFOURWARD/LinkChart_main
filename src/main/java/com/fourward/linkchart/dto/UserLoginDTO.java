package com.fourward.linkchart.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserLoginDTO {
    @NotBlank
    private String user_id;
    @NotBlank
    private String user_password;
}
