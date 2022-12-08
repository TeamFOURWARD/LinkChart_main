package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.MessageDTO;
import com.fourward.linkchart.dto.UserInfoDTO;
import com.fourward.linkchart.service.IMailService;
import com.fourward.linkchart.service.IMessageService;
import com.fourward.linkchart.service.IUserInfoService;
import com.fourward.linkchart.util.EncryptUtil;
import com.fourward.linkchart.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RequestMapping(value = "/user")
@Controller
@RequiredArgsConstructor
public class UserInfoController {
    private final IUserInfoService userInfoService;
    private final IMailService mailService;

    private final IMessageService messageService;

    //회원가입 정보 전송. 종료시 초기 페이지로 리디렉션.
    @PostMapping(value = "/doSignUp")
    public String doSignUp(HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        log.info("{}.doSignUp start", this.getClass().getName());

        if (session.getAttribute("SS_SIGNUP_PIN_CHECK") == null) {
            // 전화번호 확인 안된경우로 에러처리 추가 필요 TODO
            return "redirect:/";
        }
        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUser_id(request.getParameter("user_id"));
        pDTO.setUser_name(request.getParameter("user_name"));
        try {
            pDTO.setUser_password(EncryptUtil.encHashSHA256(request.getParameter("user_password")));
            pDTO.setUser_email(EncryptUtil.encAES128CBC(request.getParameter("user_email")));
            pDTO.setUser_addr(EncryptUtil.encAES128CBC(request.getParameter("user_addr")));
            pDTO.setMobile(EncryptUtil.encAES128CBC((String) session.getAttribute("SS_SIGNUP_MOBILE")));// 전화번호
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error_type", "서버 장애 발생");

            return "redirect:/";
        }
        userInfoService.insertUserInfo(pDTO);
        log.info("signup success. user id : [{}] name : [{}]", pDTO.getUser_id(), pDTO.getUser_name());
        redirectAttributes.addFlashAttribute("user_id", pDTO.getUser_id());
        session.invalidate();

        log.info("{}.doSignUp end", this.getClass().getName());

        return "redirect:/";
    }

    //로그인 전송
    @PostMapping(value = "/login")
    public String login(HttpServletRequest request, RedirectAttributes redirectAttributes) {
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
        HttpSession session = request.getSession();
        session.setAttribute("SS_USER_ID", pDTO.getUser_id());
        log.info("session sessionId : [{}]", session.getId());
        log.info("session SS_USER_ID : [{}]", session.getAttribute("SS_USER_ID"));
        log.info("session getMaxInactiveInterval : [{}]", session.getMaxInactiveInterval());
        log.info("session creationTime : [{}]", new Date(session.getCreationTime()));
        //log.info("session lastAccessTime : [{}]", new Date(session.getLastAccessedTime()));

        log.info("[{}] | login success", this.getClass().getName());
        log.info("[{}] | user : [{}]", this.getClass().getName(), pDTO.getUser_id());
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
        if ((userInfoService.checkUserEmailExist(pDTO).getIsExist()).equals("1")) {
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
    @ResponseBody
    public UserInfoDTO getUserInfo(HttpServletRequest request) {
        log.info("{}.getUserInfo start", this.getClass().getName());
        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUser_id(request.getParameter("user_id"));
        log.info("{}.getUserInfo | id : [{}]", this.getClass().getName(), pDTO.getUser_id());
        log.info("{}.getUserInfo end", this.getClass().getName());

        return userInfoService.getUserInfo(pDTO);
    }

    @PostMapping(value = "/makePin")
    @ResponseBody
    public ResponseEntity<Void> makePin(HttpServletRequest request, @RequestBody UserInfoDTO userInfoDTO) {
        log.info("{}.makePin start", this.getClass().getName());
        // 사용자가 입력한 이메일로 핀번호 전송
        String email = userInfoDTO.getUser_email();
        /*
        TODO
        1. 임시세션 만들기(기존에 임시세션 있으면 삭제후 재생성)
        2. 사용자가 입력한 이메일로 핀번호 전송
        3. 사용자 메일로 핀발급
        4. 세션에 발급한 핀 저장해두며 세션에 타임아웃 적용

         */
        request.getSession().invalidate();
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(300);
        session.setAttribute("SS_USER_PIN", "핀");//유틸 핀 생성
        log.info("session sessionId : [{}]", session.getId());
        log.info("session SS_USER_PIN : [{}]", session.getAttribute("SS_USER_PIN"));
        log.info("session getMaxInactiveInterval : [{}]", session.getMaxInactiveInterval());
        log.info("session creationTime : [{}]", new Date(session.getCreationTime()));
        //log.info("session lastAccessTime : [{}]", new Date(session.getLastAccessedTime()));
        // 서비스 : 메일로 생성한 핀 보냄
        log.info("{}.makePin end", this.getClass().getName());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/findId")
    @ResponseBody
    public ResponseEntity<UserInfoDTO> validatePin_(HttpServletRequest request, @RequestBody UserInfoDTO userInfoDTO) {
        log.info("{}.getUserInfo start", this.getClass().getName());
        // 사용자가 메일로 전송받은 핀번호를 입력하면 올바른지 확인후 아이디를 알려줌
        String email = userInfoDTO.getUser_email();
        /*
        TODO
        1. 사용자가 pin 입력
        2. pin 을 발급했을때의 세션과 비교하여 검증. 세션이 타임아웃이 되지않았고 핀이 올바르다면 통과.
        3. 아이디 정보 뷰에 넘김

         */

        log.info("{}.getUserInfo end", this.getClass().getName());

        return new ResponseEntity<>(userInfoDTO, null, HttpStatus.OK);
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

    @GetMapping(value = "/validateMobile")
    public String redirect_validateMobile() {
        return "forward:/html/validateMobileForm.html";
    }

    @PostMapping(value = "/validateMobile/transmit")
    public ResponseEntity<Void> validateMoblie(HttpSession session, HttpServletRequest request, @RequestBody UserInfoDTO userInfoDTO) {
        log.info("mobile number requested : [{}]", userInfoDTO.getMobile());
        if (session.getAttribute("SS_SIGNUP_MOBILE") != null) {
            log.info("전화번호인증 중 이미 이전 작업이 있는것으로 확인됨.");
            // 이전세션간 남은시간의 차이 리턴 TODO
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String pin = RandomUtil.getStr(6);
//        String pin = "123456";
        session.invalidate();
        session = request.getSession();
        session.setAttribute("SS_SIGNUP_MOBILE", userInfoDTO.getMobile());
        session.setAttribute("SS_SIGNUP_PIN", pin);
        session.setMaxInactiveInterval(300);// TODO 세션이 일괄적으로 변경되는것 개선
        log.info("session sessionId : [{}]", session.getId());
        log.info("session maxInactiveInterval : [{}]", session.getMaxInactiveInterval());
        log.info("session SS_SIGNUP_MOBILE : [{}]", session.getAttribute("SS_SIGNUP_MOBILE"));
        log.info("session SS_SIGNUP_PIN : [{}]", session.getAttribute("SS_SIGNUP_PIN"));
        // 전화번호는 유효하다는 가정
        MessageDTO messageDTO = new MessageDTO();
        String text = "[LinkChart]\n==========\n인증코드 : [" + pin + "]";
        messageDTO.setText(text);
        messageDTO.setTo(userInfoDTO.getMobile());
        try {
            messageService.sendMessage(messageDTO);
        } catch (NurigoMessageNotReceivedException e) {
            log.info("휴대폰 인증 핀 보내기 에러 | {}", e.getMessage());
        } catch (Exception e) {
            log.info("휴대폰 인증 핀 보내기 에러 | {}", e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/validateMobile/pin")
    public ResponseEntity<Void> validatePin(HttpSession session, @RequestBody UserInfoDTO userInfoDTO) {
        String pin = userInfoDTO.getPin();
        log.info("pin : [{}]", pin);
        if (session.getAttribute("SS_SIGNUP_MOBILE") == null || !session.getAttribute("SS_SIGNUP_PIN").equals(pin)) {
            log.info("pin check failed");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);// 세션 만료로인한 실패 리턴
        }
        session.setAttribute("SS_SIGNUP_PIN_CHECK", "ok");
        session.setMaxInactiveInterval(299);
        log.info("SS_SIGNUP_PIN_CHECK : [{}]", session.getAttribute("SS_SIGNUP_PIN_CHECK"));
        log.info("session SS_SIGNUP_MOBILE : [{}]", session.getAttribute("SS_SIGNUP_MOBILE"));
        log.info("session isNew : [{}]", session.isNew());
        log.info("session maxInactiveInterval : [{}]", session.getMaxInactiveInterval());

        return new ResponseEntity<>(HttpStatus.OK);
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