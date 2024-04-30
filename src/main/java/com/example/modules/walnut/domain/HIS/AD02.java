package com.example.modules.walnut.domain.HIS;

/**
 * @author Boomp
 * @date 2019/11/18 10:19
 * TODO:  确费 取消收费（住院）
 */
public class AD02 {
    private String Column1;
    private String Column2;
    private String qfbz;
    private String qrbz;
    private String sfxh;
    private String msg;


    public String getQrbz() {
        return qrbz;
    }

    public void setQrbz(String qrbz) {
        this.qrbz = qrbz;
    }

    public String getColumn1() {
        return Column1;
    }

    public void setColumn1(String column1) {
        Column1 = column1;
    }

    public String getColumn2() {
        return Column2;
    }

    public void setColumn2(String column2) {
        Column2 = column2;
    }

    public String getQfbz() {
        return qfbz;
    }

    public void setQfbz(String qfbz) {
        this.qfbz = qfbz;
    }

    public String getSfxh() {
        return sfxh;
    }

    public void setSfxh(String sfxh) {
        this.sfxh = sfxh;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "AD02{" +
                "qfbz='" + qfbz + '\'' +
                ", sfxh='" + sfxh + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
