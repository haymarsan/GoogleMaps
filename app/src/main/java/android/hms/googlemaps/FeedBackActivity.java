package android.hms.googlemaps;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class FeedBackActivity extends AppCompatActivity {

    Button btnSubmit;
    Button btnReset;
    EditText txtName;
    EditText txtEmail;
    EditText txtComment;
    RatingBar ratingBar;
    TextView txtRatingValue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

       // Toast.makeText(this,"Sending Feedback to CallCenter", Toast.LENGTH_LONG).show();

        btnSubmit = findViewById(R.id.btnSubmit);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtComment = findViewById(R.id.txtComment);
        txtRatingValue = findViewById(R.id.rateMe);
        ratingBar = findViewById(R.id.rating);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getApplicationContext(),"Your Comment have been sent", Toast.LENGTH_LONG).show();

                txtName.setText(" ");
                txtEmail.setText(" ");
                txtComment.setText(" ");
            }
        });


        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtName.setText(" ");
                txtEmail.setText(" ");
                txtComment.setText(" ");
                ratingBar.setRating(0);

            }
        });

        addListenerOnRatingBar();
//        ratingBar = findViewById(R.id.rating);
//        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//
//                txtRatingValue.setText(String.valueOf(rating));
//
//            }
//        });

    }

    public void addListenerOnRatingBar() {

        ratingBar = findViewById(R.id.rating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                txtRatingValue.setText(String.valueOf(rating));
                Toast.makeText(FeedBackActivity.this,
                        String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}
