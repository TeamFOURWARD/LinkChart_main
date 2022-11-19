package com.fourward.linkchart.persistence.mapper;

import com.fourward.linkchart.dto.UserInfoDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserInfoMapper {
    int insertUserInfo(UserInfoDTO pDTO);

    UserInfoDTO getUserIdExist(UserInfoDTO pDTO);

    UserInfoDTO getUserEmailExist(UserInfoDTO pDTO);

    UserInfoDTO getUserLoginCheck(UserInfoDTO pDTO);
}