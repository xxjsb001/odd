package com.vtradex.wms.server.model.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.vtradex.thorn.server.model.DomainModel;
import com.vtradex.thorn.server.util.BeanUtils;

/**
 * 发货单明细批次属性
 *
 * @category 组件
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1 $Date: 2015/10/22 08:03:10 $
 */
public class ShipLotInfo extends DomainModel {
	
	private static final long serialVersionUID = -4831506144534547277L;
	
	 /**存货日期*/
    private Date storageDate;
	/** 收货单号*/
	private String soi;
	
	private String supplier;
	
	/** 扩展属性1-工艺状态(最新、新、老、最老、-) */
	private String extendPropC1;
	/** 扩展属性2*/
	private String extendPropC2;
	/** 扩展属性3*/
	private String extendPropC3;
	/** 扩展属性4*/
	private String extendPropC4;
	/** 扩展属性5*/
	private String extendPropC5;
	/** 扩展属性6*/
	private String extendPropC6;
	/** 扩展属性7*/
	private String extendPropC7;
	/** 扩展属性8*/
	private String extendPropC8;
	/** 扩展属性9*/
	private String extendPropC9;
	/** 扩展属性10 */
	private String extendPropC10;
	/** 扩展属性11 */
	private String extendPropC11;
	/** 扩展属性12*/
	private String extendPropC12;
	/** 扩展属性13*/
	private String extendPropC13;
	/** 扩展属性14*/
	private String extendPropC14;
	/** 扩展属性15*/
	private String extendPropC15;
	/** 扩展属性16*/
	private String extendPropC16;
	/** 扩展属性17*/
	private String extendPropC17;
	/** 扩展属性18*/
	private String extendPropC18;
	/** 扩展属性19*/
	private String extendPropC19;
	/** 扩展属性20 */
	private String extendPropC20;
	

	public Date getStorageDate() {
		return storageDate;
	}

	public void setStorageDate(Date storageDate) {
		this.storageDate = storageDate;
	}

	public String getExtendPropC1() {
		return extendPropC1;
	}

	public void setExtendPropC1(String extendPropC1) {
		this.extendPropC1 = extendPropC1;
	}

	public String getExtendPropC2() {
		return extendPropC2;
	}

	public void setExtendPropC2(String extendPropC2) {
		this.extendPropC2 = extendPropC2;
	}

	public String getExtendPropC3() {
		return extendPropC3;
	}

	public void setExtendPropC3(String extendPropC3) {
		this.extendPropC3 = extendPropC3;
	}

	public String getExtendPropC4() {
		return extendPropC4;
	}

	public void setExtendPropC4(String extendPropC4) {
		this.extendPropC4 = extendPropC4;
	}

	public String getExtendPropC5() {
		return extendPropC5;
	}

	public void setExtendPropC5(String extendPropC5) {
		this.extendPropC5 = extendPropC5;
	}

	public String getExtendPropC6() {
		return extendPropC6;
	}

	public void setExtendPropC6(String extendPropC6) {
		this.extendPropC6 = extendPropC6;
	}

	public String getExtendPropC7() {
		return extendPropC7;
	}

	public void setExtendPropC7(String extendPropC7) {
		this.extendPropC7 = extendPropC7;
	}

	public String getExtendPropC8() {
		return extendPropC8;
	}

	public void setExtendPropC8(String extendPropC8) {
		this.extendPropC8 = extendPropC8;
	}

	public String getExtendPropC9() {
		return extendPropC9;
	}

	public void setExtendPropC9(String extendPropC9) {
		this.extendPropC9 = extendPropC9;
	}

	public String getExtendPropC10() {
		return extendPropC10;
	}

	public void setExtendPropC10(String extendPropC10) {
		this.extendPropC10 = extendPropC10;
	}

	public String getExtendPropC11() {
		return extendPropC11;
	}

	public void setExtendPropC11(String extendPropC11) {
		this.extendPropC11 = extendPropC11;
	}

	public String getExtendPropC12() {
		return extendPropC12;
	}

	public void setExtendPropC12(String extendPropC12) {
		this.extendPropC12 = extendPropC12;
	}

	public String getExtendPropC13() {
		return extendPropC13;
	}

	public void setExtendPropC13(String extendPropC13) {
		this.extendPropC13 = extendPropC13;
	}

	public String getExtendPropC14() {
		return extendPropC14;
	}

	public void setExtendPropC14(String extendPropC14) {
		this.extendPropC14 = extendPropC14;
	}

	public String getExtendPropC15() {
		return extendPropC15;
	}

	public void setExtendPropC15(String extendPropC15) {
		this.extendPropC15 = extendPropC15;
	}

	public String getExtendPropC16() {
		return extendPropC16;
	}

	public void setExtendPropC16(String extendPropC16) {
		this.extendPropC16 = extendPropC16;
	}

	public String getExtendPropC17() {
		return extendPropC17;
	}

	public void setExtendPropC17(String extendPropC17) {
		this.extendPropC17 = extendPropC17;
	}

	public String getExtendPropC18() {
		return extendPropC18;
	}

	public void setExtendPropC18(String extendPropC18) {
		this.extendPropC18 = extendPropC18;
	}

	public String getExtendPropC19() {
		return extendPropC19;
	}

	public void setExtendPropC19(String extendPropC19) {
		this.extendPropC19 = extendPropC19;
	}

	public String getExtendPropC20() {
		return extendPropC20;
	}

	public void setExtendPropC20(String extendPropC20) {
		this.extendPropC20 = extendPropC20;
	}

	public String getSoi() {
		return soi;
	}

	public void setSoi(String soi) {
		this.soi = soi;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public ShipLotInfo(){

	}

	/**
	 * 批次属性列拼接
	 * @return
	 */
	public Object[] getArraysOfColumns() {
		Object[] objs = {this.soi,  this.supplier, 
				this.extendPropC1, this.extendPropC2, this.extendPropC3, this.extendPropC4, this.extendPropC5, 
				this.extendPropC6, this.extendPropC7, this.extendPropC8, this.extendPropC9, this.extendPropC10,
				this.extendPropC11, this.extendPropC12, this.extendPropC13, this.extendPropC14, this.extendPropC15, 
				this.extendPropC16, this.extendPropC17, this.extendPropC18, this.extendPropC19, this.extendPropC20};
		
		return objs;
	}
	
	/**
	 * 生成HashCode
	 * @param item
	 * @return
	 */
	public String genHashCode() {
		return BeanUtils.getFormat(this.getArraysOfColumns());
	}
	
	/**
	 * 将lotInfo转换为String
	 * @return
	 */
	public String stringValue() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String result = "";
		result += (StringUtils.isEmpty(this.soi) ? "" : "#" + this.soi);
		result += (StringUtils.isEmpty(this.supplier) ? "" : "#" + this.supplier);
		result += (StringUtils.isEmpty(this.extendPropC1) ? "" : "#" + this.extendPropC1);
		result += (StringUtils.isEmpty(this.extendPropC2) ? "" : "#" + this.extendPropC2);
		result += (StringUtils.isEmpty(this.extendPropC3) ? "" : "#" + this.extendPropC3);
		result += (StringUtils.isEmpty(this.extendPropC4) ? "" : "#" + this.extendPropC4);
		result += (StringUtils.isEmpty(this.extendPropC5) ? "" : "#" + this.extendPropC5);
		result += (StringUtils.isEmpty(this.extendPropC6) ? "" : "#" + this.extendPropC6);
		result += (StringUtils.isEmpty(this.extendPropC7) ? "" : "#" + this.extendPropC7);
		result += (StringUtils.isEmpty(this.extendPropC8) ? "" : "#" + this.extendPropC8);
		result += (StringUtils.isEmpty(this.extendPropC9) ? "" : "#" + this.extendPropC9);
		result += (StringUtils.isEmpty(this.extendPropC10) ? "" : "#" + this.extendPropC10);
		result += (StringUtils.isEmpty(this.extendPropC11) ? "" : "#" + this.extendPropC11);
		result += (StringUtils.isEmpty(this.extendPropC12) ? "" : "#" + this.extendPropC12);
		result += (StringUtils.isEmpty(this.extendPropC13) ? "" : "#" + this.extendPropC13);
		result += (StringUtils.isEmpty(this.extendPropC14) ? "" : "#" + this.extendPropC14);
		result += (StringUtils.isEmpty(this.extendPropC15) ? "" : "#" + this.extendPropC15);
		result += (StringUtils.isEmpty(this.extendPropC16) ? "" : "#" + this.extendPropC16);
		result += (StringUtils.isEmpty(this.extendPropC17) ? "" : "#" + this.extendPropC17);
		result += (StringUtils.isEmpty(this.extendPropC18) ? "" : "#" + this.extendPropC18);
		result += (StringUtils.isEmpty(this.extendPropC19) ? "" : "#" + this.extendPropC19);
		result += (StringUtils.isEmpty(this.extendPropC20) ? "" : "#" + this.extendPropC20);
		result = StringUtils.replaceOnce(result,"#","");
		return result;
	}
}