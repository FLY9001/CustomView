package com.tyf.ui.viewwidget;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tyf.ui.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerFragment extends Fragment {
    private View rootView;
    RecyclerView rv_recycler;

    public RecyclerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
        rv_recycler = (RecyclerView) rootView.findViewById(R.id.rv_recycler);
        List<Bean> beens = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            if (i % 3 != 0) {
                beens.add(new Bean(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), "11111", ListViewFragment.Bean.TYPE_RECEIVED));
            } else {
                beens.add(new Bean(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), "11111", ListViewFragment.Bean.TYPE_SENT));
            }
        }

        //设置LayoutManager
        rv_recycler.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        rv_recycler.setHasFixedSize(true);
 //       rv_recycler.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(beens);
        rv_recycler.setAdapter(recyclerViewAdapter);
        return rootView;
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Bean> mBeans;

        public RecyclerViewAdapter(List<Bean> beans) {
            mBeans = beans;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            switch (viewType) {
                case Bean.TYPE_RECEIVED:
                    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left, parent, false);
                    final ViewHolder viewHolder = new ViewHolder(itemView);
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int adapterPosition = viewHolder.getAdapterPosition();
                            Toast.makeText(v.getContext(), "点击了第" + adapterPosition + "个", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return viewHolder;
                case Bean.TYPE_SENT:
                    View itemView2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_right, parent, false);
                    final ViewHolder2 viewHolder2 = new ViewHolder2(itemView2);
                    viewHolder2.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int adapterPosition = viewHolder2.getAdapterPosition();
                            Toast.makeText(v.getContext(), "点击了第" + adapterPosition + "个", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return viewHolder2;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof RecyclerViewAdapter.ViewHolder) {
                ((RecyclerViewAdapter.ViewHolder) holder).icon.setImageBitmap(mBeans.get(position).getIcon());
                ((RecyclerViewAdapter.ViewHolder) holder).text.setText(mBeans.get(position).getText());
            } else if (holder instanceof RecyclerViewAdapter.ViewHolder2) {
                ((RecyclerViewAdapter.ViewHolder2) holder).icon.setImageBitmap(mBeans.get(position).getIcon());
                ((RecyclerViewAdapter.ViewHolder2) holder).text.setText(mBeans.get(position).getText());
            }
        }

        @Override
        public int getItemCount() {
            return mBeans.size();
        }

        @Override
        public int getItemViewType(int position) {
            return mBeans.get(position).getType();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            View itemView;
            ImageView icon;
            TextView text;

            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                icon = (ImageView) itemView.findViewById(R.id.iv_left_listView);
                text = (TextView) itemView.findViewById(R.id.tv_left_listView);
            }
        }

        class ViewHolder2 extends RecyclerView.ViewHolder {
            View itemView;
            ImageView icon;
            TextView text;

            public ViewHolder2(View itemView) {
                super(itemView);
                this.itemView = itemView;
                icon = (ImageView) itemView.findViewById(R.id.iv_right_listView);
                text = (TextView) itemView.findViewById(R.id.tv_right_listView);
            }
        }
    }

    /**
     * 实体类
     * Created by lgl on 16/3/20.
     */
    public class Bean {
        public static final int TYPE_RECEIVED = 0;
        public static final int TYPE_SENT = 1;

        private int type;
        private Bitmap icon;
        private String text;

        public Bean(Bitmap icon, String text, int type) {
            this.icon = icon;
            this.text = text;
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Bitmap getIcon() {
            return icon;
        }

        public void setIcon(Bitmap icon) {
            this.icon = icon;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
