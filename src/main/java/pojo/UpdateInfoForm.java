package pojo;

public class UpdateInfoForm {

    private String email;
    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorizationBearer() {
        return authorizationBearer;
    }

    public void setAuthorizationBearer(String authorizationBearer) {
        this.authorizationBearer = authorizationBearer;
    }

    private String authorizationBearer;

    public UpdateInfoForm( String email, String name) {
        this.email = email;
        this.name = name;
    }

}
