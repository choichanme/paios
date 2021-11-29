package com.paios.Vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RfqDetailVo {
    private String rfqQno;
    private String index;
    private String itemName;
    private String description;
    private String qty;
    private String uprice;
    private Integer amount;
    private Integer extendedPrice;
    private String remark;
    private String userId;
}
