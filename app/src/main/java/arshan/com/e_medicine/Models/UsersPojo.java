package arshan.com.e_medicine.Models;

import android.graphics.Bitmap;

/**
 * Created by Arshan on 22-Jun-2017.
 */
public class UsersPojo {
    String id, fname, lname, uname, password, gender, email, mobile, phone, usertype, apikey, addressId, profilePic, companyid, createdBy,
            createdOn, modifiedBy, modifiedOn, isActive;
    private byte[] imageByteArray;
    private Bitmap pic;

    public UsersPojo() {
    }

    public UsersPojo(String id, String fname, String lname, String uname, String password, String gender, String email, String mobile, String phone, String usertype, String apikey, String addressId,
                     String profilePic, String companyid, String createdBy, String createdOn, String modifiedBy, String modifiedOn, String isActive, byte[] imageByteArray) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.uname = uname;
        this.password = password;
        this.gender = gender;
        this.email = email;
        this.mobile = mobile;
        this.phone = phone;
        this.usertype = usertype;
        this.apikey = apikey;
        this.addressId = addressId;
        this.profilePic = profilePic;
        this.companyid = companyid;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.isActive = isActive;
        this.imageByteArray = imageByteArray;

    }

    public UsersPojo(String id, String fname, String lname, String uname, String password, String gender, String email, String mobile, String phone, String usertype, String apikey, String addressId,
                     String profilePic, String companyid, String createdBy, String createdOn, String modifiedBy, String modifiedOn, String isActive, Bitmap pic) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.uname = uname;
        this.password = password;
        this.gender = gender;
        this.email = email;
        this.mobile = mobile;
        this.phone = phone;
        this.usertype = usertype;
        this.apikey = apikey;
        this.addressId = addressId;
        this.profilePic = profilePic;
        this.companyid = companyid;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.isActive = isActive;
        this.pic = pic;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public byte[] getImageByteArray() {
        return imageByteArray;
    }

    public void setImageByteArray(byte[] imageByteArray) {
        this.imageByteArray = imageByteArray;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }
}
