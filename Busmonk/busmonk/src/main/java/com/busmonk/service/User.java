package com.busmonk.service;

/**
 * @author Siddharth Patnaik
 *
 */
public class User {

    private String id;
    private String name;
    private String email;
    private String password;
    private String gender;
    private long mobile;
    private String companyId;
    private String roles;
    private String homeAddress;
    private String homeAddressCoordinates;
    private String officeAddress;
    private String officeAddressCoordinates;
    private int otp;
    private RegistrationStatus registrationStatus;


    public User()
    {

    }

    public User(String id, String name, String email, String password, String gender, long mobile, String companyId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.mobile = mobile;
        this.companyId = companyId;

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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public long getMobile() {
        return mobile;
    }
    public void setMobile(long mobile) {
        this.mobile = mobile;
    }
    public String getCompanyId() {
        return companyId;
    }
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
    public String getRoles() {
        return roles;
    }
    public void setRoles(String roles) {
        this.roles = roles;
    }
    public String getHomeAddress() {
        return homeAddress;
    }
    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }
    public String getHomeAddressCoordinates() {
        return homeAddressCoordinates;
    }
    public void setHomeAddressCoordinates(String homeAddressCoordinates) {
        this.homeAddressCoordinates = homeAddressCoordinates;
    }
    public String getOfficeAddress() {
        return officeAddress;
    }
    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }
    public String getOfficeAddressCoordinates() {
        return officeAddressCoordinates;
    }
    public void setOfficeAddressCoordinates(String officeAddressCoordinates) {
        this.officeAddressCoordinates = officeAddressCoordinates;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }
}

