package whatsappclone.proyecto_javier_juan_uceda.snapchatclone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.FileNotFoundException;

import whatsappclone.proyecto_javier_juan_uceda.snapchatclone.Constants.OpenFileInput;

public class ShowCaptureActivity extends ParentActivity {

    private ImageView mImage;
    private Bitmap bitmap;
    private String Uid;
    private Button mSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_capture);
        setUI();
    }

    private void setUI() {
        try {
            mImage = findViewById(R.id.imageCaptured);
            mSend = findViewById(R.id.send);
            mSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GoToScreen(ChooseReceiverActivity.class);
                }
            });
            firebaseLoad();

            loadimage();

            mImage.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            finish();
        }
    }

    private void firebaseLoad() {
        Uid = FirebaseAuth.getInstance().getUid();
    }

    private void loadimage() throws FileNotFoundException {
        bitmap = BitmapFactory.decodeStream(getApplication().openFileInput(OpenFileInput.IMAGE_TO_SEND));

    }
}