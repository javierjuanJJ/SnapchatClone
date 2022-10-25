package whatsappclone.proyecto_javier_juan_uceda.snapchatclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends ParentActivity {

    public static Boolean started = false;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            GoToScreenFlags(MainActivity.class,Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else{
            GoToScreenFlags(ChooseLoginRegistrationActivity.class,Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
    }
}