package magnusdroid.com.glucup_2date.Model;

/**
 * Class to handle the JSON response from the server. Used to read the JSON array and get the data
 * from every item of the list
 */
public class ListGluc {

    private String performer;
    private String issued;
    private String value;
    private String unit;
    private String code;
    private String state;

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
