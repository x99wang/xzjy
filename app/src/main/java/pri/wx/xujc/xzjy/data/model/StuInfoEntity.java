package pri.wx.xujc.xzjy.data.model;

public class StuInfoEntity {
    private String xjId;
    private String xjXm;
    private String xjXb;
    private int xjNj;
    private String zyMc;
    private String ssyq;
    private String tel;
    private String wechatOpenid;
    private String qqOpenid;
    private String weiboId;
    private String other;

    public String getXjId() {
        return xjId;
    }

    public void setXjId(String xjId) {
        this.xjId = xjId;
    }

    public String getXjXm() {
        return xjXm;
    }

    public void setXjXm(String xjXm) {
        this.xjXm = xjXm;
    }

    public String getXjXb() {
        return xjXb;
    }

    public void setXjXb(String xjXb) {
        this.xjXb = xjXb;
    }

    public int getXjNj() {
        return xjNj;
    }

    public void setXjNj(int xjNj) {
        this.xjNj = xjNj;
    }

    public String getZyMc() {
        return zyMc;
    }

    public void setZyMc(String zyMc) {
        this.zyMc = zyMc;
    }

    public String getSsyq() {
        return ssyq;
    }

    public void setSsyq(String ssyq) {
        this.ssyq = ssyq;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getWechatOpenid() {
        return wechatOpenid;
    }

    public void setWechatOpenid(String wechatOpenid) {
        this.wechatOpenid = wechatOpenid;
    }

    public String getQqOpenid() {
        return qqOpenid;
    }

    public void setQqOpenid(String qqOpenid) {
        this.qqOpenid = qqOpenid;
    }

    public String getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(String weiboId) {
        this.weiboId = weiboId;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public User toUser() {
        return new User(xjId, xjXm, "", "");
    }
}