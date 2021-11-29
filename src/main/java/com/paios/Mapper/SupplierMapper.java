package com.paios.Mapper;

import com.paios.Vo.BuyerPoVo;
import com.paios.Vo.RfqDetailVo;
import com.paios.Vo.RfqVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface SupplierMapper {
    public List<RfqVo> getMainRfq();

    public int getRfqCount();

    public int getQuoCount(HashMap<String, String > params);

    public List<RfqVo> getRfq(HashMap<String, String> params);

    public int getOrderCount(HashMap<String, String > params);

    public List<RfqVo> getOrder(HashMap<String, String> params);

    public List<RfqVo> getQuo(HashMap<String, String> params);

    public int getPayCount(HashMap<String, String > params);

    public List<BuyerPoVo> getPay(HashMap<String, String> params);

    public RfqVo viewRfq(HashMap<String,String> params);

    public List<RfqDetailVo> getRfqDtl(HashMap<String, String> params);

    public void sugQuo(HashMap<String, String> params);

    public void sugQuoDtl(RfqDetailVo vo);

    public void updateQuoYn(HashMap<String, String> params);

    public BuyerPoVo getKomalPo(String rfqQno);

    public List<RfqDetailVo> getPoDtl(HashMap<String, String> params);

    public void registAccount(HashMap<String, String> params);

    public void submitPo(HashMap<String, String> params);

    public void registPl(HashMap<String, String> params);

    public void registInvoice(HashMap<String, String> params);
}
