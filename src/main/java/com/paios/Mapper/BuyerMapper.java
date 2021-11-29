package com.paios.Mapper;

import com.paios.Vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

@Mapper
public interface BuyerMapper {
    public void saveRfq(RfqVo vo);
    public void saveRfqDetail(HashMap<String,String> params);
    public List<RfqVo> getAll(HashMap<String, String > params);
    public List<RfqVo> getRfq(HashMap<String, String > params);
    public int getRfqCount(HashMap<String, String > params);
    public List<KomalQuotationVo> getFeed(HashMap<String, String > params);
    public int getFeedCount(HashMap<String, String > params);
    public KomalQuotationVo getQuotation(String rfqQno);
    public List<RfqDetailVo> getQuotationDetail(HashMap<String, String > params);
    public void allowQuo(String rfqQno, String poNo);
    public void rejectQuo(HashMap<String, String> params);
    public List<BuyerPoVo> getPo(HashMap<String, String > params);
    public int getPoCount(HashMap<String, String > params);
    public BuyerPoVo getKomalPo(String rfqQno);
    public void updateCert(CompanyVo vo);
    public Integer checkCert(String certNum, String userId);
    public void savePoInfo(BuyerPoVo vo);
    public void submitPo(HashMap<String, String> params);
    public BuyerPoVo viewPo(String rfqQno);
    public List<BuyerPoVo> getPay(HashMap<String, String > params);
    public int getPayCount(HashMap<String, String > params);
}
