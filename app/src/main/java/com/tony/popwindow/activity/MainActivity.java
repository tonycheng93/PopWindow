package com.tony.popwindow.activity;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.tony.popwindow.R;
import com.tony.popwindow.adapter.ProvinceAdapter;
import com.tony.popwindow.adapter.SchoolAdapter;
import com.tony.popwindow.config.Config;
import com.tony.popwindow.entity.Province;
import com.tony.popwindow.entity.ProvinceList;
import com.tony.popwindow.entity.School;
import com.tony.popwindow.entity.SchoolList;
import com.tony.popwindow.utils.GsonRequest;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private EditText mSelectSchool;
    /**
     * popView相关
     **/
    private View parent;
    private ListView mProvinceListView;
    private ListView mSchoolListView;
    private TextView mTitle;
    private PopupWindow mPopWindow;
    /**
     * Volley相关
     **/
    private RequestQueue mRequestQueue;
    /**
     * Adapter相关
     **/
    private ProvinceAdapter mProvinceAdapter;
    private SchoolAdapter mSchoolAdapter;
    private String provinceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSelectSchool = (EditText) findViewById(R.id.select_school);

        initPopView();
        mSelectSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();
            }
        });
    }

    private void initPopView() {
        parent = this.getWindow().getDecorView();
        View popView = View.inflate(this, R.layout.view_select_province_list, null);
        mTitle = (TextView) popView.findViewById(R.id.list_title);
        mProvinceListView = (ListView) popView.findViewById(R.id.province);
        mSchoolListView = (ListView) popView.findViewById(R.id.school);
        mProvinceListView.setOnItemClickListener(itemListener);
        mSchoolListView.setOnItemClickListener(itemListener);

        mProvinceAdapter = new ProvinceAdapter(this);
        mProvinceListView.setAdapter(mProvinceAdapter);
        mSchoolAdapter = new SchoolAdapter(this);
        mSchoolListView.setAdapter(mSchoolAdapter);

        int width = getResources().getDisplayMetrics().widthPixels * 3 / 4;
        int height = getResources().getDisplayMetrics().heightPixels * 3 / 5;
        mPopWindow = new PopupWindow(popView, width, height);
        ColorDrawable dw = new ColorDrawable(0x30000000);
        mPopWindow.setBackgroundDrawable(dw);
        mPopWindow.setFocusable(true);
        mPopWindow.setTouchable(true);
        mPopWindow.setOutsideTouchable(true);//允许在外侧点击取消

        loadProvince();

        mPopWindow.setOnDismissListener(listener);
    }

    private void showPopWindow() {
        mPopWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    private void loadProvince() {
        mRequestQueue = Volley.newRequestQueue(this);
        GsonRequest<Province> request = new GsonRequest<Province>(Request.Method.POST, Config.PROVINCE_URL,
                Province.class, new Response.Listener<Province>() {
            @Override
            public void onResponse(Province response) {
                if (response.getData() != null && response.getError_code() == 0) {
                    mProvinceAdapter.setList(response.getData());
                    mProvinceAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }, this);
        mRequestQueue.add(request);
    }

    private void loadSchool() {
        mRequestQueue = Volley.newRequestQueue(this);
        GsonRequest<School> request = new GsonRequest<>(Request.Method.POST, Config.SCHOOL_URL + provinceId, School.class,
                new Response.Listener<School>() {
                    @Override
                    public void onResponse(School response) {
                        if (response.getData() != null && response.getError_code() == 0){
                            mSchoolAdapter.setList(response.getData());
                            mSchoolAdapter.notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, this);
        mRequestQueue.add(request);
    }

    /**
     * ListView Item点击事件
     */
    AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (parent == mProvinceListView) {
                ProvinceList provinceName = (ProvinceList) mProvinceListView.getItemAtPosition(position);
                provinceId = provinceName.getProvince_id();
                mTitle.setText("选择学校");
                mProvinceListView.setVisibility(View.GONE);
                mSchoolListView.setVisibility(View.VISIBLE);
                loadSchool();
            } else if (parent == mSchoolListView) {
                SchoolList schoolName = (SchoolList) mSchoolListView.getItemAtPosition(position);
                mSelectSchool.setText(schoolName.getSchool_name());
                mPopWindow.dismiss();
            }
        }
    };

    /**
     * popWindow消失监听事件
     */
    PopupWindow.OnDismissListener listener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            mTitle.setText("选择地区");
            mProvinceListView.setVisibility(View.VISIBLE);
            mSchoolAdapter.setList(new ArrayList<SchoolList>());
            mSchoolAdapter.notifyDataSetChanged();
            mSchoolListView.setVisibility(View.GONE);
        }
    };
}
