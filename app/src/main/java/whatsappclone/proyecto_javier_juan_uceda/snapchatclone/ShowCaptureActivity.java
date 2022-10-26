package whatsappclone.proyecto_javier_juan_uceda.snapchatclone;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;

public class ShowCaptureActivity extends AppCompatActivity {

    private ImageView image;
    private Bitmap rotateBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_capture);
        setUI();
    }

    private void setUI() {
        image = findViewById(R.id.imageCaptured);
        loadimage();
    }

    private void loadimage() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            byte[] b = extras.getByteArray("capture");

            if(b != null){
                image = findViewById(R.id.imageCaptured);

                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

                rotateBitmap = rotate(decodedBitmap);


                image.setImageBitmap(rotateBitmap);
            }

        }

    }

    private Bitmap rotate(Bitmap decodedBitmap) {
        int w = decodedBitmap.getWidth();
        int h = decodedBitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate(90);

        return Bitmap.createBitmap(decodedBitmap, 0, 0, w, h, matrix, true);

    }
}