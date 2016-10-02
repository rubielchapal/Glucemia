package magnusdroid.com.glucup_2date.Controler;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import magnusdroid.com.glucup_2date.R;

/**
 * Fragment to show the user manual. Called in {@link PacientActivity}
 * Use open library <a href="https://github.com/barteksc/AndroidPdfViewer">AndroidPdfViewer</a> to build the pdf viewer
 */
public class HelpFragment extends Fragment implements View.OnClickListener {

    PDFView pdfView;
    private TextView txtManualapp,txtManualweb;
    private ImageView imgManualapp, imgManualweb;

    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        txtManualapp = (TextView) view.findViewById(R.id.txtManualapp);
        txtManualweb = (TextView) view.findViewById(R.id.txtManualweb);
        imgManualapp = (ImageView) view.findViewById(R.id.imgManualapp);
        imgManualweb = (ImageView) view.findViewById(R.id.imgManualweb);
        txtManualapp.setOnClickListener(this);
        txtManualweb.setOnClickListener(this);
        imgManualapp.setOnClickListener(this);
        imgManualweb.setOnClickListener(this);
        String assetFileName = "test.pdf"; //The name of the asset to open
        int pageNumber = 0; //Start at the first page

        /*pdfView = (PDFView) view.findViewById(R.id.pdfView); //Fetch the view
        pdfView.fromAsset(assetFileName)
                .defaultPage(pageNumber)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(getContext()))
                .load();*/

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        int id = v.getId();
        if (id == R.id.txtManualapp || id == R.id.imgManualapp) {
            intent = new Intent(getActivity(), ManualActivity.class);
            intent.putExtra("manual",0);
            startActivity(intent);
        } else if (id == R.id.txtManualweb || id == R.id.imgManualweb) {
            intent = new Intent(getActivity(), ManualActivity.class);
            intent.putExtra("manual",1);
            startActivity(intent);
        }
    }
}
