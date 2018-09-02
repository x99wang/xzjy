package pri.wx.xujc.xzjy.data.model;

public class User {
    private String id;
    private String name;
    private String password;
    private String token;

    public User() {
        this("","");
    }

    public User(String id, String password) {
        this(id, "", password, "");
    }

    public User(String id, String name, String password, String token) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
