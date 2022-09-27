package com.fourward.linkchart.service;

import com.fourward.linkchart.dto.UserInfoDTO;
import org.omg.CORBA.UserException;

public interface IUserInfoService {

    // 회원 가입하기(회원정보 등록하기)
    int inserUserInfo(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO gertUserExists(UserException pDTO) throws Exception;

    // 로그인을 위해 아이디와 비밀번호가 일치하는지 확인하기
    int getUserLoginCheck(UserInfoDTO pDTO) throws Exception;
}




