package com.paios.Mapper;

import com.paios.Vo.CompanyVo;
import com.paios.Vo.MemberVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface MemberMapper {
    public MemberVo findByEmail(String userEmail);
    public void joinMember(MemberVo memberVo);
    public void updateCert(MemberVo memberVo);
    public Integer certNum(MemberVo memberVo);
    public String findId(HashMap<String, String> params);
    public void chPassword(MemberVo vo);
    public void saveCompany(CompanyVo vo);
    public CompanyVo disCompanyInfo(String userId);
}
