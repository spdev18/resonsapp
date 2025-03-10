package models;



import org.json.JSONException;
import org.json.JSONObject;

public class RegisterModel {
    private String name;
    private String email;
    private String password;
    private String accountType;

    public RegisterModel(String name, String email, String password, String accountType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            json.put("email", email);
            json.put("password", password);
            json.put("accont_type", accountType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}

