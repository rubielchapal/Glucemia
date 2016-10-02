package magnusdroid.com.glucup_2date.Controler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import magnusdroid.com.glucup_2date.R;

public class ManualActivity extends AppCompatActivity {

    PDFView pdfView;
    String assetFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        // Set up toolbar and action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.manual_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pdfView = (PDFView) findViewById(R.id.pdfView);//Fetch the view
        int pageNumber = 0; //Start at the first page

        //The name of the asset to open
        String mApp = "Manual_Usuario_GlucUp2Date.pdf";
        String mWeb = "Manual_Usuario_Web.pdf";

        int flag = getIntent().getIntExtra("manual",0);

        if(flag == 0){
            assetFileName = mApp;
        }else{
            assetFileName = mWeb;
        }
        pdfView.fromAsset(assetFileName)
                .defaultPage(pageNumber)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(getApplicationContext()))
                .load();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
