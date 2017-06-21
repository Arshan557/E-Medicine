package arshan.com.e_medicine.Models;

/**
 * Created by Arshan on 21-Jun-2017.
 */
public class PurchaseDistributorPojo {
    private String id, name;

    public PurchaseDistributorPojo() {

    }

    public PurchaseDistributorPojo(String id, String name) {
        this.id = id;
        this.name = name;
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
}
