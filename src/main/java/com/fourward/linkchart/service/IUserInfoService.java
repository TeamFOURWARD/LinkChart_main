package com.fourward.linkchart.service;

import com.fourward.linkchart.dto.UserInfoDTO;
import com.fourward.linkchart.dto.UserSignupDTO;

public interface IUserInfoService {
    void insertUserInfo(UserSignupDTO pDTO);

    UserInfoDTO getUserLoginCheck(UserInfoDTO pDTO);

    String isIdExists(String s);

    String isEmailExists(String s);

    String isMobileExists(String s);

    void updateUserPsw(UserInfoDTO pDTO);

    void updateUserEmail(UserInfoDTO pDTO);

    void updateUserAddr(UserInfoDTO pDTO);

    UserInfoDTO getUserInfo(UserInfoDTO pDTO);

}