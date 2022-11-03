package com.fourward.linkchart.dto;

import lombok.Getter;
import lombok.Setter;

public class MemberDTO {
    @Getter
    @Setter
    public class MemberForm {

        private String user_id;
        private String user_name;
        private String password;
        private String email;
        private String addr1;
        private String addr2;
        private String reg_id;
        private String reg_dt;
        private String chg_id;
        private String chg_dt;

    }
}
