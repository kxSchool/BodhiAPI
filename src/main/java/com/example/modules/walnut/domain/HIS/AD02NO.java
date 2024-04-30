package com.example.modules.walnut.domain.HIS;

import java.io.Serializable;

/**
 * @author Boomp
 * @date 2019/11/28 11:26
 * TODO: 收费退费时保存的数据
 */
public class AD02NO implements Serializable {
    private String sfxh;
    private String tfxh;
    private String state;
    private String qrbz;
    private String qfbz;

    public String getQrbz() {
        return qrbz;
    }

    public void setQrbz(String qrbz) {
        this.qrbz = qrbz;
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

    public String getTfxh() {
        return tfxh;
    }

    public void setTfxh(String tfxh) {
        this.tfxh = tfxh;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "AD02NO{" +
                "sfxh='" + sfxh + '\'' +
                ", tfxh='" + tfxh + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
