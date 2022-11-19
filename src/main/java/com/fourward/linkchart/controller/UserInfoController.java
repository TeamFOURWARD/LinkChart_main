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

@Slf4j
@RequestMapping(value = "/user")
@Controller
@RequiredArgsConstructor
public class UserInfoController {
    private final IUserInfoService userInfoService;

    //회원가입 정보 전송. 종료시 초기 페이지로 리디렉션.
    @PostMapping(value = "/doSignUp")
    public String doSignUp(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        log.info("{}.doSignUp start", this.getClass().getName());

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUser_id(request.getParameter("user_id"));
        pDTO.setUser_name(request.getParameter("user_name"));
        try {
            pDTO.setUser_password(EncryptUtil.encHashSHA256(request.getParameter("user_password")));
            pDTO.setUser_email(EncryptUtil.encAES128CBC(request.getParameter("user_email")));
            pDTO.setUser_addr(EncryptUtil.encAES128CBC(request.getParameter("user_addr")));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error_type", "서버 장애 발생");

            return "redirect:/";
        }
        userInfoService.insertUserInfo(pDTO);
        log.info("signup success. user id : [{}] name : [{}]", pDTO.getUser_id(), pDTO.getUser_name());
        redirectAttributes.addFlashAttribute("user_id", pDTO.getUser_id());

        log.info("{}.doSignUp end", this.getClass().getName());

        return "redirect:/";
    }

    //로그인 전송
    @PostMapping(value = "/login")
    public String login(HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        log.info("{}.login start", this.getClass().getName());

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUser_id(request.getParameter("user_id"));
        try {
            pDTO.setUser_password(EncryptUtil.encHashSHA256(request.getParameter("user_password")));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error_type", "서버 장애 발생");

            return "redirect:/";
        }

        if (userInfoService.getUserLoginCheck(pDTO).getIsExist().equals("0")) {
            log.info("{} | login rejected. invalid id or password.", this.getClass().getName());
            log.info("{}.login end", this.getClass().getName());
            redirectAttributes.addFlashAttribute("error_type", "로그인 실패");

            return "redirect:/";
        }
        session.setAttribute("SS_USER_ID", pDTO.getUser_id());
        session.setMaxInactiveInterval(60 * 60);// 60분

        log.info("{} | login success", this.getClass().getName());
        log.info("{} | user : [{}]", this.getClass().getName(), pDTO.getUser_id());
        log.info("{}.login end", this.getClass().getName());

        return "redirect:/view";
    }

    //아이디 이메일 중복검사 요청
    @ResponseBody
    @PostMapping(value = "/isExist")
    public UserInfoDTO isExist(HttpServletRequest request) {
        log.info("{}.isExist start", this.getClass().getName());

        UserInfoDTO pDTO = new UserInfoDTO();
        String type = request.getParameter("type");
        if (type.equals("user_id")) {
            pDTO.setUser_id(request.getParameter("value"));
            pDTO.setIsExist(userInfoService.checkUserIdExist(pDTO).getIsExist());
            log.info("type : [{}] req : [{}]", type, pDTO.getUser_id());
            log.info("req [{}] is exists? : [{}]", pDTO.getUser_id(), pDTO.getIsExist().equals("1") ? "yes" : "no");
        } else if (type.equals("user_email")) {
            pDTO.setUser_email(request.getParameter("value"));
            pDTO.setIsExist(userInfoService.checkUserEmailExist(pDTO).getIsExist());
            log.info("type : [{}] req : [{}]", type, pDTO.getUser_email());
            log.info("req [{}] is exists? : [{}]", pDTO.getUser_email(), pDTO.getIsExist().equals("1") ? "yes" : "no");
        }

        log.info("{}.isExist end", this.getClass().getName());

        return pDTO;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            log.info("{} | invalidate session. user id : [{}]", this.getClass().getName(), session.getAttribute("SS_USER_ID"));
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