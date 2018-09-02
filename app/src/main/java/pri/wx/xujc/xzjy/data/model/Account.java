package pri.wx.xujc.xzjy.data.model;

public class Account {
    private String xjId;
    private String xjPw;
    private String pePw;
    private String apikey;

    public Account(String xjid, String pw) {
        this(xjid, pw, "", "");
    }

    public String getXjId() {
        return xjId;
    }

    public void setXjId(String xjId) {
        this.xjId = xjId;
    }

    public String getXjPw() {
        return xjPw;
    }

    public void setXjPw(String xjPw) {
        this.xjPw = xjPw;
    }

    public String getPePw() {
        return pePw;
    }

    public void setPePw(String pePw) {
        this.pePw = pePw;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public Account(String xjId, String xjPw, String pePw, String apikey) {

        this.xjId = xjId;
        this.xjPw = xjPw;
        this.pePw = pePw;
        this.apikey = apikey;
    }

    public User toUser() {
        return new User(xjId,xjPw);
    }
}
