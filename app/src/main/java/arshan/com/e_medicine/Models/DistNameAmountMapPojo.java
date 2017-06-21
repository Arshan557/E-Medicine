package arshan.com.e_medicine.Models;

/**
 * Created by Arshan on 21-Jun-2017.
 */
public class DistNameAmountMapPojo {
    private String name, amount;

    public DistNameAmountMapPojo() {

    }

    public DistNameAmountMapPojo(String amount, String name) {
        this.amount = amount;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
