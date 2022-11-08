package com.fourward.linkchart.service.impl;

import com.fourward.linkchart.dto.MailDTO;
import com.fourward.linkchart.dto.UserInfoDTO;
import com.fourward.linkchart.persistence.mapper.IUserInfoMapper;
import com.fourward.linkchart.service.IUserInfoService;
import com.fourward.linkchart.util.CmmUtil;
import com.fourward.linkchart.util.DateUtil;
import com.fourward.linkchart.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service("UserInfoService")
public class UserInfoService implements IUserInfoService {
    private final IUserInfoMapper userInfoMapper;
    @Override
    public int insertUserInfo(UserInfoDTO pDTO) throws Exception {

/*        if (pDTO == null) {
            pDTO = new UserInfoDTO();
        }

        UserInfoDTO rDTO = userInfoMapper.getUserExists(pDTO);

        if (rDTO == null) {
            rDTO = new UserInfoDTO();
        }

        if (CmmUtil.nvl(rDTO.getExists_yn()).equals("Y")) {
            res = 2;

        } else {
            int success = userInfoMapper.insertUserInfo(pDTO);

            if (success > 0) {
                res = 1;

                MailDTO mDTO = new MailDTO();

                mDTO.setToMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(pDTO.getEmail())));

                mDTO.setTitle("회원가입을 축하드립니다."); //제목

                mDTO.setContents(CmmUtil.nvl(pDTO.getUser_name()) + "님의 회원가입을 진심으로 축하드립니다.");

                mailService.doSendMail(mDTO);
            } else {
                res = 0;
            }
        }
        return res;
       */
        return 1;
    }

    @Override
    public int getUserLoginCheck(UserInfoDTO pDTO) throws Exception {
        // 로그인 성공 : 1, 실패 : 0
        int res = 0;

        UserInfoDTO rDTO = userInfoMapper.getUserLoginCheck(pDTO);

        if (rDTO == null) {
            rDTO = new UserInfoDTO();
        }

        if (CmmUtil.nvl(rDTO.getUser_id()).length() > 0) {
            res = 1;

            MailDTO mDTO = new MailDTO();

            mDTO.setToMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(rDTO.getEmail())));

            mDTO.setTitle("로그인 알림!"); //제목

            //메일 내용에 가입자 이름넣어서 내용 발송
            mDTO.setContents(DateUtil.getDateTime("yyyy.MM.dd hh:mm:ss") + "에 "
                    + CmmUtil.nvl(rDTO.getUser_name()) + "님이 로그인하였습니다.");

            //회원 가입이 성공했기 때문에 메일을 발송함
           // mailService.doSendMail(mDTO);
        }

        return res;
    }
}
