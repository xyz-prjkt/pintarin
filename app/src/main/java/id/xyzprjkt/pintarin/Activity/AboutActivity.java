package id.xyzprjkt.pintarin.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import id.xyzprjkt.pintarin.R;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        // Our Team Intent
        CardView xyzuan = findViewById(R.id.about_xyzuan);
        CardView zara = findViewById(R.id.about_zara);
        CardView rosydan = findViewById(R.id.about_rosydan);

        xyzuan.setOnClickListener(v-> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/jodyyuan"))));
        zara.setOnClickListener(v-> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/kiarazzahraa"))));
        rosydan.setOnClickListener(v-> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/ay.dannnnn"))));

        // Suport Intent
        CardView umm = findViewById(R.id.umm_logo);
        CardView labit = findViewById(R.id.labit_logo);

        umm.setOnClickListener(v-> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/ummcampus"))));
        labit.setOnClickListener(v-> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/labit.umm"))));

    }
}
