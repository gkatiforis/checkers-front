package com.katiforis.top10.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.katiforis.top10.DTO.GamePlayerDTO;
import com.katiforis.top10.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.GroceryViewHolder>{
    private List<GamePlayerDTO> horizontalGrocderyList;
    Context context;

    public UserAdapter(List<GamePlayerDTO> horizontalGrocderyList, Context context){
        this.horizontalGrocderyList= horizontalGrocderyList;
        this.context = context;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {
        holder.imageView.setImageResource(horizontalGrocderyList.get(position).getImg());
        holder.txtview.setText(horizontalGrocderyList.get(position).getUsername() + "(" + horizontalGrocderyList.get(position).getPoints() + ")");
//        holder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String productName = horizontalGrocderyList.get(position).getProductName().toString();
//                Toast.makeText(context, productName + " is selected", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtview;
        public GroceryViewHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.statusImage);
            txtview=view.findViewById(R.id.idProductName);
        }
    }
}