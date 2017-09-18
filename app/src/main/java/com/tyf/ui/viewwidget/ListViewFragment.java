package com.tyf.ui.viewwidget;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tyf.ui.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListViewFragment extends Fragment {
    private View rootView;
    private ListView mListView;


    public ListViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list_view, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listview);
        List<Bean> beens = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            if (i % 3 != 0) {
                beens.add(new Bean(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), "11111", Bean.TYPE_RECEIVED));
            } else {
                beens.add(new Bean(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), "11111", Bean.TYPE_SENT));
            }
        }
        ListViewAdapter listViewAdapter = new ListViewAdapter(beens);
        mListView.setAdapter(listViewAdapter);
        mListView.setEmptyView(rootView.findViewById(R.id.textview));
        return rootView;
    }

    class ListViewAdapter extends BaseAdapter {
        private List<Bean> mBeans;

        public ListViewAdapter(List<Bean> beans) {
            mBeans = beans;
        }

        @Override
        public int getCount() {
            return mBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return mBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                if (getItemViewType(position) == Bean.TYPE_RECEIVED) {
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left, parent, false);
                    holder.icon = (ImageView) convertView.findViewById(R.id.iv_left_listView);
                    holder.text = (TextView) convertView.findViewById(R.id.tv_left_listView);
                } else {
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_right, parent, false);
                    holder.icon = (ImageView) convertView.findViewById(R.id.iv_right_listView);
                    holder.text = (TextView) convertView.findViewById(R.id.tv_right_listView);
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.icon.setImageBitmap(mBeans.get(position).getIcon());
            holder.text.setText(mBeans.get(position).getText());
            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            return mBeans.get(position).getType();
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        public class ViewHolder {
            ImageView icon;
            TextView text;
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
