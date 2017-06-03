package arshan.com.e_medicine.Models;

import android.graphics.Bitmap;

/**
 * Created by Arshan on 29-May-2017.
 */
public class DistributorPojo {
    private String name, active, id, companyid, email, uname, password, mobile,
            phone, createdBy, modifiedBy, createdOn, modifiedOn, picUrl;
    private Bitmap pic;

    public DistributorPojo(String name, Bitmap pic, String active, String id, String companyid, String email, String uname,
                           String password, String mobile, String phone, String createdBy, String modifiedBy, String createdOn, String modifiedOn, String picUrl) {
        this.name = name;
        this.pic = pic;
        this.id = id;
        this.companyid = companyid;
        this.email = email;
        this.uname = uname;
        this.password = password;
        this.mobile = mobile;
        this.phone = phone;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.active = active;
        this.picUrl = picUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getName() {
        return name;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
