package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.UserInfoDTO;
import com.fourward.linkchart.service.IUserInfoService;
import com.fourward.linkchart.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RequestMapping(value = "/user")
@Controller
@RequiredArgsConstructor
public class UserInfoController {
    private final IUserInfoService userInfoService;

    //회원가잆 정보 전송. 종료시 초기 페이지로 리디렉션.
    @PostMapping(value = "doSignUp")
    public String doSignUp(HttpServletRequest request) {
        log.info(this.getClass().getName() + ".doSignUp start");

        //비밀번호 아이디 등의 유효성 검사는 js 로 처리 되었다는 가정.
        String user_id = request.getParameter("user_id");
        String user_name = request.getParameter("user_name");
        String email = request.getParameter("email");
        String addr = request.getParameter("addr");
        String password = request.getParameter("password");

        log.info("user_id : " + user_id);
        log.info("email : " + email);
        log.info("addr : " + addr);

        UserInfoDTO pDTO = new UserInfoDTO();

        pDTO.setUser_id(user_id);
        pDTO.setUser_name(user_name);
        try {
            pDTO.setPassword(EncryptUtil.encHashSHA256(password));
            pDTO.setEmail(EncryptUtil.encAES128CBC(email));
        } catch (Exception e) {
            log.warn("회원정보 암호화 오류 발생.");
        } finally {
            log.warn(this.getClass().getName() + ".doSignUp failed");
            log.info(this.getClass().getName() + ".doSignUp start");
        }
        pDTO.setAddr(addr);

        //db 삽입
        try {
            userInfoService.insertUserInfo(pDTO);
        } catch (Exception e) {
            log.warn("회원정보 저장 오류 발생.");
        } finally {
            log.warn(this.getClass().getName() + ".doSignUp failed");
            log.info(this.getClass().getName() + ".doSignUp start");
        }

        return "/user/UserRegSuccess";
    }

    //로그인 전송
    @PostMapping(value = "getUserLoginCheck")
    public String getUserLoginCheck(HttpSession session, HttpServletRequest request) {
        log.info(this.getClass().getName() + ".getUserLoginCheck start");

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUser_id(request.getParameter("user_id")); //아이디
        log.info("requested user_id : " + pDTO.getUser_id());
        try {
            pDTO.setPassword(EncryptUtil.encHashSHA256(request.getParameter("password")));
        } catch (Exception e) {
            log.warn("비밀번호 암호화 오류.");
        } finally {
            log.info(this.getClass().getName() + ".getUserLoginCheck end");
        }

        try {
            userInfoService.getUserLoginCheck(pDTO);
        } catch (Exception e) {

        }
        log.info(this.getClass().getName() + ".insertUserInfo end");

        return "/index";
    }

    //아이디 중복검사 요청
    public void checkUserIdExists(HttpServletRequest request) {

    }

    //이메일 중복검사 요청
    public void checkUserEmailExists(HttpServletRequest request) {

    }
}
