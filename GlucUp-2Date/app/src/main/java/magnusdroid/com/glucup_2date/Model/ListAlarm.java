package magnusdroid.com.glucup_2date.Model;

/**
 * Created by Dell on 24/07/2016.
 */
public class ListAlarm {

    private String date;
    private String title;
    private int id;

    public ListAlarm(String date, String title, int id){
        this.date = date;
        this.title = title;
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
