package arshan.com.e_medicine.Models;

/**
 * Created by Arshan on 10-Jun-2017.
 */
public class ProductsSQLite {
    String id, companyid, barcode, itemname, mfgdate, expdate, maxdiscount, qty, mrp, batch, productimage;
    private byte[] imageByteArray;

    public ProductsSQLite () {
    }

    public ProductsSQLite(String id, String companyid, String barcode, String itemname, String mfgdate
            , String expdate, String maxdiscount, String qty, String mrp, String batch, String productimage, byte[] imageByteArray) {
        this.id = id;
        this.companyid = companyid;
        this.barcode = barcode;
        this.itemname = itemname;
        this.mfgdate = mfgdate;
        this.expdate = expdate;
        this.maxdiscount = maxdiscount;
        this.qty = qty;
        this.mrp = mrp;
        this.batch = batch;
        this.productimage = productimage;
        this.imageByteArray = imageByteArray;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getMfgdate() {
        return mfgdate;
    }

    public void setMfgdate(String mfgdate) {
        this.mfgdate = mfgdate;
    }

    public String getExpdate() {
        return expdate;
    }

    public void setExpdate(String expdate) {
        this.expdate = expdate;
    }

    public String getMaxdiscount() {
        return maxdiscount;
    }

    public void setMaxdiscount(String maxdiscount) {
        this.maxdiscount = maxdiscount;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public byte[] getImageByteArray() {
        return imageByteArray;
    }

    public void setImageByteArray(byte[] imageByteArray) {
        this.imageByteArray = imageByteArray;
    }
}
