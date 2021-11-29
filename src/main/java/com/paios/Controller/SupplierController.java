package com.paios.Controller;

import com.paios.Service.MemberService;
import com.paios.Service.SupplierService;
import com.paios.Vo.BuyerPoVo;
import com.paios.Vo.CompanyVo;
import com.paios.Vo.RfqDetailVo;
import com.paios.Vo.RfqVo;
import net.sf.log4jdbc.log.SpyLogDelegator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    SupplierService supplierService;

    @Autowired
    MemberService memberService;

    // 메인 페이지
    @GetMapping("/main")
    public String mainSupplier(Model model) throws ParseException {
        List<RfqVo> rfqVoList = supplierService.getMainRfq();

        model.addAttribute("rfqVo", rfqVoList);

        return "supplier/main_supplier";
    }

    @GetMapping("/allDoc")
    public String disAllDoc(){
        return "/supplier/supplier_qm";
    }

    @GetMapping("/allQuo")
    public String disAllQuo(){
        return "/supplier/supplier_qm_rfq";
    }

    @GetMapping("/allPay")
    public String disAllPay(){
        return "/supplier/supplier_qm_payment";
    }

    @GetMapping("/payRfq")
    public String disPayRfq(@RequestParam(value = "rfqQno") String rfqQno ,Model model){

        model.addAttribute("rfqQno", rfqQno);

        return "/supplier/supplier_qm_payment_rfq";
    }

    @GetMapping("/komalPay")
    public String komalPay(@RequestParam(value = "rfqQno") String rfqQno, Model model, @AuthenticationPrincipal UserDetails user){

        CompanyVo companyVo = memberService.disCompanyInfo(user.getUsername());
        BuyerPoVo buyerPoVo = supplierService.getKomalPo(rfqQno);

        model.addAttribute("company", companyVo);
        model.addAttribute("poVo", buyerPoVo);

        return "/supplier/supplier_qm_payment_pl";
    }

    @GetMapping("/payView")
    public String payView(@RequestParam(value = "rfqQno") String rfqQno, Model model, @AuthenticationPrincipal UserDetails user){

        CompanyVo companyVo = memberService.disCompanyInfo(user.getUsername());
        BuyerPoVo buyerPoVo = supplierService.getKomalPo(rfqQno);

        model.addAttribute("company", companyVo);
        model.addAttribute("poVo", buyerPoVo);

        return "/supplier/supplier_qm_payment_view";
    }

    @GetMapping("/shipment")
    public String disShipment(@RequestParam(value = "rfqQno") String rfqQno){
        return "/supplier/supplier_qm_payment_shipment";
    }

    @GetMapping("/ownPay")
    public String ownPay(){
        return "/supplier/supplier_qm_payment_rfq02";
    }

    @GetMapping("/allOrder")
    public String disAllOrder(){
        return "/supplier/supplier_qm_order";
    }

    @GetMapping("/orderPo")
    public String disOrderPo(@RequestParam(value = "rfqQno") String rfqQno, Model model, @AuthenticationPrincipal UserDetails user) {

        BuyerPoVo buyerPoVo = supplierService.getKomalPo(rfqQno);
        CompanyVo companyVo = memberService.disCompanyInfo(user.getUsername());

        model.addAttribute("rfqQno", rfqQno);
        model.addAttribute("company", companyVo);
        model.addAttribute("poVo", buyerPoVo);

        return "/supplier/supplier_qm_order_po";
    }

    @GetMapping("/liveRfq")
    public String liveRfq() {
        return "supplier/supplier_live";
    }

    @GetMapping("/rfqlist")
    public String getRfqList(HttpServletRequest request, Model model) throws ParseException {
        int page = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        int offset = (page-1) * limit;

        HashMap params = new HashMap();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        List<RfqVo> rfqVoList = supplierService.getRfq(params);

        model.addAttribute("rfqVo", rfqVoList);

        return "/supplier/supplier_live_list";
    }

    //rfq 리스트 페이징
    @GetMapping("/rfqpagination")
    public String rfqPagination(HttpServletRequest request, Model model) {

        int limit = Integer.parseInt(request.getParameter("limit"));
        HashMap params = new HashMap();
        params.put("limit", String.valueOf(limit));
        int totalCount = supplierService.getRfqCount();
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

    //quotation 리스트 가져오기
    @GetMapping("/quolist")
    public String getQuoList(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails user) {
        int page = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        int offset = (page-1) * limit;

        HashMap params = new HashMap();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        params.put("searchDtFr", request.getParameter("searchDtFr"));
        params.put("searchDtTo", request.getParameter("searchDtTo"));
        params.put("userId", user.getUsername());
        List<RfqVo> quoList = supplierService.getQuo(params);

        model.addAttribute("quoVo", quoList);

        return "/supplier/supplier_qm_rfq_list";
    }

    //quotation 리스트 페이징
    @GetMapping("/quopagination")
    public String quoPagination(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails user) {

        int limit = Integer.parseInt(request.getParameter("limit"));
        HashMap params = new HashMap();
        params.put("searchDtFr", request.getParameter("searchDtFr"));
        params.put("searchDtTo", request.getParameter("searchDtTo"));
        params.put("userId", user.getUsername());
        int totalCount = supplierService.getQuoCount(params);
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

    //order 리스트 가져오기
    @GetMapping("/orderlist")
    public String getOrderList(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails user) {
        int page = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        int offset = (page-1) * limit;

        HashMap params = new HashMap();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        params.put("searchDtFr", request.getParameter("searchDtFr"));
        params.put("searchDtTo", request.getParameter("searchDtTo"));
        params.put("userId", user.getUsername());
        List<RfqVo> orderList = supplierService.getOrder(params);

        model.addAttribute("orderVo", orderList);

        return "/supplier/supplier_qm_order_list";
    }

    //order 리스트 페이징
    @GetMapping("/orderpagination")
    public String orderPagination(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails user) {

        int limit = Integer.parseInt(request.getParameter("limit"));
        HashMap params = new HashMap();
        params.put("searchDtFr", request.getParameter("searchDtFr"));
        params.put("searchDtTo", request.getParameter("searchDtTo"));
        params.put("userId", user.getUsername());
        int totalCount = supplierService.getOrderCount(params);
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

    //order 리스트 가져오기
    @GetMapping("/paylist")
    public String getPayList(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails user) {
        int page = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        int offset = (page-1) * limit;

        HashMap params = new HashMap();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        params.put("searchDtFr", request.getParameter("searchDtFr"));
        params.put("searchDtTo", request.getParameter("searchDtTo"));
        params.put("userId", user.getUsername());
        List<RfqVo> payList = supplierService.getPay(params);

        model.addAttribute("payVo", payList);

        return "/supplier/supplier_qm_payment_list";
    }

    //order 리스트 페이징
    @GetMapping("/paypagination")
    public String payPagination(HttpServletRequest request, Model model, @AuthenticationPrincipal UserDetails user) {

        int limit = Integer.parseInt(request.getParameter("limit"));
        HashMap params = new HashMap();
        params.put("searchDtFr", request.getParameter("searchDtFr"));
        params.put("searchDtTo", request.getParameter("searchDtTo"));
        params.put("userId", user.getUsername());
        int totalCount = supplierService.getPayCount(params);
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

    //liveRfq 프리뷰페이지
    @GetMapping("/viewRfq")
    public String viewRfq(@RequestParam HashMap<String, String> params, Model model) throws ParseException {
        RfqVo rfqVo = supplierService.getViewRfq(params);

        model.addAttribute("rfqVo", rfqVo);

        return "supplier/supplier_rfq_preview";
    }

    @GetMapping("/getRfqDtl")
    @ResponseBody
    public List<RfqDetailVo> getRfqDtl(@RequestParam HashMap<String, String> params){
        return supplierService.getRfqDtl(params);
    }

    @GetMapping("/quotation")
    public String quotation(@RequestParam(value = "rfqQno") String rfqQno, Model model){

        model.addAttribute("rfqQno", rfqQno);

        return "supplier/supplier_rfq";
    }

    @GetMapping("/komalQuotation")
    public String komalQuotation(@RequestParam HashMap<String, String> params, Model model, @AuthenticationPrincipal UserDetails user) throws ParseException {
        RfqVo rfqVo = supplierService.getViewRfq(params);
        CompanyVo companyVo = memberService.disCompanyInfo(user.getUsername());

        model.addAttribute("rfqVo", rfqVo);
        model.addAttribute("companyVo", companyVo);

        return "supplier/supplier_quotation_01";
    }

    @GetMapping("/ownQuotation")
    public String ownQuotation(){
        return "supplier/supplier_quotation_02";
    }

    @GetMapping("/docDtl")
    public String disAllDocDtl(){
        return "supplier/supplier_docu_all";
    }


    /**
     * 주소검색 팝업 띄우기
     */
    @RequestMapping(value = "/jusoPopup")
    public String addrPopup(HttpServletRequest request, Model model){
        String inputYn = request.getParameter("inputYn");
        String roadFullAddr = request.getParameter("roadFullAddr");
        String roadAddrPart1 = request.getParameter("roadAddrPart1");
        String roadAddrPart2 = request.getParameter("roadAddrPart2");
        String engAddr = request.getParameter("engAddr");
        String jibunAddr = request.getParameter("jibunAddr");
        String zipNo = request.getParameter("zipNo");
        String addrDetail = request.getParameter("addrDetail");
        String admCd = request.getParameter("admCd");
        String rnMgtSn = request.getParameter("rnMgtSn");
        String bdMgtSn = request.getParameter("bdMgtSn");
        String entX = request.getParameter("entX");
        String entY = request.getParameter("entY");
        String confmKey = "U01TX0FVVEgyMDIxMDcxNzAyNDU0NzExMTQyMzA=";
        model.addAttribute("confmKey", confmKey);
        model.addAttribute("inputYn", inputYn);
        model.addAttribute("roadFullAddr", roadFullAddr);
        model.addAttribute("addrDetail", addrDetail);

        return "supplier/p_addr";
    }

    @PostMapping(value = "/sugQuo")
    @ResponseBody
    public void suggestQuo(@RequestParam HashMap<String, String> params, @AuthenticationPrincipal UserDetails user){
        params.put("userId",user.getUsername());

        supplierService.sugQuo(params);
    }

    @PostMapping(value = "/sugQuoDtl")
    @ResponseBody
    public void suggestQuoDtl(@RequestBody List<RfqDetailVo> params, @AuthenticationPrincipal UserDetails user){
        for(RfqDetailVo vo : params){
            vo.setUserId(user.getUsername());
            supplierService.sugQuoDtl(vo);
        }
    }

    @GetMapping(value = "/getPoDtl")
    @ResponseBody
    public List<RfqDetailVo> getPoDtl(@RequestParam HashMap<String, String > params){
        return supplierService.getPoDtl(params);
    }

    @PostMapping("/registAccount")
    @ResponseBody
    public void registAccount(@RequestParam HashMap<String, String> params, @AuthenticationPrincipal UserDetails user){
        params.put("userId", user.getUsername());

        supplierService.registAccount(params);
    }

    @PostMapping("/submitPo")
    @ResponseBody
    public void submitPo(@RequestParam HashMap<String, String> params, @AuthenticationPrincipal UserDetails user){
        params.put("userId", user.getUsername());

        supplierService.submitPo(params);
    }

    @PutMapping("/registPl")
    @ResponseBody
    public void registPl(@RequestParam HashMap<String, String> params, @AuthenticationPrincipal UserDetails user){
        params.put("userId", user.getUsername());

        supplierService.registPl(params);
    }

    @PutMapping("/registInvoice")
    @ResponseBody
    public void registInvoice(@RequestParam HashMap<String, String> params, @AuthenticationPrincipal UserDetails user){
        params.put("userId", user.getUsername());

        supplierService.registInvoice(params);
    }
}
