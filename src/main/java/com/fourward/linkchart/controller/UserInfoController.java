package com.fourward.linkchart.controller;

import com.fourward.linkchart.dto.UserInfoDTO;
import com.fourward.linkchart.service.IUserInfoService;
import com.fourward.linkchart.util.CmmUtil;
import com.fourward.linkchart.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@RequestMapping(value="/user")
@Controller
public class UserInfoController {

    /*
     * 비즈니스 로직(중요 로직을 수행하기 위해 사용되는 서비스를 메모리에 적재(싱글톤패턴 적용됨)
     * */
    @Resource(name = "UserInfoService")
    private IUserInfoService userInfoService;


    /**
     * 회원가입 화면으로 이동
     */

    @GetMapping (value = "userRegForm")
    public String userRegForm() {
        log.info(this.getClass().getName() + ".user/userRegForm ok!");

        return "/user/UserRegForm";
    }


    /**
     * 회원가입 로직 처리
     */
    @PostMapping(value = "insertUserInfo")
    public String insertUserInfo(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".insertUserInfo start!");

        //회원가입 결과에 대한 메시지를 전달할 변수
        String msg = "";

        //웹(회원정보 입력화면)에서 받는 정보를 저장할 변수
        UserInfoDTO pDTO = null;

        try {

            String user_id = CmmUtil.nvl(request.getParameter("user_id")); //아이디
            String user_name = CmmUtil.nvl(request.getParameter("user_name")); //이름
            String password = CmmUtil.nvl(request.getParameter("password")); //비밀번호
            String email = CmmUtil.nvl(request.getParameter("email")); //이메일
            String addr1 = CmmUtil.nvl(request.getParameter("addr1")); //주소
            String addr2 = CmmUtil.nvl(request.getParameter("addr2")); //상세주소

            log.info("user_id : " + user_id);
            log.info("user_name : " + user_name);
            log.info("password : " + password);
            log.info("email : " + email);
            log.info("addr1 : " + addr1);
            log.info("addr2 : " + addr2);




            pDTO = new UserInfoDTO();

            pDTO.setUser_id(user_id);
            pDTO.setUser_name(user_name);


            pDTO.setPassword(EncryptUtil.encHashSHA256(password));


            pDTO.setEmail(EncryptUtil.encAES128CBC(email));
            pDTO.setAddr1(addr1);
            pDTO.setAddr2(addr2);


            int res = userInfoService.insertUserInfo(pDTO);

            log.info("회원가입 결과(res) : " + res);

            if (res == 1) {
                msg = "회원가입되었습니다.";

                //추후 회원가입 입력화면에서 ajax를 활용해서 아이디 중복, 이메일 중복을 체크하길 바람
            } else if (res == 2) {
                msg = "이미 가입된 이메일 주소입니다.";

            } else {
                msg = "오류로 인해 회원가입이 실패하였습니다.";

            }

        } catch (Exception e) {
            //저장이 실패되면 사용자에게 보여줄 메시지
            msg = "실패하였습니다. : " + e;
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            log.info(this.getClass().getName() + ".insertUserInfo end!");


            //회원가입 여부 결과 메시지 전달하기
            model.addAttribute("msg", msg);

            //회원가입 여부 결과 메시지 전달하기
            model.addAttribute("pDTO", pDTO);

            //변수 초기화(메모리 효율화 시키기 위해 사용함)
            pDTO = null;

        }

        return "/user/UserRegSuccess";
    }


    /**
     * 로그인을 위한 입력 화면으로 이동
     */
    @GetMapping(value = "loginForm")
    public String loginForm() {
        log.info(this.getClass().getName() + ".user/loginForm ok!");

        return "/user/LoginForm";
    }



    /**
     * 로그인 처리 및 결과 알려주는 화면으로 이동
     */
    @PostMapping(value = "getUserLoginCheck")
    public String getUserLoginCheck(HttpSession session, HttpServletRequest request, ModelMap model) throws Exception {
        log.info(this.getClass().getName() + ".getUserLoginCheck start!");

        int res = 0;

        UserInfoDTO pDTO = null;

        try {

            String user_id = CmmUtil.nvl(request.getParameter("user_id")); //아이디
            String password = CmmUtil.nvl(request.getParameter("password")); //비밀번호

            log.info("user_id : " + user_id);
            log.info("password : " + password);


            pDTO = new UserInfoDTO();

            pDTO.setUser_id(user_id);


            pDTO.setPassword(EncryptUtil.encHashSHA256(password));


            res = userInfoService.getUserLoginCheck(pDTO);

            log.info("res : " + res);

            if (res == 1) { //로그인 성공

                session.setAttribute("SS_USER_ID", user_id);
            }


        } catch (Exception e) {

            res = 2;
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            log.info(this.getClass().getName() + ".insertUserInfo end!");


            model.addAttribute("res", String.valueOf(res));

            pDTO = null;

        }


        return "/user/LoginResult";


    }

}
