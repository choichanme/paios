package com.paios.Service.impl;

import com.paios.Mapper.MemberMapper;
import com.paios.Service.MemberService;
import com.paios.Vo.CompanyVo;
import com.paios.Vo.Enum.Role;
import com.paios.Vo.MemberVo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements UserDetailsService, MemberService {

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    Environment env;

    @Override
    @Transactional
    public void joinUser(MemberVo memberVo) {
        // 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberVo.setPassword(passwordEncoder.encode(memberVo.getPassword()));
        memberMapper.joinMember(memberVo);
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        MemberVo memberVo = memberMapper.findByEmail(userEmail);

        List<GrantedAuthority> authorities = new ArrayList<>();
        String role = memberVo.getRole();

        if (role.equals("B")) {
            authorities.add(new SimpleGrantedAuthority(Role.BUYER.getValue()));
        } else if(role.equals("S")){
            authorities.add(new SimpleGrantedAuthority(Role.SUPPLIER.getValue()));
        } else if(role.equals("F")){
            authorities.add(new SimpleGrantedAuthority(Role.FORWARDING.getValue()));
        } else if(role.equals("A")){
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        }

        return new User(memberVo.getEmail(), memberVo.getPassword(), authorities);
    }

    @Override
    public void sendPwMail(MemberVo vo, String div) {
        String host = "smtps.hiworks.com";
        String user = env.getProperty("hiworks.id");
        String password = env.getProperty("hiworks.password");
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", 465);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable","true");
        props.put("mail.smtp.ssl.trust", "smtps.hiworks.com");
        DecimalFormat formatter = new DecimalFormat("###,###.##");

        try {
            Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(user, password);
                }
            });
            try{
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(user));
                message.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(vo.getEmail())));
                message.setHeader("content-type", "text/html;charset=UTF-8");
                message.setSubject("[파이오스] 이메일 주소 인증을 완료해 주세요");
                String mailText = "";

                if(div.equals("findpw")) {
                    mailText += "<!DOCTYPE html>"
                            + "<html lang=\"kr\" dir=\"ltr\" style=\"margin: 0;padding: 0;box-sizing: border-box;color: #333;\">";
                    mailText += "<head style=\"margin: 0;padding: 0;box-sizing: border-box;color: #333;\">	<meta charset=\"utf-8\">";
                    mailText += "<title style=\"margin: 0;padding: 0;box-sizing: border-box;color: #333;\">파이오스</title>";
                    mailText += "</head>";
                    mailText += "<body>";
                    mailText += vo.getCertNum();
                    mailText += "</body></html>";
                }

                message.setContent(mailText,"text/html;charset=UTF-8");
                Transport.send(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void findPw(HttpServletResponse resp, MemberVo vo) throws Exception {
        resp.setContentType("text/html;charset=utf-8");
        MemberVo ck = memberMapper.findByEmail(vo.getEmail());

        String pw = "";
        for (int i = 0; i < 12; i++) {
            pw += (char) ((Math.random() * 26) + 97);
        }
        vo.setChPwYn("Y");
        vo.setCertNum(pw);
        memberMapper.updateCert(vo);
        sendPwMail(vo, "findpw");
    }

    @Override
    public String findId(HashMap<String, String> params) {
        return memberMapper.findId(params);
    }

    @Override
    public Integer certNum(MemberVo vo) {
       return memberMapper.certNum(vo);
    }

    @Override
    public String requestChPw(MemberVo vo) {

        String email = vo.getEmail();
        UUID key = UUID.randomUUID();
        String url = email+"/"+key.toString();

        return url;
    }

    @Override
    public void chPassword(MemberVo vo) {
        System.out.println("체크" + vo.getPassword());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        vo.setPassword(passwordEncoder.encode(vo.getPassword()));
        memberMapper.chPassword(vo);
    }

    @Override
    public void saveCompany(HashMap<String, String> params) {
        CompanyVo vo = new CompanyVo();

        vo.setUserId(params.get("userId"));
        vo.setCompany(params.get("company"));
        vo.setComAddr(params.get("address"));
        vo.setComNum(params.get("cellnum"));
        vo.setComSite(params.get("website"));
        vo.setCeoName(params.get("ceoname"));
        vo.setMngName(params.get("name"));
        vo.setComRegnum(params.get("comregnum"));
        vo.setMngPosition(params.get("position"));
        vo.setMngNum(params.get("mngnum"));
        vo.setComEmail(params.get("email"));


        memberMapper.saveCompany(vo);
    }

    @Override
    public CompanyVo disCompanyInfo(String userId) {
        return memberMapper.disCompanyInfo(userId);
    }
}
