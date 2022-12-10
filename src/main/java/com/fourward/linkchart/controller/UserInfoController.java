package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.*;
import com.fourward.linkchart.service.IMailService;
import com.fourward.linkchart.service.IMessageService;
import com.fourward.linkchart.service.IUserInfoService;
import com.fourward.linkchart.util.EncryptUtil;
import com.fourward.linkchart.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;

@Slf4j
@RequestMapping(value = "/user")
@RestController
@RequiredArgsConstructor
public class UserInfoController {
    private final IUserInfoService userInfoService;
    private final IMailService mailService;
    private final IMessageService messageService;

    //회원가입 정보 전송. 종료시 초기 페이지로 리디렉션.
    @PostMapping(value = "/doSignUp")
    public ResponseEntity<Object> doSignUp(@Valid @RequestBody UserSignupDTO userInfoDTO, HttpSession session) {
        log.info("{}.doSignUp start", this.getClass().getName());
        String mp = (String) session.getAttribute("SS_SIGNUP_MOBILE_PIN");
        String ep = (String) session.getAttribute("SS_SIGNUP_EMAIL_PIN");
        if (mp == null) {

            return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT);
        }
        if (!(mp.equals("ok") && ep.equals("ok"))) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String email = userInfoDTO.getUser_email();
        String email_ss = (String) session.getAttribute("SS_SIGNUP_EMAIL");
        String mobile = userInfoDTO.getMobile();
        String mobile_ss = (String) session.getAttribute("SS_SIGNUP_MOBILE");
        if ((!email.equals(email_ss)) || (!mobile.equals(mobile_ss))) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            userInfoDTO.setUser_password(EncryptUtil.encHashSHA256(userInfoDTO.getUser_password()));
            userInfoDTO.setUser_addr(EncryptUtil.encAES128CBC(userInfoDTO.getUser_addr()));
            userInfoDTO.setUser_email(EncryptUtil.encAES128CBC(email));
            userInfoDTO.setMobile(EncryptUtil.encAES128CBC(mobile));// 전화번호
        } catch (Exception e) {
            log.debug(e.getMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        userInfoService.insertUserInfo(userInfoDTO);
        log.info("signup success. user id : [{}] name : [{}]", userInfoDTO.getUser_id(), userInfoDTO.getUser_name());
        session.invalidate();

        log.info("{}.doSignUp end", this.getClass().getName());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //로그인 전송
    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(@Valid @RequestBody UserLoginDTO userLoginDTO, HttpSession session, HttpServletRequest request) {
        log.info("{}.login start", this.getClass().getName());
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUser_id(userLoginDTO.getUser_id());
        userInfoDTO.setUser_password(userLoginDTO.getUser_password());
        try {
            userInfoDTO.setUser_password(EncryptUtil.encHashSHA256(userInfoDTO.getUser_password()));
        } catch (Exception e) {
            log.debug(e.getMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (session.getAttribute("SS_USER_ID") != null || userInfoService.getUserLoginCheck(userInfoDTO).getIsExist().equals("0")) {
            log.info("{} | login rejected. invalid user.", this.getClass().getName());
            log.info("{}.login end", this.getClass().getName());
            session.invalidate();

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        session.invalidate();
        session = request.getSession();
        session.setAttribute("SS_USER_ID", userInfoDTO.getUser_id());
        log.info("session sessionId : [{}]", session.getId());
        log.info("session SS_USER_ID : [{}]", session.getAttribute("SS_USER_ID"));
        log.info("session getMaxInactiveInterval : [{}]", session.getMaxInactiveInterval());
        log.info("session creationTime : [{}]", new Date(session.getCreationTime()));
        //log.info("session lastAccessTime : [{}]", new Date(session.getLastAccessedTime()));

        log.info("[{}] | login success", this.getClass().getName());
        log.info("[{}] | user : [{}]", this.getClass().getName(), userInfoDTO.getUser_id());
        log.info("{}.login end", this.getClass().getName());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        log.info("{}.logout start", this.getClass().getName());
        HttpSession session = request.getSession(false);
        if (session != null) {
            log.info("{} | invalidate session. user id : [{}]", this.getClass().getName(), session.getAttribute("SS_USER_ID"));
            session.invalidate();
        }
        log.info("{}.logout end", this.getClass().getName());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/updatePsw")
    public ResponseEntity<Void> updatePsw(HttpServletRequest request) {
        log.info("{}.updatePsw start", this.getClass().getName());
        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUser_id(request.getParameter("user_id"));
        try {
            pDTO.setUser_password(EncryptUtil.encHashSHA256(request.getParameter("user_password")));
        } catch (Exception ignored) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (userInfoService.getUserLoginCheck(pDTO).getIsExist().equals("1")) {

            return new ResponseEntity<>(HttpStatus.valueOf(409));
        }
        log.info("{}.updatePsw | id : [{}]", this.getClass().getName(), pDTO.getUser_id());
        userInfoService.updateUserPsw(pDTO);

        log.info("{}.updatePsw end", this.getClass().getName());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/updateEmail")
    public ResponseEntity<Void> updateEmail(HttpServletRequest request) {
        log.info("{}.updateEmail start", this.getClass().getName());
        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUser_id(request.getParameter("user_id"));
        try {
            pDTO.setUser_email(EncryptUtil.encAES128CBC(request.getParameter("user_email")));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!((userInfoService.isEmailExists(pDTO.getUser_email()).equals("0")))) {
            return new ResponseEntity<>(HttpStatus.valueOf(409));
        }
        log.info("{}updateEmail | id : [{}]", this.getClass().getName(), pDTO.getUser_id());
        userInfoService.updateUserEmail(pDTO);

        log.info("{}.updateEmail end", this.getClass().getName());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/updateAddr")
    public ResponseEntity<Void> updateAddr(HttpServletRequest request) {
        log.info("{}.updateAddr start", this.getClass().getName());
        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUser_id(request.getParameter("user_id"));
        try {
            pDTO.setUser_addr(EncryptUtil.encAES128CBC(request.getParameter("user_addr")));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("{}.updateAddr | id : [{}]", this.getClass().getName(), pDTO.getUser_id());
        userInfoService.updateUserAddr(pDTO);

        log.info("{}.updateAddr end", this.getClass().getName());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/getUserInfo")
    public ResponseEntity<UserInfoDTO> getUserInfo(HttpServletRequest request) {
        log.info("{}.getUserInfo start", this.getClass().getName());
        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUser_id(request.getParameter("user_id"));
        log.info("{}.getUserInfo | id : [{}]", this.getClass().getName(), pDTO.getUser_id());
        log.info("{}.getUserInfo end", this.getClass().getName());

        return new ResponseEntity<>(userInfoService.getUserInfo(pDTO), HttpStatus.OK);
    }

    // 인증단계 초기화
    @GetMapping(value = "/invalidate")
    public ResponseEntity<Object> invalidateSession(HttpSession session) {
        session.invalidate();
        log.info("{}.invalidateSession done", this.getClass().getName());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/validate/id")
    public ResponseEntity<Object> validateId(@RequestBody UserInfoDTO userInfoDTO) {
        log.info("{}.validateId start", this.getClass().getName());
        if (!((userInfoService.isIdExists(userInfoDTO.getUser_id()).equals("0")))) {
            // 중복 아이디
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("{}.validateId end", this.getClass().getName());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/validate/Mobile")
    public ResponseEntity<Object> validateMobile(@RequestBody UserInfoDTO userInfoDTO, HttpSession session, HttpServletRequest request) {
        log.info("{}.validateMobile start", this.getClass().getName());
        final long SS = 300L - (System.currentTimeMillis() - session.getCreationTime()) / 1000L;
        log.info("session remaining time : [{}]", SS);
        if (SS < 0) {
            session.invalidate();
            session = request.getSession();
        }
        if (session.getAttribute("SS_SIGNUP_MOBILE") != null) {
            log.info("이전 전화번호인증 작업이 있음.");
            log.info("session Id: [{}]", session.getId());
            log.info("session maxInactiveInterval : [{}]", session.getMaxInactiveInterval());

            return new ResponseEntity<>(SS, HttpStatus.BAD_REQUEST);// 400
        }
        String mobile = userInfoDTO.getMobile();
        log.info("mobile number requested : [{}]", mobile);
        try {// 중복 전화번호 확인
            if (!((userInfoService.isMobileExists(EncryptUtil.encAES128CBC(mobile))).equals("0"))) {
                log.info("duplicate mobile number requested. return error.");
                session.invalidate();

                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);// 406
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            session.invalidate();

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String pin = RandomUtil.getStr(8);
        // 전화번호는 유효하다는 가정
        MessageDTO messageDTO = new MessageDTO();
        String text = "[LinkChart]\n==========\n인증코드 : [" + pin + "]";
        messageDTO.setText(text);
        messageDTO.setTo(mobile);
        try {
            messageService.sendMessage(messageDTO);
        } catch (Exception e) {
            log.info("휴대폰 인증 핀 보내기 에러 | {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        session.setMaxInactiveInterval(300);
        session.setAttribute("SS_SIGNUP_MOBILE", mobile);
        session.setAttribute("SS_SIGNUP_MOBILE_PIN", pin);
        log.info("session sessionId : [{}]", session.getId());
        log.info("session maxInactiveInterval : [{}]", session.getMaxInactiveInterval());
        log.info("session SS_SIGNUP_MOBILE : [{}]", session.getAttribute("SS_SIGNUP_MOBILE"));
        log.info("session SS_SIGNUP_MOBILE_PIN : [{}]", session.getAttribute("SS_SIGNUP_MOBILE_PIN"));
        log.info("{}.validateMobile end", this.getClass().getName());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/validate/Mobile/pin")
    public ResponseEntity<Object> validateMobilePin(@RequestBody UserInfoDTO userInfoDTO, HttpSession session) {
        log.info("{}.validateMobliePin start", this.getClass().getName());
        final long SS = 300L - (System.currentTimeMillis() - session.getCreationTime()) / 1000L;
        log.info("session remaining time : [{}]", SS);
        if (SS < 0 || session.getAttribute("SS_SIGNUP_MOBILE") == null) {
            session.invalidate();

            return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT);
        }
        String pin = userInfoDTO.getPin();
        log.debug("user mobile pin : [{}]", pin);
        if (!session.getAttribute("SS_SIGNUP_MOBILE_PIN").equals(pin)) {
            log.info("pin check failed");

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        session.setAttribute("SS_SIGNUP_MOBILE_PIN", "ok");
        log.info("SS_SIGNUP_MOBILE_PIN : [{}]", session.getAttribute("SS_SIGNUP_MOBILE_PIN"));
        log.info("session SS_SIGNUP_MOBILE : [{}]", session.getAttribute("SS_SIGNUP_MOBILE"));
        log.info("session isNew : [{}]", session.isNew());
        log.info("session maxInactiveInterval : [{}]", session.getMaxInactiveInterval());
        log.info("{}.validateMobilePin end", this.getClass().getName());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/validate/email")
    public ResponseEntity<Object> validateEmail(@RequestBody UserInfoDTO userInfoDTO, HttpSession session) {
        log.info("{}.validateEmail start", this.getClass().getName());
        String email = userInfoDTO.getUser_email();
        String pin = RandomUtil.getStr(8);
        try {
            if (!(userInfoService.isEmailExists(EncryptUtil.encAES128CBC(email)).equals("0"))) {

                log.info("{}.validateEmail failed", this.getClass().getName());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("user email : [{}]", email);
        log.debug("email pin : [{}]", pin);
        MailDTO mailDTO = new MailDTO();
        mailDTO.setFrom("Admin@LinkChart.net");
        mailDTO.setTo(email);
        mailDTO.setSubject("[LinkChart 인증번호 안내]");
        mailDTO.setText("인증번호를 5분안에 입력해주시기 바랍니다.\n" + "인증번호 : [" + pin + "]\n");
        if (mailService.doSendMail(mailDTO) != 0) {
            log.info("{}.validateEmail failed", this.getClass().getName());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        session.setAttribute("SS_SIGNUP_EMAIL_PIN", pin);
        session.setAttribute("SS_SIGNUP_EMAIL", email);
        log.info("{}.validateEmail end", this.getClass().getName());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/validate/email/pin")
    public ResponseEntity<Object> validateEmailPin(HttpSession session, @RequestBody UserInfoDTO userInfoDTO) {
        log.info("{}.validateEmailPin start", this.getClass().getName());
        String pin = userInfoDTO.getPin();
        log.info("pin : [{}]", pin);
        if (session.getAttribute("SS_SIGNUP_EMAIL_PIN") == null) {
            session.invalidate();
            return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT);
        }
        if (!session.getAttribute("SS_SIGNUP_EMAIL_PIN").equals(pin)) {
            log.info("pin check failed");

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        session.setAttribute("SS_SIGNUP_EMAIL_PIN", "ok");
        log.info("SS_SIGNUP_EMAIL_PIN : [{}]", session.getAttribute("SS_SIGNUP_EMAIL_PIN"));
        log.info("session isNew : [{}]", session.isNew());
        log.info("session maxInactiveInterval : [{}]", session.getMaxInactiveInterval());
        log.info("{}.validateEmailPin end", this.getClass().getName());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/findPwd")
    @ResponseBody
    public ResponseEntity<UserInfoDTO> findPwd(HttpServletRequest request, @RequestBody UserInfoDTO userInfoDTO) {
        log.info("{}.getUserInfo start", this.getClass().getName());
        // 사용자가 메일로 전송받은 핀번호를 입력하면 올바른지 확인후 메일로 임시 비밀번호 발급해줌.
        String email = userInfoDTO.getUser_email();
        /*
        TODO
        1. 사용자가 pin 입력
        2. 사용자가 id 입력
        3. pin 을 발급했을때의 세션과 비교하여 검증. 세션이 타임아웃이 되지않았고 핀이 올바르다면 통과.
        4. 임시비밀번호 사용자 메일로 전송

         */

        log.info("{}.getUserInfo end", this.getClass().getName());

        return new ResponseEntity<>(userInfoDTO, null, HttpStatus.OK);
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