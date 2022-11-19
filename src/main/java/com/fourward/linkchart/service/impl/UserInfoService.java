package com.fourward.linkchart.service.impl;

import com.fourward.linkchart.dto.UserInfoDTO;
import com.fourward.linkchart.persistence.mapper.IUserInfoMapper;
import com.fourward.linkchart.service.IUserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service("UserInfoService")
public class UserInfoService implements IUserInfoService {
    private final IUserInfoMapper userInfoMapper;

    @Transactional
    @Override
    public void insertUserInfo(UserInfoDTO rDTO) {
        log.info(this.getClass().getName() + ".insertUserInfo start");
        userInfoMapper.insertUserInfo(rDTO);
        log.info(this.getClass().getName() + ".insertUserInfo end");
    }

    @Override
    public UserInfoDTO getUserLoginCheck(UserInfoDTO rDTO) {

        return userInfoMapper.getUserLoginCheck(rDTO);
    }

    @Override
    public UserInfoDTO checkUserIdExist(UserInfoDTO pDTO) {

        return userInfoMapper.getUserIdExist(pDTO);
    }

    @Override
    public UserInfoDTO checkUserEmailExist(UserInfoDTO pDTO) {

        return userInfoMapper.getUserEmailExist(pDTO);
    }
}
