package com.fourward.linkchart.persistence.mapper;

import com.fourward.linkchart.dto.UserInfoDTO;
import com.fourward.linkchart.dto.UserSignupDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserInfoMapper {
    int insertUserInfo(UserSignupDTO pDTO);

    String getUserIdExists(String s);

    String getUserEmailExists(String s);

    String getUserMobileExists(String s);

    UserInfoDTO getUserLoginCheck(UserInfoDTO pDTO);

    int updateUserPsw(UserInfoDTO pDTO);

    int updateUserEmail(UserInfoDTO pDTO);

    int updateUserAddr(UserInfoDTO pDTO);

    UserInfoDTO getUserInfo(UserInfoDTO pDTO);

}