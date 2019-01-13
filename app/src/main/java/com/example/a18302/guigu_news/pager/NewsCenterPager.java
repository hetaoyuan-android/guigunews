package com.example.a18302.guigu_news.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.a18302.guigu_news.MainActivity;
import com.example.a18302.guigu_news.base.BasePager;
import com.example.a18302.guigu_news.base.MenuDetailBasePager;
import com.example.a18302.guigu_news.domain.NewsCenterPagerBean;
import com.example.a18302.guigu_news.domain.NewsCenterPagerBean2;
import com.example.a18302.guigu_news.fragment.LeftMenuFragment;
import com.example.a18302.guigu_news.menudeatailpager.InteracMenuDetailPager;
import com.example.a18302.guigu_news.menudeatailpager.NewsMenuDetailPager;
import com.example.a18302.guigu_news.menudeatailpager.PhotosMenuDetailPager;
import com.example.a18302.guigu_news.menudeatailpager.TopicMenuDetailPager;
import com.example.a18302.guigu_news.utils.CacheUtils;
import com.example.a18302.guigu_news.utils.Contants;
import com.example.a18302.guigu_news.utils.LogUtil;
import com.example.a18302.guigu_news.volley.VolleyManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.example.a18302.guigu_news.utils.Contants.NEWSCENTER_PAGER_URL;

public class NewsCenterPager extends BasePager {

//    private List<NewsCenterPagerBean.DataEntity> data;

    /**
     * 左侧菜单对应的数据集合
     */
    private List<NewsCenterPagerBean2.DetailPagerData> data;


    //详情页面的集合
    private ArrayList<MenuDetailBasePager> detailBasePagers;

    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        ib_menu.setVisibility(View.VISIBLE);
        LogUtil.e("新闻页面数据被初始化了..");
        //标题
        tv_title.setText("新闻中心");
        //联网请求得到数据
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        //子试图添加到basePager的FrameLayout
        fl_content.addView(textView);
        LogUtil.e("我是新闻中心页面");
        //绑定数据
        textView.setText("我是新闻中心内容");
        //获取缓存数据
        String saveJson = CacheUtils.getString(context, Contants.NEWSCENTER_PAGER_URL);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        //联网请求数据
//        getDataFromNet();
        getDataFromNetByVolley();
    }

    /**
     * 使用Volley联网请求数据
     */
    private void getDataFromNetByVolley() {
        //请求队列
//        RequestQueue queue = Volley.newRequestQueue(context);
        //String请求
        StringRequest request = new StringRequest(Request.Method.GET, Contants.NEWSCENTER_PAGER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                LogUtil.e("Volley--passTime==" + result);
                LogUtil.e("使用Volley联网请求成功==" + result);
                //缓存数据
                CacheUtils.putString(context, Contants.NEWSCENTER_PAGER_URL, result);

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

    private void getDataFromNet() {
        RequestParams params = new RequestParams(NEWSCENTER_PAGER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("使用xutils3联网请求成功==" + result);
                //缓存数据
                CacheUtils.putString(context, NEWSCENTER_PAGER_URL, result);

                processData(result);
                //设置适配器
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("使用xutils3联网请求失败==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("使用xutils3联网请求取消==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("使用xutils3联网请求onFinished");
            }
        });


    }

    /**
     * 解析json数据和显示数据
     *
     * @param result
     */
    private void processData(String result) {
//        NewsCenterPagerBean bean = parsedJson(result);
        NewsCenterPagerBean2 bean = parsedJson2(result);
//        String title = bean.getData().get(0).getChildren().get(1).getTitle();
        String title2 = bean.getData().get(0).getChildren().get(1).getTitle();
        LogUtil.e("使用Gson解析成功" + title2);

        //给左侧菜单传值
        data = bean.getData();
        MainActivity mainActivity = (MainActivity) context;
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        //添加详情页面
        detailBasePagers = new ArrayList<>();
        detailBasePagers.add(new NewsMenuDetailPager(context, data.get(0)));
        detailBasePagers.add(new TopicMenuDetailPager(context, data.get(0)));
        detailBasePagers.add(new PhotosMenuDetailPager(context, data.get(2)));
        detailBasePagers.add(new InteracMenuDetailPager(context, data.get(2)));
        //把数据传递给左侧菜单
        leftMenuFragment.setData(data);
    }

    /**
     * 解析json的两种方式
     * 1.使用系统的API解析JSON
     * 2.第三方GSON，fastjson
     *
     * @param json
     * @return
     */
    private NewsCenterPagerBean2 parsedJson(String json) {
        return new Gson().fromJson(json, NewsCenterPagerBean2.class);
    }

    private NewsCenterPagerBean2 parsedJson2(String json) {

        NewsCenterPagerBean2 bean2 = new NewsCenterPagerBean2();
        try {
            JSONObject object = new JSONObject(json);
            int retcode = object.optInt("retcode");
            bean2.setRetcode(retcode);

            JSONArray data = object.optJSONArray("data");
            if (data != null && data.length() > 0) {

                List<NewsCenterPagerBean2.DetailPagerData> detailPagerDatas = new ArrayList<>();
                bean2.setData(detailPagerDatas);
                //for循环，解析每条数据
                for (int i = 0; i < data.length(); i++) {

                    NewsCenterPagerBean2.DetailPagerData detailPagerData = new NewsCenterPagerBean2.DetailPagerData();
                    //添加到集合中
                    detailPagerDatas.add(detailPagerData);

                    JSONObject jsonObject = (JSONObject) data.get(i);

                    int id = jsonObject.optInt("id");
                    detailPagerData.setId(id);
                    int type = jsonObject.optInt("type");
                    detailPagerData.setType(type);
                    String title = jsonObject.optString("title");
                    detailPagerData.setTitle(title);
                    String url = jsonObject.optString("url");
                    detailPagerData.setUrl(url);
                    String url1 = jsonObject.optString("url1");
                    detailPagerData.setUrl1(url1);
                    String dayurl = jsonObject.optString("dayurl");
                    detailPagerData.setDayurl(dayurl);
                    String excurl = jsonObject.optString("excurl");
                    detailPagerData.setExcurl(excurl);
                    String weekurl = jsonObject.optString("weekurl");
                    detailPagerData.setWeekurl(weekurl);


                    JSONArray children = jsonObject.optJSONArray("children");
                    if (children != null && children.length() > 0) {

                        List<NewsCenterPagerBean2.DetailPagerData.ChildrenData> childrenDatas = new ArrayList<>();

                        //设置集合-ChildrenData
                        detailPagerData.setChildren(childrenDatas);

                        for (int j = 0; j < children.length(); j++) {
                            JSONObject childrenitem = (JSONObject) children.get(j);

                            NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData = new NewsCenterPagerBean2.DetailPagerData.ChildrenData();
                            //添加到集合中
                            childrenDatas.add(childrenData);


                            int childId = childrenitem.optInt("id");
                            childrenData.setId(childId);
                            String childTitle = childrenitem.optString("title");
                            childrenData.setTitle(childTitle);
                            String childUrl = childrenitem.optString("url");
                            childrenData.setUrl(childUrl);
                            int childType = childrenitem.optInt("type");
                            childrenData.setType(childType);

                        }

                    }
                }


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bean2;
    }

    /**
     * 根据位置切换详情页面
     *
     * @param i
     */
    public void swichPager(int i) {
        if (i < detailBasePagers.size()) {
            //设置标题
            tv_title.setText(data.get(i).getTitle());
            //移除之前的内容
            fl_content.removeAllViews();

            //添加新的内容
            MenuDetailBasePager detailBasePager = detailBasePagers.get(i);
            View rootView = detailBasePager.rootView;
            detailBasePager.initData();

            fl_content.addView(rootView);

            if (i == 2) {
                //图组详情
                ib_switch_list_grid.setVisibility(View.VISIBLE);
                ib_switch_list_grid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PhotosMenuDetailPager detailPager = (PhotosMenuDetailPager) detailBasePagers.get(2);
                        detailPager.swichListAndGrid(ib_switch_list_grid);
                    }
                });
            } else {
                ib_switch_list_grid.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(context, "该页面还没有启用", Toast.LENGTH_SHORT).show();
        }
    }
}
