package com.example.pallavi.shareit;


        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import java.io.File;
        import java.util.List;

        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Toast;

        import com.google.android.gms.plus.Moments;

        import java.util.List;

public class SolventRecyclerViewAdapter  extends RecyclerView.Adapter<SolventViewHolders> {

    private List<Moment> itemList;
    private Context context;
    private User loggedInUser;

    public SolventRecyclerViewAdapter(Context context) {

        this.context = context;
    }
    public SolventRecyclerViewAdapter(Context context, List<Moment> itemList, User loggedInUser) {
        this.itemList = itemList;
        this.context = context;
        this.loggedInUser=loggedInUser;
    }

    @Override
    public SolventViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.solvent_list, null);
        SolventViewHolders rcv = new SolventViewHolders(layoutView,itemList,loggedInUser);
        return rcv;
    }

    @Override
    public void onBindViewHolder(SolventViewHolders holder, int position) {
        holder.countryName.setText(itemList.get(position).getMomentName());

        Log.d("Where stored?", itemList.get(position).getMomentPath());

        File imageFile = new File(itemList.get(position).getMomentPath());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;  // Experiment with different sizes
        Bitmap b = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        holder.countryPhoto.setImageBitmap(b);
        //holder.countryPhoto.setImageResource(itemList.get(position).getPhoto());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

}


