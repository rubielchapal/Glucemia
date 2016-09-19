package magnusdroid.com.glucup_2date.Model;

/**
 * Class to handle the JSON response from the server. Used to read the JSON array and get the data
 * from every item of the list
 */
public class Pacient {

    public String id;
    public String subject;

    public Pacient(String id, String subject) {
        this.id = id;
        this.subject = subject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return subject;
    }

    public void setName(String subject) {
        this.subject = subject;
    }
}
