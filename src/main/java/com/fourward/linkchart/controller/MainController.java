package com.fourward.linkchart.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class MainController {
    @GetMapping(value = "/")// 권한 없는 사용자
    public String guest(HttpSession session) {
        if (session.getAttribute("SS_USER_ID") != null) {

            return "redirect:/view";
        }
        log.info("guest session user : [{}]", session.getAttribute("SS_USER_ID"));
        log.info("guest session id : [{}]", session.getId());
        log.info("guest session maxInactiveInterval : [{}]", session.getMaxInactiveInterval());
        log.info("guest session lastAccessedTime : [{}]", session.getLastAccessedTime());
        log.info("guest session creationTime : [{}]", session.getCreationTime());
        log.info("guest session isNew : [{}]",session.isNew());

        return "/index";
    }

    @GetMapping(value = "/view")// 권한 있는 사용자
    public String member(HttpSession session) {
        if (session.getAttribute("SS_USER_ID") == null) {
            session.invalidate();

            return "redirect:/";
        }
        log.info("member session user : [{}]", session.getAttribute("SS_USER_ID"));
        log.info("member session id : [{}]", session.getId());
        log.info("member session maxInactiveInterval : [{}]", session.getMaxInactiveInterval());
        log.info("member session lastAccessedTime : [{}]", session.getLastAccessedTime());
        log.info("member session creationTime : [{}]", session.getCreationTime());
        log.info("member session isNew : [{}]",session.isNew());

        return "/view/ChartAndNews";
    }
}
