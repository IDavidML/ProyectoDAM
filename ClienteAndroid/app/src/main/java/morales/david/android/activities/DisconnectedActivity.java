package morales.david.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import morales.david.android.R;

public class DisconnectedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disconnected);

        getSupportActionBar().hide();

    }

}