package id.xyzprjkt.pintarin.infotechAPI.model;

public class User {
    private boolean loggedWithiLab;
    private String id;
    private String user_name;
    private String full_name;
    private String email;
    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getLoggedWithiLab() {
        return loggedWithiLab;
    }

    public void setLoggedWithiLab(boolean loggedWithiLab) {
        this.loggedWithiLab = loggedWithiLab;
    }
}
