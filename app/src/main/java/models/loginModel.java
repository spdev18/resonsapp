package models;

import org.json.JSONException;
import org.json.JSONObject;

public class loginModel {

    private String email;
    private String password;

    public loginModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

}
