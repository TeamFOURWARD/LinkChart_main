package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.UserInfoDTO;
import com.fourward.linkchart.service.IUserInfoService;
import com.fourward.linkchart.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Slf4j
@RequestMapping(value = "/user")
@Controller
@RequiredArgsConstructor
public class UserInfoController {
    private final IUserInfoService userInfoService;

    //회원가입 정보 전송. 종료시 초기 페이지로 리디렉션.
    @PostMapping(value = "/doSignUp")
    public String doSignUp(HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
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
        pDTO.setUser_password(EncryptUtil.encHashSHA256(user_password));
        pDTO.setUser_email(EncryptUtil.encAES128CBC(user_email));
        pDTO.setUser_addr(EncryptUtil.encAES128CBC(user_addr));

        log.info(pDTO.getUser_id());
        log.info(pDTO.getUser_name());
        log.info(pDTO.getUser_password());
        log.info(pDTO.getUser_email());
        log.info(pDTO.getUser_addr());
        userInfoService.insertUserInfo(pDTO);

        redirectAttributes.addFlashAttribute("user_id", user_id);

        log.info(this.getClass().getName() + ".doSignUp end");

        return "redirect:/";
    }

    //로그인 전송
    @PostMapping(value = "/login")
    public String login(HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
        log.info(this.getClass().getName() + ".login start");

        final String user_id = request.getParameter("user_id");
        final String user_password = request.getParameter("user_password");

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUser_id(user_id);
        pDTO.setUser_password(EncryptUtil.encHashSHA256(user_password));
        log.info("requested user_id : " + user_id);

        if (Objects.equals(userInfoService.getUserLoginCheck(pDTO).getIsExist(), "0")) {
            log.info(this.getClass().getName() + " | login rejected. invalid id or password.");
            log.info(this.getClass().getName() + ".login end");
            redirectAttributes.addFlashAttribute("error_type", "로그인 실패");

            return "redirect:/";
        }
        session.setAttribute("SS_USER_ID", user_id);
        session.setMaxInactiveInterval(60 * 60);// 60분

        log.info(this.getClass().getName() + " | login success.");
        log.info(this.getClass().getName() + " | user : " + user_id);
        log.info(this.getClass().getName() + ".login end");

        return "redirect:/view";
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

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }

    @Deprecated
    @GetMapping(value = "/userErrPage")
    public String userErrPage() {

        return "/user/userErrPage";
    }

    @Deprecated
    @GetMapping(value = "/userRegSuccess")
    public String userRegSuccess() {

        return "/user/userRegSuccess";
    }
}