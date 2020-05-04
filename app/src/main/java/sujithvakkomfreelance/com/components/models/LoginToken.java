package sujithvakkomfreelance.com.components.models;

import sujithvakkomfreelance.com.components.localdata.dataproviders.UserDetail;

public class LoginToken extends UserDetail {
    public LoginToken(String userName, String fullName, String vehicleCode, String profile, String sign, String session) {
        super(userName, fullName, vehicleCode,profile,sign, session);
    }
}
