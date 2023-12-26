package com.leduongw01.mlis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leduongw01.mlis.R;
import com.leduongw01.mlis.listener.RecycleViewLongClickListener;
import com.leduongw01.mlis.listener.RecyclerViewClickListener;
import com.leduongw01.mlis.models.StringValue;

import java.util.List;

public class AccountOptionAdapter extends RecyclerView.Adapter<AccountOptionAdapter.AccountOptionViewHolder> {
    RecyclerViewClickListener accountOptionClickListener;
    Context context;
    List<StringValue> optionList;
    public AccountOptionAdapter(Context context, List<StringValue> optionList, RecyclerViewClickListener recyclerViewClickListener){
        this.context = context;
        this.optionList = optionList;
        this.accountOptionClickListener = recyclerViewClickListener;
    }
    @NonNull
    @Override
    public AccountOptionAdapter.AccountOptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_option_myaccount, parent, false);
        return new AccountOptionViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountOptionAdapter.AccountOptionViewHolder holder, int position) {
        holder.optionName.setText(optionList.get(position).getText1());
        holder.optionDes.setText(optionList.get(position).getText2());
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    class AccountOptionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView optionName;
        public TextView optionDes;

        public AccountOptionViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);
            optionName = itemView.findViewById(R.id.optionName);
            optionDes = itemView.findViewById(R.id.optionDes);
        }

        @Override
        public void onClick(View view) {
            accountOptionClickListener.recyclerViewListClicked(view, getLayoutPosition());
        }
    }
}
