package com.fourward.linkchart.service;

import com.fourward.linkchart.dto.UserInfoDTO;

public interface IUserInfoService {
    void insertUserInfo(UserInfoDTO pDTO);

    UserInfoDTO getUserLoginCheck(UserInfoDTO pDTO);

    UserInfoDTO checkUserIdExist(UserInfoDTO pDTO);

    UserInfoDTO checkUserEmailExist(UserInfoDTO pDTO);
}