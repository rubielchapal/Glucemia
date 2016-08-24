package magnusdroid.com.glucup_2date.Controller;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dell on 12/06/2016.
 */
public class PrefManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "glucup2date";
    // Preference first launch
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    // Preference ip server
    private static final String IP_SERVER = "IpServer";
    // Preference login
    private static  final String USER = "mUser";
    private static  final String DOC = "mDoc";
    private static  final String PASSWORD = "mPassword";
    private static  final String ROL = "mRol";
    // Preference alarm
    private static  final String ALARM_NAME = "mAlarm";
    private static  final String ALARM_DATE = "mDate";
    // Preference pacient fragment
    private static  final String FRAGMENT = "mFragment";


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setClear(){
        editor.remove(USER);
        editor.remove(DOC);
        editor.remove(PASSWORD);
        editor.remove(ROL);
        editor.remove(IP_SERVER);
        editor.commit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public void setIpServer(String ipServer){
        editor.putString(IP_SERVER, ipServer);
        editor.commit();
    }

    public void setUser(String user){
        editor.putString(USER, user);
        editor.commit();
    }

    public void setPassword(String password){
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    public void setDoc(String doc){
        editor.putString(DOC, doc);
        editor.commit();
    }

    public void setRol(String rol){
        editor.putString(ROL, rol);
        editor.commit();
    }

    public void setAlarmName(String alarmName){
        editor.putString(ALARM_NAME, alarmName);
        editor.commit();
    }

    public void setAlarmDate(String alarmDate){
        editor.putString(ALARM_DATE, alarmDate);
        editor.commit();
    }

    public void setFragment(int fragment){
        editor.putInt(FRAGMENT, fragment);
        editor.commit();
    }

    public String getRol(){
        return pref.getString(ROL, "");
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public String IpServer(){
        return pref.getString(IP_SERVER, "");
    }

    public String getUser(){
        return pref.getString(USER, "");
    }

    public String getPassword(){
        return pref.getString(PASSWORD, "");
    }

    public String getDoc(){
        return pref.getString(DOC, "");
    }

    public String getAlarmName(){
        return pref.getString(ALARM_NAME, "");
    }

    public String getAlarmDate(){
        return pref.getString(ALARM_DATE, "");
    }

    public int getFragment(){
        return pref.getInt(FRAGMENT, 0);
    }


}
