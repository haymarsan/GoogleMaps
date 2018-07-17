package android.hms.googlemaps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PhoneCallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_call);
      // Toast.makeText(this,"Making a phone call to CallCenter", Toast.LENGTH_LONG).show();

        Button btnCall = findViewById(R.id.btnPhoneNo1);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:01370200"));

                if (ActivityCompat.checkSelfPermission(PhoneCallActivity.this,
                        android.Manifest.permission.CALL_PHONE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return;

                }
                startActivity(intent);
            }
        });


        Button btnCall2 = findViewById(R.id.btnPhoneNo2);
        btnCall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_CALL);
                intent1.setData(Uri.parse("tel:01370320"));

                if (ActivityCompat.checkSelfPermission(PhoneCallActivity.this,
                        Manifest.permission.CALL_PHONE) !=
                        PackageManager.PERMISSION_GRANTED){
                    return;
                }
                startActivity(intent1);
            }
        });

    }

}
