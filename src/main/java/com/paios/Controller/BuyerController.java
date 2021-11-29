package com.paios.Controller;

import com.paios.Service.BuyerService;
import com.paios.Service.MemberService;
import com.paios.Util.QueryStringUtils;
import com.paios.Vo.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

@Controller
@AllArgsConstructor
@RequestMapping(value = "buyer")
public class BuyerController {

    @Autowired
    MemberService memberService;

    @Autowired
    BuyerService buyerService;

    @Autowired
    QueryStringUtils queryStringUtils;

    @GetMapping("/allProcess")
    public String disAllProcess() {
        return "/buyer/buyer_qm";
    }

    @GetMapping("/allRfq")
    public String disAllRfq() {

        return "/buyer/buyer_qm_rfq";
    }

    @GetMapping("/allFeed")
    public String disAllFeedBack() {
        return "/buyer/buyer_qm_feedback";
    }

    // rfq등록 페이지
    @GetMapping("/rfq")
    public String disRfq() {
        return "/buyer/buyer_rfq";
    }

    // 코말양식 rfq작성 페이지
    @GetMapping("/komalRfq")
    public String disKomalRfq(Model model, @AuthenticationPrincipal UserDetails user) {
        CompanyVo companyVo = memberService.disCompanyInfo(user.getUsername());

        model.addAttribute("rfqno", genRfqNo());
        model.addAttribute("company", companyVo);
        return "/buyer/buyer_rfq01";
    }

    // 자사양식 rfq작성 페이지
    @GetMapping("/ownRfq")
    public String disOwnRfq(Model model, @AuthenticationPrincipal UserDetails user) {

        CompanyVo companyVo = memberService.disCompanyInfo(user.getUsername());

        model.addAttribute("rfqno", genRfqNo());
        model.addAttribute("company", companyVo);
        return "/buyer/buyer_rfq02";
    }

    //발주 화면
    @GetMapping("/po")
    public String disPo() {
        return "/buyer/buyer_qm_po";
    }

    @GetMapping("/createPo")
    public String createPo(@RequestParam (value = "rfqQno") String rfqQno, Model model) {

        model.addAttribute("rfqQno", rfqQno);

        return "/buyer/buyer_qm_po02";
    }

    @GetMapping("/viewPo")
    public String viewPo(@RequestParam (value = "rfqQno") String rfqQno, Model model, @AuthenticationPrincipal UserDetails user) {

        CompanyVo companyVo = memberService.disCompanyInfo(user.getUsername());
        BuyerPoVo buyerPoVo = buyerService.viewPo(rfqQno);

        model.addAttribute("rfqQno", rfqQno);
        model.addAttribute("company", companyVo);
        model.addAttribute("poVo", buyerPoVo);

        return "/buyer/buyer_qm_po02_03";
    }

    @GetMapping("/payment")
    public String disPayment() {
        return "/buyer/buyer_qm_payment";
    }

    @RequestMapping("/saverfq")
    @ResponseBody
    public void saveRfq(@RequestParam HashMap<String, String> params, @AuthenticationPrincipal UserDetails user) {
        params.put("userId",user.getUsername());

        buyerService.saveRfq(params);
    }

    @RequestMapping("/saveRfqDetail")
    @ResponseBody
    public void saveRfqDetail(@RequestParam HashMap<String, String> params) {
        buyerService.saveRfqDetail(params);
    }

    @ResponseBody
    @RequestMapping(value = "/uploadOwnRfq")
    public void uploadOwnRfq(MultipartHttpServletRequest req) throws Exception {
        MultipartFile file = req.getFile("pdf");

        buyerService.uploadOwnRfq(file);
    }

    @ResponseBody
    @RequestMapping("/getAll")
    public List<RfqVo> getAll(@RequestParam HashMap<String, String> params, @AuthenticationPrincipal UserDetails user) {

        params.put("userId", user.getUsername());

        return buyerService.getAll(params);
    }

    @GetMapping("/alldoc")
    public String allDoc(@RequestParam(value = "rfqQno") String rfqQno, @AuthenticationPrincipal UserDetails user, Model model) {

        model.addAttribute("rfqQno", rfqQno);
        return "/buyer/docu_all";
    }

    @GetMapping("/feed")
    public String disFeed(@RequestParam(value = "rfqQno") String rfqQno, @AuthenticationPrincipal UserDetails user, Model model) {

        KomalQuotationVo komalQuotationVo = buyerService.getQuotation(rfqQno);

        model.addAttribute("QuotationVo", komalQuotationVo);
        return "/buyer/buyer_qm_feedback02";
    }

    @RequestMapping("/allowQuo")
    @ResponseBody
    public void allowQuo(@RequestParam(value = "rfqQno") String rfqQno){

        String poNo = genPoNo();

        buyerService.allowQuo(rfqQno, poNo);
    }

    @RequestMapping("/rejectQuo")
    @ResponseBody
    public void rejectQuo(@RequestParam HashMap<String,String> params){
        System.out.println(params.get("rejectFlag"));
        System.out.println(params.get("rejectTxt"));
        buyerService.rejectQuo(params);
    }

    //코말 견적 디테일정보 들고오기
    @RequestMapping("/getQuotationDetail")
    @ResponseBody
    public List<RfqDetailVo> getKQuotationDetail(@RequestParam HashMap<String, String> params){
        return buyerService.getQuotationDetail(params);
    }

    @ResponseBody
    @RequestMapping("/getRfq")
    public List<RfqVo> getRfq(@RequestParam HashMap<String, String> params, @AuthenticationPrincipal UserDetails user) {

        params.put("userId", user.getUsername());

        return buyerService.getRfq(params);
    }

    @GetMapping("komalPo")
    public String disKomalPo(@RequestParam(value = "rfqQno") String rfqQno, @AuthenticationPrincipal UserDetails user, Model model) {

        CompanyVo companyVo = memberService.disCompanyInfo(user.getUsername());
        BuyerPoVo buyerPoVo = buyerService.getKomalPo(rfqQno);

        model.addAttribute("rfqQno", rfqQno);
        model.addAttribute("company", companyVo);
        model.addAttribute("poVo", buyerPoVo);

        return "/buyer/buyer_qm_po02_01";
    }

    @GetMapping("ownPo")
    public String disOwnPo(Model model, @RequestParam(value = "rfqQno") String rfqQno) {

        model.addAttribute("rfqQno", rfqQno);

        return "/buyer/buyer_qm_po02_02";
    }

    //직인 인증메일 보내기
    @RequestMapping("/certemail")
    @ResponseBody
    public void certEmail(HttpServletResponse resp, @AuthenticationPrincipal UserDetails user) throws Exception {
        CompanyVo vo = new CompanyVo();

        vo.setUserId(user.getUsername());

        buyerService.certEmail(resp, vo);
    }

    //직인 인증번호 확인
    @RequestMapping("/checkCert")
    @ResponseBody
    public Integer CheckCert(@RequestParam(value = "certNum") String certNum, @AuthenticationPrincipal UserDetails user) {
        String userId = user.getUsername();

        return buyerService.checkCert(certNum, userId);
    }

    //rfq 리스트 가져오기
    @GetMapping("/rfqlist")
    public String getRfqList(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails user) {
        int page = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        int offset = (page-1) * limit;

        HashMap params = new HashMap();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        params.put("searchDtFr", request.getParameter("searchDtFr"));
        params.put("searchDtTo", request.getParameter("searchDtTo"));
        params.put("userId", user.getUsername());
        List<RfqVo> rfqVoList = buyerService.getRfq(params);

        model.addAttribute("rfqVo", rfqVoList);

        return "/buyer/buyer_qm_rfq_list";
    }

    //rfq 리스트 페이징
    @GetMapping("/rfqpagination")
    public String rfqPagination(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails user) {

        int limit = Integer.parseInt(request.getParameter("limit"));
        HashMap params = new HashMap();
        params.put("searchDtFr", request.getParameter("searchDtFr"));
        params.put("searchDtTo", request.getParameter("searchDtTo"));
        params.put("userId", user.getUsername());
        int totalCount = buyerService.getRfqCount(params);
        int lastPage = (int) Math.ceil((double) totalCount/limit);

        model.addAttribute("page", request.getParameter("page"));
        model.addAttribute("keyword", request.getParameter("keyword"));
        model.addAttribute("totalCount", totalCount);
        if(lastPage == 0){
            model.addAttribute("lastPage", 1);
        }else {
            model.addAttribute("lastPage", lastPage);
        }
        model.addAttribute("range", 3);

        return "/fragment/async_pagination";
    }

    //feed 리스트 가져오기
    @GetMapping("/feedlist")
    public String getFeedList(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails user){
        int page = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        int offset = (page-1) * limit;

        HashMap params = new HashMap();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        params.put("searchDtFr", request.getParameter("searchDtFr"));
        params.put("searchDtTo", request.getParameter("searchDtTo"));
        params.put("userId", user.getUsername());
        List<KomalQuotationVo> feedList = buyerService.getFeed(params);

        model.addAttribute("feedVo", feedList);

        return "/buyer/buyer_qm_feedback_list";
    }

    //feed 리스트 페이징
    @GetMapping("/feedpagination")
    public String feedPagination(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails user) {

        int limit = Integer.parseInt(request.getParameter("limit"));
        HashMap params = new HashMap();
        params.put("searchDtFr", request.getParameter("searchDtFr"));
        params.put("searchDtTo", request.getParameter("searchDtTo"));
        params.put("userId", user.getUsername());
        int totalCount = buyerService.getFeedCount(params);
        int lastPage = (int) Math.ceil((double) totalCount/limit);

        model.addAttribute("page", request.getParameter("page"));
        model.addAttribute("keyword", request.getParameter("keyword"));
        model.addAttribute("totalCount", totalCount);
        if(lastPage == 0){
            model.addAttribute("lastPage", 1);
        }else {
            model.addAttribute("lastPage", lastPage);
        }
        model.addAttribute("range", 3);

        return "/fragment/async_pagination";
    }

    //po 리스트 가져오기
    @GetMapping("/polist")
    public String getPoList(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails user){
        int page = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        int offset = (page-1) * limit;

        HashMap params = new HashMap();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        params.put("searchDtFr", request.getParameter("searchDtFr"));
        params.put("searchDtTo", request.getParameter("searchDtTo"));
        params.put("userId", user.getUsername());
        List<BuyerPoVo> poList = buyerService.getPo(params);

        model.addAttribute("poVo", poList);

        return "/buyer/buyer_qm_po_list";
    }

    //po 리스트 페이징
    @GetMapping("/popagination")
    public String poPagination(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails user) {

        int limit = Integer.parseInt(request.getParameter("limit"));
        HashMap params = new HashMap();
        params.put("searchDtFr", request.getParameter("searchDtFr"));
        params.put("searchDtTo", request.getParameter("searchDtTo"));
        params.put("userId", user.getUsername());
        int totalCount = buyerService.getPoCount(params);
        int lastPage = (int) Math.ceil((double) totalCount/limit);

        model.addAttribute("page", request.getParameter("page"));
        model.addAttribute("keyword", request.getParameter("keyword"));
        model.addAttribute("totalCount", totalCount);
        if(lastPage == 0){
            model.addAttribute("lastPage", 1);
        }else {
            model.addAttribute("lastPage", lastPage);
        }
        model.addAttribute("range", 3);

        return "/fragment/async_pagination";
    }

    //payment 리스트 가져오기
    @GetMapping("/paylist")
    public String getPayList(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails user){
        int page = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        int offset = (page-1) * limit;

        HashMap params = new HashMap();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        params.put("searchDtFr", request.getParameter("searchDtFr"));
        params.put("searchDtTo", request.getParameter("searchDtTo"));
        params.put("userId", user.getUsername());
        List<BuyerPoVo> payList = buyerService.getPay(params);

        model.addAttribute("payVo", payList);

        return "/buyer/buyer_qm_payment_list";
    }

    //payment 리스트 페이징
    @GetMapping("/paypagination")
    public String payPagination(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails user) {

        int limit = Integer.parseInt(request.getParameter("limit"));
        HashMap params = new HashMap();
        params.put("searchDtFr", request.getParameter("searchDtFr"));
        params.put("searchDtTo", request.getParameter("searchDtTo"));
        params.put("userId", user.getUsername());
        int totalCount = buyerService.getPayCount(params);
        int lastPage = (int) Math.ceil((double) totalCount/limit);

        model.addAttribute("page", request.getParameter("page"));
        model.addAttribute("keyword", request.getParameter("keyword"));
        model.addAttribute("totalCount", totalCount);
        if(lastPage == 0){
            model.addAttribute("lastPage", 1);
        }else {
            model.addAttribute("lastPage", lastPage);
        }
        model.addAttribute("range", 3);

        return "/fragment/async_pagination";
    }

    @RequestMapping(value = "savePoInfo",  method = {RequestMethod.POST , RequestMethod.GET})
    @ResponseBody
    public void savePoInfo(@RequestBody List<BuyerPoVo> params){
        for(BuyerPoVo vo : params){
            buyerService.savePoInfo(vo);
        }
    }

    @RequestMapping(value = "submitPo", method = {RequestMethod.POST , RequestMethod.GET})
    @ResponseBody
    public void submitPo(@RequestParam HashMap<String, String> params) {
            buyerService.submitPo(params);
    }

    //직인 등록
    @ResponseBody
    @PostMapping(value = "uploadSeal", consumes = { "multipart/form-data" })
    public String uploadSeal(@RequestParam(value = "image") MultipartFile multi) {
        try {
            String uploadpath = "/var/upload/img/seal";
            String originFilename = multi.getOriginalFilename();
            String extName = originFilename.substring(originFilename.lastIndexOf("."),originFilename.length());
            long size = multi.getSize();
            String saveFileName = genSaveFileName(extName);

            System.out.println("uploadpath : " + uploadpath);

            System.out.println("originFilename : " + originFilename);
            System.out.println("extensionName : " + extName);
            System.out.println("size : " + size);
            System.out.println("saveFileName : " + saveFileName);

            if(!multi.isEmpty())
            {
                File file = new File(uploadpath, saveFileName);
                if(!file.exists()) // 해당 경로가 없을 경우
                    file.mkdirs();  // 폴더 생성
                multi.transferTo(file);
                String url = "https://www.komalplatform.com/img/seal/"+saveFileName;
                return url;
            }
            return "fail";
        }catch(Exception e)
        {
            System.out.println(e);
            return "fail";
        }
    }

    @ResponseBody
    @PostMapping(value = "uploadRfqPdf", consumes = {"multipart/form-data"})
    public String uploadRfqPdf(@RequestParam(value = "pdf") MultipartFile multi, @RequestParam(value = "rfqQno") String rfqQno, @AuthenticationPrincipal UserDetails user) {
        try {
            String uploadPath = "/upload/pdf/buyer_rfq";
            String originFilename = multi.getOriginalFilename();
            String extName = originFilename.substring(originFilename.lastIndexOf("."),originFilename.length());
            long size = multi.getSize();
            String saveFileName = genSaveFileName(extName);

            System.out.println("uploadpath : " + uploadPath);

            System.out.println("originFilename : " + originFilename);
            System.out.println("extensionName : " + extName);
            System.out.println("size : " + size);
            System.out.println("saveFileName : " + saveFileName);

            if(!multi.isEmpty())
            {
                File file = new File(uploadPath, saveFileName);
                if(!file.exists()) // 해당 경로가 없을 경우
                    file.mkdirs();  // 폴더 생성
                multi.transferTo(file);
                String url = "http://www.idtacorp.com/pdf/buyer_rfq/"+saveFileName;
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("rfqPdf", url);
                hashMap.put("userId", user.getUsername());
                hashMap.put("rfqno", rfqQno);
                buyerService.saveRfq(hashMap);
                return url;
            }
            return "fail";
        }catch(Exception e)
        {
            System.out.println(e);
            return "fail";
        }
    }
    @ResponseBody
    @PostMapping(value = "uploadPoPdf", consumes = {"multipart/form-data"})
    public String uploadPoPdf(@RequestParam(value = "pdf") MultipartFile multi, @RequestParam(value = "rfqQno") String rfqQno, @RequestParam(value = "addDoc") String addDoc, @AuthenticationPrincipal UserDetails user) {
        try {
            String uploadPath = "/upload/pdf/buyer_po";
            String originFilename = multi.getOriginalFilename();
            String extName = originFilename.substring(originFilename.lastIndexOf("."),originFilename.length());
            long size = multi.getSize();
            String saveFileName = genSaveFileName(extName);

            System.out.println("uploadpath : " + uploadPath);

            System.out.println("originFilename : " + originFilename);
            System.out.println("extensionName : " + extName);
            System.out.println("size : " + size);
            System.out.println("saveFileName : " + saveFileName);

            if(!multi.isEmpty())
            {
                File file = new File(uploadPath, saveFileName);
                if(!file.exists()) // 해당 경로가 없을 경우
                    file.mkdirs();  // 폴더 생성
                multi.transferTo(file);
                String url = "http://www.idtacorp.com/pdf/buyer_po/"+saveFileName;
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("poPdf", url);
                hashMap.put("userId", user.getUsername());
                hashMap.put("rfqQno", rfqQno);
                hashMap.put("addDoc", addDoc);
                buyerService.submitPo(hashMap);
                return url;
            }
            return "fail";
        }catch(Exception e)
        {
            System.out.println(e);
            return "fail";
        }
    }


    private String genRfqNo() {
        String rfqNo = "";

        Calendar calendar = Calendar.getInstance();
        rfqNo += calendar.get(Calendar.YEAR);
        rfqNo += calendar.get(Calendar.MONTH) + 1;
        rfqNo += calendar.get(Calendar.DATE);
        rfqNo += calendar.get(calendar.HOUR);
        rfqNo += calendar.get(calendar.MILLISECOND);

        return rfqNo;
    }

    private String genPoNo() {
        String poNo = "";

        Calendar calendar = Calendar.getInstance();
        poNo += "PAIOS-";
        poNo += calendar.get(Calendar.YEAR);
        poNo += calendar.get(Calendar.MONTH) + 1;
        poNo += calendar.get(Calendar.DATE);
        poNo += calendar.get(calendar.HOUR);
        poNo += calendar.get(calendar.MILLISECOND);

        return poNo;
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
