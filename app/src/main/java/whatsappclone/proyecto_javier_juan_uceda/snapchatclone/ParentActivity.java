package whatsappclone.proyecto_javier_juan_uceda.snapchatclone;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

public abstract class ParentActivity extends AppCompatActivity {
    private Intent getIntent(Context screenFrom, Class<?> screenDestiny){
        Intent intent = new Intent(screenFrom, screenDestiny);
        return intent;
    }

    private void GoToScreen(Context screenFrom, Class<?> screenDestiny){
        startActivity(getIntent(screenFrom, screenDestiny));
        finish();
    }

    private void GoToScreen(Intent intent){
        startActivity(intent);
        finish();
    }

    public void GoToScreen(Class<?> screenDestiny){
        GoToScreen(this, screenDestiny);
    }

    public void GoToScreenFlags(Class<?> screenDestiny, int... flags){
        Intent intent = getIntent(getApplicationContext(), screenDestiny);
        for (int flag : flags) {
            intent.addFlags(flag);
        }
        GoToScreen(intent);
    }

    public void makeToast(CharSequence text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    public void makeToast(@StringRes int id){
        makeToast(getString(id));
    }
}
