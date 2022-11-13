package com.fourward.linkchart.service.impl;

import com.fourward.linkchart.dto.UserInfoDTO;
import com.fourward.linkchart.persistence.mapper.IUserInfoMapper;
import com.fourward.linkchart.service.IUserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service("UserInfoService")
public class UserInfoService implements IUserInfoService {
    private final IUserInfoMapper userInfoMapper;

    @Override
    public void insertUserInfo(UserInfoDTO rDTO) throws Exception {
        log.info(this.getClass().getName() + ".insertUserInfo start");
        userInfoMapper.insertUserInfo(rDTO);
        log.info(this.getClass().getName() + ".insertUserInfo end");
    }

    @Override
    public void getUserLoginCheck(UserInfoDTO rDTO) throws Exception {
        UserInfoDTO pDTO = userInfoMapper.getUserLoginCheck(rDTO);

        if (Objects.equals(pDTO.getIsExist(), "")) {
            log.warn("회원정보 오류. 로그인 실패.");
            throw new Exception();
        }
    }

    @Override
    public UserInfoDTO checkUserIdExist(UserInfoDTO pDTO) throws Exception {

        return userInfoMapper.getUserIdExist(pDTO);
    }

    @Override
    public UserInfoDTO checkUserEmailExist(UserInfoDTO pDTO) throws Exception {

        return userInfoMapper.getUserEmailExist(pDTO);
    }
}
