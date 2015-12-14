package cn.syndu.eldertip.elder.com.drama.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.syndu.eldertip.elder.R;
import cn.syndu.eldertip.elder.com.drama.adapter.MainDramaAdapter;
import cn.syndu.eldertip.elder.com.drama.domain.MainDramaData;

public class MainDramaListActivity extends Activity {
     ListView MainDramaList;
    private List<MainDramaData> mainDataList = new ArrayList<MainDramaData>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drama_list);
        MainDramaAdapter mAdapter = new MainDramaAdapter(getApplicationContext());
        InitSetData();
        mAdapter.setMainDramaDatas(mainDataList);
        MainDramaList = (ListView) findViewById(R.id.MainDramaList);
        MainDramaList.setAdapter(mAdapter);
        setListClick();
    }

    private void InitSetData() {
        MainDramaData mData1 = new MainDramaData("类型一") ;
        mainDataList.add(mData1);
        MainDramaData mData2 = new MainDramaData("类型二");
        mainDataList.add(mData2);
        MainDramaData mData3 = new MainDramaData("类型三");
        mainDataList.add(mData3);
    }

    private void setListClick(){
        MainDramaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("hehe","这是第"+position+"个");
                Log.i("hehe", "这是" + mainDataList.get(position).getTypeName());
            }
        });

    }

}
