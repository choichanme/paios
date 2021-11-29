package com.paios.Controller;

import com.paios.Service.MemberService;
import com.paios.Util.ScriptUtils;
import com.paios.Vo.CompanyVo;
import com.paios.Vo.MemberVo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Controller
@AllArgsConstructor
public class MemberController {

    @Autowired
    MemberService memberService;

    // 메인 페이지
    @GetMapping("/")
    public String index() {
        return "main";
    }

    // 회원가입 페이지
    @GetMapping("/user/signup")
    public String dispSignup() {
        return "signup";
    }

//    // 회원가입 처리
//    @PostMapping("/user/signup")
//    public String execSignup(MemberVo memberVo) {
//        memberService.joinUser(memberVo);
//
//        return "redirect:/login";
//    }

    // 회원가입 페이지
    @GetMapping("/user/register")
    public String dispRegister() {
        return "register";
    }

    // 회원가입 처리
    @RequestMapping("/user/register")
    public void execRegister(MemberVo memberVo, HttpServletResponse response) throws IOException {
        memberService.joinUser(memberVo);
        ScriptUtils.alertAndMovePage(response, "회원가입이 완료되었습니다..", "/login");
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String dispLogin(HttpServletRequest request)
    {
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referrer);
        return "login";
    }

    // 로그인 결과 페이지
    @GetMapping("/user/login/result")
    public String dispLoginResult() {
        return "loginSuccess";
    }

//    // 로그아웃 결과 페이지
//    @GetMapping("/user/logout/result")
//    public String dispLogout() {
//        return "logout";
//    }

    // 접근 거부 페이지
    @GetMapping("/user/denied")
    public void dispDenied(HttpServletResponse response, Authentication authentication) throws IOException {
        if(authentication.getAuthorities().toString().equals("[ROLE_BUYER]")){
            ScriptUtils.alertAndMovePage(response, "접근권한이 없습니다.", "/");
        }else if(authentication.getAuthorities().toString().equals("[ROLE_SUPPLIER]")){
            ScriptUtils.alertAndMovePage(response, "접근권한이 없습니다.", "/supplier/main");
        }else if(authentication.getAuthorities().toString().equals("[ROLE_FORWARDING]")){
            ScriptUtils.alertAndMovePage(response, "접근권한이 없습니다.", "/forward/main");
        }
    }

    // 내 정보 페이지
    @GetMapping("/user/info")
    public String dispMyInfo() {
        return "user/myinfo";
    }

    // 내 정보 페이지
    @GetMapping("/user/mypage")
    public String dispMyPage(Model model, @AuthenticationPrincipal UserDetails user) {
        CompanyVo companyVo = memberService.disCompanyInfo(user.getUsername());

        model.addAttribute("company", companyVo);
        return "user/mypage";
    }

    // 내 정보 페이지
    @GetMapping("/user/supplier/mypage")
    public String dispSupMyPage(Model model, @AuthenticationPrincipal UserDetails user) {
        CompanyVo companyVo = memberService.disCompanyInfo(user.getUsername());

        model.addAttribute("company", companyVo);
        return "user/supplier_mypage";
    }

    // 내 정보 페이지
    @GetMapping("/user/findInquiry")
    public String findInquiry() {
        return "find_inquiry";
    }

    // 어드민 페이지
    @GetMapping("/admin")
    public String dispAdmin() {
        return "admin/admin";
    }

    @PostMapping("/user/findpw")
    public void findPw(@RequestBody MemberVo vo, HttpServletResponse resp) throws Exception {
        memberService.findPw(resp, vo);
    }

    @PostMapping("/user/certnum")
    @ResponseBody
    public String certNum(@RequestBody MemberVo vo) {
         int count =  memberService.certNum(vo);

         if(count == 1) {
            return memberService.requestChPw(vo);
         }else{
             return "";
         }
    }

    @RequestMapping("/user/dupCheckId")
    @ResponseBody
    public String dupCheckId(@RequestParam HashMap<String, String> params) {
        return memberService.findId(params);
    }

    @PostMapping("/user/password/{user}/{key}")
    public String chPwPage(@PathVariable String user, HttpServletRequest req) {
        req.setAttribute("user" , user);

        return "pw_chg";
    }

    @RequestMapping("/user/chpassword")
    @ResponseBody
    public void chPassword(HttpServletRequest request, MemberVo vo) {
        String email = request.getParameter("email");
        String pass = request.getParameter("password");
        vo.setEmail(email);
        vo.setPassword(pass);
        memberService.chPassword(vo);
    }

    @RequestMapping("/user/savecompany")
    @ResponseBody
    public void saveCompany(@RequestParam HashMap<String, String> params, @AuthenticationPrincipal UserDetails user, HttpServletRequest req) {
        params.put("userId", user.getUsername());
        memberService.saveCompany(params);
    }

}
