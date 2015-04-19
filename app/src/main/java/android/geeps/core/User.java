package android.geeps.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Túlio on 19/04/2015.
 */
public class User {
    private Long phone;
    private String countryCode;
    private String name;
    private List<Order> orders;

    public User(Long userPhone, String userCountryCode, String userName){
        this.phone = userPhone;
        this.countryCode = userCountryCode;
        this.name = userName;
        this.orders = new ArrayList<Order>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public boolean registeredUser(){
        //TODO fazer um request no servidor e ver se ele já cadastrado;
        return false;
    }

    public void registerUser(){
        //TODO efetivar o cadastro no servidor.
    }

}
