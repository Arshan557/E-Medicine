package arshan.com.e_medicine.Models;

import android.graphics.Bitmap;

/**
 * Created by Arshan on 29-May-2017.
 */
public class DistributorPojo {
    private String name;
    private String active;
    private Bitmap pic;

    public DistributorPojo(String name, Bitmap pic, String active) {
        this.name = name;
        this.pic = pic;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

}
