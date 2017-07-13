package com.example.linghao.newsclient.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.linghao.newsclient.R;
import com.example.linghao.newsclient.activity.MainActivity;
import com.example.linghao.newsclient.base.BaseFragment;
import com.example.linghao.newsclient.domain.NewsPagerBean;
import com.example.linghao.newsclient.menudetailPager.NewsDetailPager;
import com.example.linghao.newsclient.pager.newspager;
import com.example.linghao.newsclient.utils.DensityUtil;
import com.example.linghao.newsclient.utils.LogUtil;

import java.util.List;

/**
 * Created by linghao on 2017/6/26.
 */

public class LeftmenuFragment extends BaseFragment {
    private ListView listview;
    private int pressPosition;
    private List<NewsPagerBean.DataBean> data;
    leftmenuAdapter leftmenuAdapter = new leftmenuAdapter();

    @Override
    public View initView() {
        listview = new ListView(context);
        listview.setPadding(0, DensityUtil.dip2px(context, 40), 0, 0);
        listview.setDividerHeight(0);
        listview.setCacheColorHint(Color.TRANSPARENT);
        listview.setSelector(android.R.color.transparent);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //把点击的位置切换成红色
                pressPosition = position;
                leftmenuAdapter.notifyDataSetChanged();
                //关闭侧滑
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();
                //切换到对面的页面
                SwichPager(pressPosition);

            }


        });

        return listview;
    }

    private void SwichPager(int position) {
        MainActivity mainActivity = (MainActivity) context;
        ContextFragment contextFragment = mainActivity.getContextFragment();
        newspager newspager = contextFragment.getNewsDetailPager();
        newspager.switchPager(position);

    }

    @Override
    public void initData() {
        super.initData();

    }


    public void setData(List<NewsPagerBean.DataBean> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            LogUtil.e("title" + data.get(i).getTitle());
        }
        listview.setAdapter(leftmenuAdapter);
        SwichPager(pressPosition);
    }

    private class leftmenuAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textview = (TextView) View.inflate(context, R.layout.left_menu, null);
            textview.setText(data.get(position).getTitle());
            if (pressPosition == position)
                textview.setEnabled(true);
            else {
                textview.setEnabled(false);
            }
            return textview;
        }
    }
}
