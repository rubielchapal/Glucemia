package magnusdroid.com.glucup_2date.Model;

/**
 * Created by Dell on 19/07/2016.
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
