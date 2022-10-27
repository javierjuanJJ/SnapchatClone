package whatsappclone.proyecto_javier_juan_uceda.snapchatclone.RecyclerViewStory;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import whatsappclone.proyecto_javier_juan_uceda.snapchatclone.R;

public class StoryViewHolders extends RecyclerView.ViewHolder {

   public TextView mEmail;

   public StoryViewHolders(View itemView){
      super(itemView);
      mEmail = itemView.findViewById(R.id.email);

   }

}
