package whatsappclone.proyecto_javier_juan_uceda.snapchatclone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class ShowCaptureActivity extends ParentActivity {

    private ImageView mImage;
    private Bitmap bitmap;
    private String Uid;
    private Button mStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_capture);
        setUI();
    }

    private void setUI() {
        try {
            mImage = findViewById(R.id.imageCaptured);
            mStory = findViewById(R.id.story);
            mStory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveToStories();
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

    private void saveToStories() {
        final DatabaseReference userStoryDb = FirebaseDatabase.getInstance().getReference().child("users").child(Uid).child("story");
        final String key = userStoryDb.push().getKey();

        StorageReference filePath = FirebaseStorage.getInstance().getReference().child("captures").child(key);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] dataToUpload = baos.toByteArray();
        UploadTask uploadTask = filePath.putBytes(dataToUpload);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful()) ;

                Uri imageUrl = urlTask.getResult();

                Long currentTimestamp = System.currentTimeMillis();
                Long endTimestamp = currentTimestamp + (24 * 60 * 60 * 1000);

                Map<String, Object> mapToUpload = new HashMap<>();
                mapToUpload.put("imageUrl", imageUrl.toString());
                mapToUpload.put("timestampBeg", currentTimestamp);
                mapToUpload.put("timestampEnd", endTimestamp);

                userStoryDb.child(key).setValue(mapToUpload);

                Log.i("upload " + this.getClass().getSimpleName(), "Upload correct");
                Toast.makeText(ShowCaptureActivity.this, "Upload correct", Toast.LENGTH_SHORT).show();

                finish();
                return;
            }
        });


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("upload " + this.getClass().getSimpleName(), "Upload error");
                Toast.makeText(ShowCaptureActivity.this, "Upload error", Toast.LENGTH_SHORT).show();

                finish();
                return;
            }
        });
    }

    private void firebaseLoad() {
        Uid = FirebaseAuth.getInstance().getUid();
    }

    private void loadimage() throws FileNotFoundException {
        bitmap = BitmapFactory.decodeStream(getApplication().openFileInput("imageToSend"));

    }
}