package whatsappclone.proyecto_javier_juan_uceda.snapchatclone;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseLoginRegistrationActivity extends ParentActivity implements View.OnClickListener {


    private Button mLogin, mRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_registration);

        setUI();

    }

    private void setUI() {
        mLogin = findViewById(R.id.login);
        mRegistration = findViewById(R.id.registration);

        mLogin.setOnClickListener(this);
        mRegistration.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                GoToScreen(LoginActivity.class);
                break;
            case R.id.registration:
                GoToScreen(RegistrationActivity.class);
                break;
        }
    }
}