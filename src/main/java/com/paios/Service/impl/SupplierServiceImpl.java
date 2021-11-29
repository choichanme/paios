package com.paios.Service.impl;

import com.paios.Mapper.SupplierMapper;
import com.paios.Service.SupplierService;
import com.paios.Vo.BuyerPoVo;
import com.paios.Vo.RfqDetailVo;
import com.paios.Vo.RfqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    SupplierMapper supplierMapper;

    @Override
    public List<RfqVo> getMainRfq() throws ParseException {
        List<RfqVo> rfqVoList = supplierMapper.getMainRfq();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        String today = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        for (RfqVo vo : rfqVoList){
            Date date = new Date(dateFormat.parse(vo.getDeadline()).getTime());
            Date now = new Date(dateFormat.parse(today).getTime());

            long calculate = date.getTime() - now.getTime();

            int Dday = (int) (calculate/( 24*60*60*1000));

            vo.setDDay(Integer.toString(Dday));
        }


        return rfqVoList;
    }

    @Override
    public int getRfqCount() {
        return supplierMapper.getRfqCount();
    }

    @Override
    public int getQuoCount(HashMap<String, String> params) {
        return supplierMapper.getQuoCount(params);
    }

    @Override
    public List<RfqVo> getRfq(HashMap<String, String> params) throws ParseException {
        List<RfqVo> rfqVoList = supplierMapper.getRfq(params);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        String today = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        for (RfqVo vo : rfqVoList){
            Date date = new Date(dateFormat.parse(vo.getDeadline()).getTime());
            Date now = new Date(dateFormat.parse(today).getTime());

            long calculate = date.getTime() - now.getTime();

            int Dday = (int) (calculate/( 24*60*60*1000));

            vo.setDDay(Integer.toString(Dday));
        }


        return rfqVoList;
    }

    @Override
    public List<RfqVo> getQuo(HashMap<String, String> params) {
        return supplierMapper.getQuo(params);
    }

    @Override
    public int getOrderCount(HashMap<String, String> params) {
        return supplierMapper.getOrderCount(params);
    }

    @Override
    public List<RfqVo> getOrder(HashMap<String, String> params) {
        return supplierMapper.getOrder(params);
    }

    @Override
    public int getPayCount(HashMap<String, String> params) {
        return supplierMapper.getPayCount(params);
    }

    @Override
    public List<BuyerPoVo> getPay(HashMap<String, String> params) {
        return supplierMapper.getPay(params);
    }

    @Override
    public RfqVo getViewRfq(HashMap<String, String> params) throws ParseException {

        RfqVo rfqVo = supplierMapper.viewRfq(params);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        String today = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date(dateFormat.parse(rfqVo.getShipmentDt()).getTime());
        Date now = new Date(dateFormat.parse(today).getTime());

        long calculate = date.getTime() - now.getTime();

        int Dday = (int) (calculate/( 24*60*60*1000));

        rfqVo.setDDay(Integer.toString(Dday));

        return rfqVo;
    }

    @Override
    public List<RfqDetailVo> getRfqDtl(HashMap<String, String> params) {
        return supplierMapper.getRfqDtl(params);
    }

    @Override
    public void sugQuo(HashMap<String, String> params) {
        supplierMapper.updateQuoYn(params);
        supplierMapper.sugQuo(params);
    }

    @Override
    public void sugQuoDtl(RfqDetailVo vo) {
        supplierMapper.sugQuoDtl(vo);
    }

    @Override
    public BuyerPoVo getKomalPo(String rfqQno) {
        return supplierMapper.getKomalPo(rfqQno);
    }

    @Override
    public List<RfqDetailVo> getPoDtl(HashMap<String, String> params) {
        return supplierMapper.getPoDtl(params);
    }

    @Override
    public void registAccount(HashMap<String, String> params) {
        supplierMapper.registAccount(params);
    }

    @Override
    public void submitPo(HashMap<String, String> params) {
        supplierMapper.submitPo(params);
    }

    @Override
    public void registPl(HashMap<String, String> params) {
        supplierMapper.registPl(params);
    }

    @Override
    public void registInvoice(HashMap<String, String> params) {
        supplierMapper.registInvoice(params);
    }
}
