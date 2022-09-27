package com.fourward.linkchart.persistence.mapper;

import com.fourward.linkchart.dto.UserInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IUserInfoMapper {

    // 회원 가입하기(회원정보 등록하기(
    int insertUserInfo(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getUserExists(UserInfoDTO pDTO) throws Exception;


    UserInfoDTO getUserLoginCheck(UserInfoDTO pDTO);
}
