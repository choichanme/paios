package com.paios.Service;

import com.paios.Mapper.MemberMapper;
import com.paios.Vo.CompanyVo;
import com.paios.Vo.Enum.Role;
import com.paios.Vo.MemberVo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public interface MemberService {
    public void joinUser(MemberVo memberVo);
    public void sendPwMail(MemberVo vo, String div);
    public void findPw(HttpServletResponse resp, MemberVo vo) throws Exception;
    public Integer certNum( MemberVo vo);
    public String findId(HashMap<String, String> params);
    public String requestChPw(MemberVo vo);
    public void chPassword(MemberVo vo);
    public void saveCompany(HashMap<String, String> params);
    public CompanyVo disCompanyInfo(String userId);
}
