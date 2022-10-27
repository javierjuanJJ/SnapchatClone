package whatsappclone.proyecto_javier_juan_uceda.snapchatclone.RecyclerViewStory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import whatsappclone.proyecto_javier_juan_uceda.snapchatclone.DisplayImageActivity;
import whatsappclone.proyecto_javier_juan_uceda.snapchatclone.R;

public class StoryViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

   public TextView mEmail;

   public StoryViewHolders(View itemView){
      super(itemView);
      itemView.setOnClickListener(this);
      mEmail = itemView.findViewById(R.id.email);

   }

   @Override
   public void onClick(View view) {
      Intent intent = new Intent(view.getContext(), DisplayImageActivity.class);
      Bundle b = new Bundle();
      b.putString("userId", mEmail.getTag().toString());
      intent.putExtras(b);
      view.getContext().startActivity(intent);
   }
}
