package com.fourward.linkchart.service;

import com.fourward.linkchart.dto.UserInfoDTO;

public interface IUserInfoService {
    void insertUserInfo(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getUserLoginCheck(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO checkUserIdExist(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO checkUserEmailExist(UserInfoDTO pDTO) throws Exception;


    /*
    이메일 중복 확인
    아이디 중복 확인
    회원정보 db 등록
    로그인시 검증

     */

}