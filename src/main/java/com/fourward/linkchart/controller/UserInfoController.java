package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.UserInfoDTO;
import com.fourward.linkchart.service.IUserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Slf4j
@RequestMapping(value = "/user")
@Controller
@RequiredArgsConstructor
public class UserInfoController {
    private final IUserInfoService userInfoService;

    //회원가잆 정보 전송. 종료시 초기 페이지로 리디렉션.
    @PostMapping(value = "/doSignUp")
    public String doSignUp(HttpServletRequest request) {
        log.info(this.getClass().getName() + ".doSignUp start");

        //비밀번호 아이디 등의 유효성 검사는 js 로 처리 되었다는 가정.
        final String user_id = request.getParameter("user_id");
        final String user_name = request.getParameter("user_name");
        final String user_email = request.getParameter("user_email");
        final String user_addr = request.getParameter("user_addr");
        final String user_password = request.getParameter("user_password");

        log.info("user_id : " + user_id);
        log.info("email : " + user_email);
        log.info("addr : " + user_addr);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUser_id(user_id);
        pDTO.setUser_name(user_name);
        pDTO.setUser_password(user_password);
        pDTO.setUser_email(user_email);
        pDTO.setUser_addr(user_addr);

        //db 삽입
        try {
            userInfoService.insertUserInfo(pDTO);
        } catch (Exception e) {
            log.warn("회원정보 저장 오류 발생.");
            log.warn(this.getClass().getName() + ".doSignUp failed");
        } finally {
            log.info(this.getClass().getName() + ".doSignUp end");
        }

        return "/user/userRegSuccess";
    }

    //로그인 전송
    @PostMapping(value = "/login")
    public String login(HttpSession session, HttpServletRequest request) {
        log.info(this.getClass().getName() + ".login start");

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUser_id(request.getParameter("user_id"));
        log.info("requested user_id : " + pDTO.getUser_id());
        pDTO.setUser_password(request.getParameter("user_password"));

        try {
            userInfoService.getUserLoginCheck(pDTO);
            session.setAttribute("SS_USER_ID", pDTO.getUser_id());
            session.setAttribute("SS_USER_ROLE", "USER");
            log.info(this.getClass().getName() + " | login success.");
            log.info(this.getClass().getName() + " | identified by : " + pDTO.getUser_id());
        } catch (Exception e) {
            log.warn(this.getClass().getName() + " | login failed. invalid id or password.");
        } finally {
            log.info(this.getClass().getName() + ".login end");
        }

        return "redirect:/index";
    }

    //아이디 이메일 중복검사 요청
    @ResponseBody
    @PostMapping(value = "/isExist")
    public UserInfoDTO isExist(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".isExist start");

        UserInfoDTO pDTO = new UserInfoDTO();
        final String type = request.getParameter("type");
        if (Objects.equals(type, "user_id")) {
            pDTO.setUser_id(request.getParameter("value"));
            log.info("req : " + pDTO.getUser_id());
            pDTO.setIsExist(userInfoService.checkUserIdExist(pDTO).getIsExist());
        } else if (Objects.equals(type, "user_email")) {
            pDTO.setUser_email(request.getParameter("value"));
            log.info("req : " + pDTO.getUser_email());
            pDTO.setIsExist(userInfoService.checkUserEmailExist(pDTO).getIsExist());
        }
        log.info(this.getClass().getName() + ".isExist end");

        return pDTO;
    }
}
