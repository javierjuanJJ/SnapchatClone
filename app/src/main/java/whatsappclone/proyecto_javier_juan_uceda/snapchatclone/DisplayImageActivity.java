package whatsappclone.proyecto_javier_juan_uceda.snapchatclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayImageActivity extends ParentActivity {


    private String userId;

    private ImageView mImage;

    private boolean started = false;
    private int imageIterator = 0;
    ArrayList<String> imageUrlList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        setUI();

    }

    private void setUI() {
        Bundle b = getIntent().getExtras();
        userId = b.getString("userId");

        mImage = findViewById(R.id.image);

        listenForData();
    }

    private void listenForData() {
        DatabaseReference followingStoryDb = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        followingStoryDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageUrl = "";
                long timestampBeg = 0;
                long timestampEnd = 0;
                for(DataSnapshot storySnapshot : dataSnapshot.child("story").getChildren()){
                    if(storySnapshot.child("timestampBeg").getValue() != null){
                        timestampBeg = Long.parseLong(storySnapshot.child("timestampBeg").getValue().toString());
                    }
                    if(storySnapshot.child("timestampEnd").getValue() != null){
                        timestampEnd = Long.parseLong(storySnapshot.child("timestampEnd").getValue().toString());
                    }
                    if(storySnapshot.child("imageUrl").getValue() != null){
                        imageUrl = storySnapshot.child("imageUrl").getValue().toString();
                    }
                    long timestampCurrent = System.currentTimeMillis();
                    if(timestampCurrent >= timestampBeg && timestampCurrent <= timestampEnd){
                        imageUrlList.add(imageUrl);
                        if (!started){
                            started = true;
                            initializeDisplay();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void initializeDisplay() {
        Glide.with(getApplication()).load(imageUrlList.get(imageIterator)).into(mImage);

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeImage();
            }
        });
        final Handler handler = new Handler();
        final int delay = 5000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeImage();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void changeImage() {
        if(imageIterator == imageUrlList.size() - 1){
            finish();
            return;
        }
        imageIterator++;
        Glide.with(getApplication()).load(imageUrlList.get(imageIterator)).into(mImage);
    }

}