package com.paios.Service;

import com.paios.Vo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

public interface BuyerService {
    public void saveRfq(HashMap<String,String> params);
    public void saveRfqDetail(HashMap<String,String> params);
    public void uploadOwnRfq (MultipartFile file) throws Exception;
    public List<RfqVo> getAll(HashMap<String,String> params);
    public List<RfqVo> getRfq(HashMap<String,String> params);
    public int getRfqCount(HashMap<String,String> params);
    public List<KomalQuotationVo> getFeed(HashMap<String,String> params);
    public int getFeedCount(HashMap<String,String> params);
    public KomalQuotationVo getQuotation(String rfqQno);
    public List<RfqDetailVo> getQuotationDetail(HashMap<String, String> params);
    public void allowQuo(String rfqQno, String poNo);
    public void rejectQuo(HashMap<String, String> params);
    public List<BuyerPoVo> getPo(HashMap<String,String> params);
    public int getPoCount(HashMap<String,String> params);
    public BuyerPoVo getKomalPo(String rfqQno);
    public void certEmail(HttpServletResponse resp, CompanyVo vo) throws Exception;
    public void sendCertMail(CompanyVo vo, String div);
    public Integer checkCert(String certNum, String userId);
    public void savePoInfo(BuyerPoVo vo);
    public void submitPo(HashMap<String, String> params);
    public BuyerPoVo viewPo(String rfqQno);
    public List<BuyerPoVo> getPay(HashMap<String,String> params);
    public int getPayCount(HashMap<String,String> params);

}
