package com.zhiyuan3g.androidweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyuan3g.androidweather.R;
import com.zhiyuan3g.androidweather.db.CityDB;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/23.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {


    private Context context;
    private List<CityDB> cityDBList;

    private onItemClick onItemClick;

    public void setOnItemClick(CityAdapter.onItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface onItemClick {
        void onItemClick(int position);
    }

    public CityAdapter(Context context, List<CityDB> cityDBList) {
        this.context = context;
        this.cityDBList = cityDBList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_public, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClick(viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(cityDBList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return cityDBList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }
    }
}
