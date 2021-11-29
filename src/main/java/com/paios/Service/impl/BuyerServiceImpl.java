package com.paios.Service.impl;

import com.paios.Mapper.BuyerMapper;
import com.paios.Mapper.MemberMapper;
import com.paios.Service.BuyerService;
import com.paios.Vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Service
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    BuyerMapper buyerMapper;

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    Environment env;

    @Override
    public void saveRfq(HashMap<String, String> params){
        RfqVo rfqVo = new RfqVo();

        rfqVo.setUserId(params.get("userId"));
        rfqVo.setRfqDt(params.get("today"));
        rfqVo.setRfqQno(params.get("rfqno"));
        rfqVo.setRfqTo(params.get("rfqto"));
        rfqVo.setRfqAttention(params.get("attention"));
        rfqVo.setRfqItem(params.get("qutation"));
        rfqVo.setDeadline(params.get("deadline"));
        rfqVo.setIncotermsFlag(params.get("incoterms"));
        rfqVo.setPayTermsFlag(params.get("payterm"));
        rfqVo.setPayTerms(params.get("addterm"));
        rfqVo.setCurrencyFlag(params.get("currency"));
        rfqVo.setShipmentDt(params.get("shipdate"));
        rfqVo.setDelLoc(params.get("location"));
        rfqVo.setDDay(params.get("dday"));
        rfqVo.setRfqPdf(params.get("rfqPdf"));

        buyerMapper.saveRfq(rfqVo);
    }

    @Override
    public void saveRfqDetail(HashMap<String,String> params) {
//        RfqDetailVo rfqDetailVo = new RfqDetailVo();
//
//        rfqDetailVo.setRfqQno();
//        rfqDetailVo.setAmount();
//        rfqDetailVo.setDescription();
//        rfqDetailVo.setIndex();
//        rfqDetailVo.setExtendedPrice();
//        rfqDetailVo.setItemName();
//        rfqDetailVo.setRemark();
//        rfqDetailVo.setUPrice();
//        rfqDetailVo.setQty();

        buyerMapper.saveRfqDetail(params);
    }

    @Override
    public void uploadOwnRfq(MultipartFile file) throws Exception{
        String uploadPath = "/upload/pdf/own_rfq";
        String originalFileName = file.getOriginalFilename();
        String extName = originalFileName.substring(originalFileName.lastIndexOf("."),originalFileName.length());
        String saveFileName = genSaveFileName(extName);

        System.out.println("uploadpath : " + uploadPath);
        System.out.println("originFilename : " + originalFileName);
        System.out.println("extensionName : " + extName);
        System.out.println("saveFileName : " + saveFileName);

        try{
            if(!file.isEmpty()){
                file.transferTo(new File(uploadPath, saveFileName));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public List<RfqVo> getAll(HashMap<String,String> params) {
        return buyerMapper.getAll(params);
    }

    @Override
    public List<RfqVo> getRfq(HashMap<String,String> params) {
        return buyerMapper.getRfq(params);
    }

    @Override
    public int getRfqCount(HashMap<String,String> params) {
        return buyerMapper.getRfqCount(params);
    }

    @Override
    public List<KomalQuotationVo> getFeed(HashMap<String,String> params) {
        return buyerMapper.getFeed(params);
    }

    @Override
    public int getFeedCount(HashMap<String,String> params) {
        return buyerMapper.getFeedCount(params);
    }

    @Override
    public KomalQuotationVo getQuotation(String rfqQno) {
        return buyerMapper.getQuotation(rfqQno);
    }

    @Override
    public List<RfqDetailVo> getQuotationDetail(HashMap<String, String> params) {
        return buyerMapper.getQuotationDetail(params);
    }

    @Override
    public void allowQuo(String rfqQno, String poNo) {
        buyerMapper.allowQuo(rfqQno, poNo);
    }

    @Override
    public void rejectQuo(HashMap<String ,String> params) {
        buyerMapper.rejectQuo(params);
    }

    @Override
    public List<BuyerPoVo> getPo(HashMap<String,String> params) {
        return buyerMapper.getPo(params);
    }

    @Override
    public int getPoCount(HashMap<String,String> params) {
        return buyerMapper.getPoCount(params);
    }

    @Override
    public List<BuyerPoVo> getPay(HashMap<String,String> params) {
        return buyerMapper.getPay(params);
    }

    @Override
    public int getPayCount(HashMap<String,String> params) {
        return buyerMapper.getPayCount(params);
    }

    @Override
    public BuyerPoVo getKomalPo(String rfqQno) {
        return buyerMapper.getKomalPo(rfqQno);
    }

    @Override
    public void certEmail(HttpServletResponse resp, CompanyVo vo) throws Exception {
        resp.setContentType("text/html;charset=utf-8");
        CompanyVo ck = memberMapper.disCompanyInfo(vo.getUserId());

        String pw = "";
        for (int i = 0; i < 12; i++) {
            pw += (char) ((Math.random() * 26) + 97);
        }

        vo.setCertNum(pw);
        buyerMapper.updateCert(vo);

        sendCertMail(ck, "poCert");
    }

    @Override
    public void sendCertMail(CompanyVo vo, String div) {
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
                message.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(vo.getComEmail())));
                message.setHeader("content-type", "text/html;charset=UTF-8");
                message.setSubject("[파이오스] 직인 인증을 완료해 주세요");
                String mailText = "";

                if(div.equals("poCert")) {
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
    public Integer checkCert(String certNum, String userId) {
        return buyerMapper.checkCert(certNum, userId);
    }

    @Override
    public void savePoInfo(BuyerPoVo vo) {
        buyerMapper.savePoInfo(vo);
    }

    @Override
    public BuyerPoVo viewPo(String rfqQno){
        return buyerMapper.viewPo(rfqQno);
    }

    @Override
    public void submitPo(HashMap<String, String> params){
        buyerMapper.submitPo(params);
    }

    // 현재 시간을 기준으로 파일 이름 생성
    private String genSaveFileName(String extName) {
        String fileName = "";

        Calendar calendar = Calendar.getInstance();
        fileName += calendar.get(Calendar.YEAR);
        fileName += calendar.get(Calendar.MONTH);
        fileName += calendar.get(Calendar.DATE);
        fileName += calendar.get(Calendar.HOUR);
        fileName += calendar.get(Calendar.MINUTE);
        fileName += calendar.get(Calendar.SECOND);
        fileName += calendar.get(Calendar.MILLISECOND);
        fileName += extName;

        return fileName;
    }
}
