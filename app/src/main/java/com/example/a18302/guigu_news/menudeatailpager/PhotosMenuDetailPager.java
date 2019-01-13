package com.example.a18302.guigu_news.menudeatailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.a18302.guigu_news.R;
import com.example.a18302.guigu_news.activity.ShowImageActivity;
import com.example.a18302.guigu_news.base.MenuDetailBasePager;
import com.example.a18302.guigu_news.domain.NewsCenterPagerBean2;
import com.example.a18302.guigu_news.domain.PhotosMenuDetailPagerBean;
import com.example.a18302.guigu_news.utils.CacheUtils;
import com.example.a18302.guigu_news.utils.Contants;
import com.example.a18302.guigu_news.utils.LogUtil;
import com.example.a18302.guigu_news.volley.VolleyManager;
import com.google.gson.Gson;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.example.a18302.guigu_news.utils.Contants.NEWSCENTER_PAGER_URL;

public class PhotosMenuDetailPager extends MenuDetailBasePager {

    private NewsCenterPagerBean2.DetailPagerData detailPagerData;
    @ViewInject(R.id.listview)
    private ListView listView;

    @ViewInject(R.id.gridview)
    private GridView gridview;

    private String url;

    private List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> news;
    private PhotoMenuDetailPagerAdapter adapter;

    public PhotosMenuDetailPager(Context context, NewsCenterPagerBean2.DetailPagerData detailPagerData) {
        super(context);
        this.detailPagerData = detailPagerData;

    }

    public PhotosMenuDetailPager(Context context) {
        super(context);
    }

    @Override
    protected View initView() {
        View view = View.inflate(context, R.layout.photos_menudetail_pager, null);
        x.view().inject(this, view);
        //设置点击某条的item的监听
        listView.setOnItemClickListener(new MyOnItemClickListener());
        gridview.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            PhotosMenuDetailPagerBean.DataEntity.NewsEntity newsEntity = news.get(position);
            String imageUrl = Contants.BASE_URL+newsEntity.getLargeimage();
            Intent intent = new Intent(context, ShowImageActivity.class);
            intent.putExtra("url",imageUrl);
            context.startActivity(intent);
        }
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("图组详情页面数据被初始化了");
        url = Contants.BASE_URL + detailPagerData.getUrl();
        Log.i("yuanhteao","url" + detailPagerData.getUrl());
        String saveJson = CacheUtils.getString(context, url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        getDataFromNet();
    }

    private void getDataFromNet() {
        //String请求
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogUtil.e("使用Volley联网请求成功==" + result);
                //缓存数据
                CacheUtils.putString(context, url, result);

                processData(result);
                //设置适配器
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("使用Volley联网请求失败==" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String parsed = new String(response.data, "UTF-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }
        };

        //添加到队列
        VolleyManager.getRequestQueue().add(request);
    }
    /**
     * 解析和显示数据
     *
     * @param result
     */
    private void processData(String result) {
        PhotosMenuDetailPagerBean bean = parseJson(result);
        Log.i("abc",bean.getData().getNews().get(0).getTitle()+"000");
        news = bean.getData().getNews();
        LogUtil.e("图组详情页面"+news);
        adapter = new PhotoMenuDetailPagerAdapter();
        listView.setAdapter(adapter);
    }

    class PhotoMenuDetailPagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = View.inflate(context, R.layout.item_photos_menudetail_pager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = view.findViewById(R.id.iv_icon);
                viewHolder.tv_title = view.findViewById(R.id.tv_title);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            //位置得到数据
            PhotosMenuDetailPagerBean.DataEntity.NewsEntity newsEntity = news.get(i);
            viewHolder.tv_title.setText(newsEntity.getTitle());
            String imageUrl = Contants.BASE_URL + newsEntity.getSmallimage();
            //设置图片
            loaderImager(viewHolder,imageUrl);
            return view;
        }
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
    }

    private PhotosMenuDetailPagerBean parseJson(String result) {
        return new Gson().fromJson(result, PhotosMenuDetailPagerBean.class);
    }

    /**
     *
     * @param viewHolder
     * @param imageurl
     */
    private void loaderImager(final ViewHolder viewHolder, String imageurl) {

        //设置tag
        viewHolder.iv_icon.setTag(imageurl);
        //直接在这里请求会乱位置
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer != null) {

                    if (viewHolder.iv_icon != null) {
                        if (imageContainer.getBitmap() != null) {
                            //设置图片
                            viewHolder.iv_icon.setImageBitmap(imageContainer.getBitmap());
                        } else {
                            //设置默认图片
                            viewHolder.iv_icon.setImageResource(R.drawable.home_scroll_default);
                        }
                    }
                }
            }
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //如果出错，则说明都不显示（简单处理），最好准备一张出错图片
                viewHolder.iv_icon.setImageResource(R.drawable.home_scroll_default);
            }
        };
        VolleyManager.getImageLoader().get(imageurl, listener);
    }


    /**
     * true,显示ListView，隐藏GridView
     * false,显示GridView,隐藏ListView
     */
    private boolean isShowListView = true;

    public void swichListAndGrid(ImageButton ib_swich_list_grid) {
        if(isShowListView){
            isShowListView = false;
            //显示GridView,隐藏ListView
            gridview.setVisibility(View.VISIBLE);
            adapter = new PhotoMenuDetailPagerAdapter();
            gridview.setAdapter(adapter);
            listView.setVisibility(View.GONE);
            //按钮显示--ListView
            ib_swich_list_grid.setImageResource(R.drawable.icon_pic_list_type);


        }else{
            isShowListView = true;
            //显示ListView，隐藏GridView
            listView.setVisibility(View.VISIBLE);
            adapter = new PhotoMenuDetailPagerAdapter();
            listView.setAdapter(adapter);
            gridview.setVisibility(View.GONE);
            //按钮显示--GridView
            ib_swich_list_grid.setImageResource(R.drawable.icon_pic_grid_type);
        }
    }
}
