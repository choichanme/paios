package com.paios.Controller;

import com.paios.Service.SupplierService;
import com.paios.Vo.RfqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/forward")
public class ForwardController {

    @Autowired
    SupplierService supplierService;

    @GetMapping("/main")
    public String mainForward(Model model) throws ParseException {
        List<RfqVo> rfqVoList = supplierService.getMainRfq();

        model.addAttribute("rfqVo", rfqVoList);

        return "/forward/main_forwarding";
    }

    @GetMapping("/liveRfq")
    public String liveRfq() {
        return "forward/forwarding_live";
    }
}
