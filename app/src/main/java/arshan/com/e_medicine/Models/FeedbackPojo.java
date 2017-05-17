package arshan.com.e_medicine.Models;

/**
 * Created by Arshan on 13-May-2017.
 */
public class FeedbackPojo {

    private String name;
    private String mail;
    private String comments;
    private String created;

    public FeedbackPojo(String name, String mail, String comments, String created) {
        this.name = name;
        this.mail = mail;
        this.comments = comments;
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
