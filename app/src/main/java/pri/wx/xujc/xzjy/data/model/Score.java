package pri.wx.xujc.xzjy.data.model;

public class Score {

    private String xjId;
    private String tmId;
    private String kcMc;
    private int kcXf;
    private int zcj;
    private String zcjDj;
    private String xkfs;
    private String ksqkQz;
    private String ksqkQm;

    public String getXjId() {
        return xjId;
    }

    public void setXjId(String xjId) {
        this.xjId = xjId;
    }

    public String getTmId() {
        return tmId;
    }

    public void setTmId(String tmId) {
        this.tmId = tmId;
    }

    public String getKcMc() {
        return kcMc;
    }

    public void setKcMc(String kcMc) {
        this.kcMc = kcMc;
    }

    public int getKcXf() {
        return kcXf;
    }

    public void setKcXf(int kcXf) {
        this.kcXf = kcXf;
    }

    public int getZcj() {
        return zcj;
    }

    public void setZcj(int zcj) {
        this.zcj = zcj;
    }

    public String getZcjDj() {
        return zcjDj;
    }

    public void setZcjDj(String zcjDj) {
        this.zcjDj = zcjDj;
    }

    public String getXkfs() {
        return xkfs;
    }

    public void setXkfs(String xkfs) {
        this.xkfs = xkfs;
    }

    public String getKsqkQz() {
        return ksqkQz;
    }

    public void setKsqkQz(String ksqkQz) {
        this.ksqkQz = ksqkQz;
    }

    public String getKsqkQm() {
        return ksqkQm;
    }

    public void setKsqkQm(String ksqkQm) {
        this.ksqkQm = ksqkQm;
    }


    @Override
    public String toString() {
        return getKcXf() + getKcMc() + getZcj();
    }
}
