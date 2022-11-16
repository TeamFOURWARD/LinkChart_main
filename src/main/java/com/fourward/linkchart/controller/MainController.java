package com.fourward.linkchart.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class MainController {
    @GetMapping(value = "/")
    public String home(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // 기존세션이 존재하지 않음
        if (session == null) {

            return "/index";
        }
        // 로그인한 사용자 세션 존재
        if (session.getAttribute("SS_USER_ID") == null) {

            return "/index";
        }

        return "redirect:/view";
    }

    // 권한 있는 사용자
    @GetMapping(value = "/view")
    public String view(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // 기존세션이 존재하지 않음
        if (session == null) {

            return "redirect:/";
        }
        // 로그인한 사용자 세션 존재
        if (session.getAttribute("SS_USER_ID") == null) {

            return "redirect:/";
        }


        return "/view/ChartAndNews";
    }
}
