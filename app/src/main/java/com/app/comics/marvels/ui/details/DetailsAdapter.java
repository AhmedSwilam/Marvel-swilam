package com.app.comics.marvels.ui.details;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.comics.marvels.R;
import com.app.comics.marvels.data.network.model.Comics;
import com.app.comics.marvels.ui.home.characters.CharacterAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //adapter for character details

    private List<Comics> comics;
    private int tapCount = 0;
    private CharacterAdapter.ItemClickListener mListener;

    public DetailsAdapter(List<Comics> comics) {
        this.comics = comics;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_comics_data, parent, false);
        final DetailsAdapter.CharacterViewHolder characterViewHolder = new DetailsAdapter.CharacterViewHolder(view);

        return characterViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CharacterAdapter.CharacterViewHolder characterViewHolder = (CharacterAdapter.CharacterViewHolder) holder;
        String path = comics.get(position).getItems().get(position).getName() + "." + comics.get(position).getItems()
                .get(position).getResourceURI();
        Glide.with(characterViewHolder.itemView.getContext())
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .crossFade()
                .into(characterViewHolder.characterIV);

        characterViewHolder.nameTV.setText(comics.get(position).getItems().get(position).getName());

    }

    private void onDoubleClick(int position, Comics result) {
        //TODO: create a callback to save result item in db as favorite
        //callback.onLikeRecipeClick(position, result);
    }


    @Override
    public int getItemCount() {
        return comics.size();
    }

    public Comics getItem(int position) {
        if (position != RecyclerView.NO_POSITION)
            return comics.get(position);
        else
            return null;
    }

    public class CharacterViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTV;
        public ImageView likeIV;

        public CharacterViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.txt_comics);
            likeIV = itemView.findViewById(R.id.img_comics);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClicked(getAdapterPosition());

                        }
                    }
                }
            });


        }
    }


}
