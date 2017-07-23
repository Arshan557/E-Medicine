package arshan.com.e_medicine.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arshan on 19-Jun-2017.
 */
public class PurchasesPojo {
    String DistributorName, id, companyid, BillDate, InvoiceNumber, DistributorId, Amount, PaymentDate, PaymentMode, ChequeNumber, BankName,
            createdBy, createdOn, modifiedBy, modifiedOn, isSettled;

    public PurchasesPojo() {
    }

    public PurchasesPojo(String DistributorName, String id, String companyid, String BillDate, String InvoiceNumber, String DistributorId
            , String Amount, String PaymentDate, String PaymentMode, String ChequeNumber, String BankName,
                         String createdBy, String createdOn, String modifiedBy, String modifiedOn, String isSettled) {
        this.DistributorName = DistributorName;
        this.id = id;
        this.companyid = companyid;
        this.BillDate = BillDate;
        this.InvoiceNumber = InvoiceNumber;
        this.DistributorId = DistributorId;
        this.Amount = Amount;
        this.PaymentDate = PaymentDate;
        this.PaymentMode = PaymentMode;
        this.ChequeNumber = ChequeNumber;
        this.BankName = BankName;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.isSettled = isSettled;

    }

    public String getDistributorName() {
        return DistributorName;
    }

    public void setDistributorName(String distributorName) {
        DistributorName = distributorName;
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

    public String getBillDate() {
        return BillDate;
    }

    public void setBillDate(String billDate) {
        BillDate = billDate;
    }

    public String getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        InvoiceNumber = invoiceNumber;
    }

    public String getDistributorId() {
        return DistributorId;
    }

    public void setDistributorId(String distributorId) {
        DistributorId = distributorId;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getChequeNumber() {
        return ChequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        ChequeNumber = chequeNumber;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
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

    public String getIsSettled() {
        return isSettled;
    }

    public void setIsSettled(String isSettled) {
        this.isSettled = isSettled;
    }
}
