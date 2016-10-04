package magnusdroid.com.glucup_2date.Controler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import magnusdroid.com.glucup_2date.Model.PrefManager;
import magnusdroid.com.glucup_2date.R;

/**
 * A fragment representing a dialog to logout session.
 */
public class LogOutFragment extends Fragment {


    private PrefManager prefManager;

    public LogOutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.logout_layout, container, false);

        prefManager = new PrefManager(getContext());

        Button okButton = (Button) view.findViewById(R.id.btn_yes);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.setClear();
                getActivity().finish();
            }
        });

        Button noButton = (Button) view.findViewById(R.id.btn_no);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PacientActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });


        return view;
    }


}
