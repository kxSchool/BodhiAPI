package com.example.modules.walnut.domain.HIS;

/**
 * @author Boomp
 * @date 2019/11/18 10:52
 * TODO:
 */
public class BiographyQF01 {
    private String type=""; //1 门诊确费 4 门诊取消收费
    private String blh="";//门诊号
    private String brlb="";//病人类别
    private String xmlb="";//0临床项目1收费项目(JB03- itemtype)
    private String patid="";//病人唯一码
    private String syxh="";//首页序号
    private String zxksdm="";//确认科室代码(需要接口传入是哪个科室进行确费的)
    private String zxysdm="";//确认人员代码(需要接口传入是哪个操作员进行确费的)
    private String qqxh="";//申请序号（无默认为0）
    private String qqmxxh="";//申请序号（明细序号）[由于这个序号在确费时要求传入，在返回中已经增加了这个字段的输出]
    private String itemcode="";//项目代码 AD02 xmdm
    private String itemname="";//项目名称
    private String price="";//项目单价  AD02 xmdj
    private double itemqty=0;//项目数量  AD02 xmsl
    private String xmstatus="";//项目状态：0不处理1确认2拒绝3撤销(增加撤销处理)
    private String sfflag="";//收费状态：0不收费1收费2退费
    private String djlb="";//单价类别(0:使用原来的单价1：自定义价格)
    private String bgdh="";//报告单号
    private String bglx="";//报告类型
    private String tssm="";//自定义单价时需要给出解释，否则确费不成功 如果需要使用自定义单价则djlb需要设置为1  同时给出说明his会纪录到对应的项目信息的备注上面
    private String tjrybh="";//体检人员编号

    private String curno;//住院syxh（JB01返回的CureNo）
    private String qqksdm;//申请增加项目的科室代码
    private String qqysdm;//申请增加项目医生代码
    private String fylb;//费用类别（默认传12）
    private String sfbz;//收费标志(0:收费;1:退费）
    private String tfxh;//退费序号(收费时传0;退费时传入收费序号)

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBlh() {
        return blh;
    }

    public void setBlh(String blh) {
        this.blh = blh;
    }

    public String getBrlb() {
        return brlb;
    }

    public void setBrlb(String brlb) {
        this.brlb = brlb;
    }

    public String getXmlb() {
        return xmlb;
    }

    public void setXmlb(String xmlb) {
        this.xmlb = xmlb;
    }

    public String getPatid() {
        return patid;
    }

    public void setPatid(String patid) {
        this.patid = patid;
    }

    public String getSyxh() {
        return syxh;
    }

    public void setSyxh(String syxh) {
        this.syxh = syxh;
    }

    public String getZxksdm() {
        return zxksdm;
    }

    public void setZxksdm(String zxksdm) {
        this.zxksdm = zxksdm;
    }

    public String getZxysdm() {
        return zxysdm;
    }

    public void setZxysdm(String zxysdm) {
        this.zxysdm = zxysdm;
    }

    public String getQqxh() {
        return qqxh;
    }

    public void setQqxh(String qqxh) {
        this.qqxh = qqxh;
    }

    public String getQqmxxh() {
        return qqmxxh;
    }

    public void setQqmxxh(String qqmxxh) {
        this.qqmxxh = qqmxxh;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public double getItemqty() {
        return itemqty;
    }

    public void setItemqty(double itemqty) {
        this.itemqty = itemqty;
    }

    public String getXmstatus() {
        return xmstatus;
    }

    public void setXmstatus(String xmstatus) {
        this.xmstatus = xmstatus;
    }

    public String getSfflag() {
        return sfflag;
    }

    public void setSfflag(String sfflag) {
        this.sfflag = sfflag;
    }

    public String getDjlb() {
        return djlb;
    }

    public void setDjlb(String djlb) {
        this.djlb = djlb;
    }

    public String getBgdh() {
        return bgdh;
    }

    public void setBgdh(String bgdh) {
        this.bgdh = bgdh;
    }

    public String getBglx() {
        return bglx;
    }

    public void setBglx(String bglx) {
        this.bglx = bglx;
    }

    public String getTssm() {
        return tssm;
    }

    public void setTssm(String tssm) {
        this.tssm = tssm;
    }

    public String getTjrybh() {
        return tjrybh;
    }

    public void setTjrybh(String tjrybh) {
        this.tjrybh = tjrybh;
    }

    public String getCurno() {
        return curno;
    }

    public void setCurno(String curno) {
        this.curno = curno;
    }

    public String getQqksdm() {
        return qqksdm;
    }

    public void setQqksdm(String qqksdm) {
        this.qqksdm = qqksdm;
    }

    public String getQqysdm() {
        return qqysdm;
    }

    public void setQqysdm(String qqysdm) {
        this.qqysdm = qqysdm;
    }

    public String getFylb() {
        return fylb;
    }

    public void setFylb(String fylb) {
        this.fylb = fylb;
    }

    public String getSfbz() {
        return sfbz;
    }

    public void setSfbz(String sfbz) {
        this.sfbz = sfbz;
    }

    public String getTfxh() {
        return tfxh;
    }

    public void setTfxh(String tfxh) {
        this.tfxh = tfxh;
    }
}
