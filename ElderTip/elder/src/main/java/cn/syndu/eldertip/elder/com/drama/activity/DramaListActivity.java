package cn.syndu.eldertip.elder.com.drama.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.syndu.eldertip.elder.R;
import cn.syndu.eldertip.elder.com.drama.adapter.DrameListAdapter;
import cn.syndu.eldertip.elder.com.drama.domain.DramaData;

public class DramaListActivity extends Activity {
    private List<DramaData> dramaDatas = new ArrayList<DramaData>();
    TextView textViewOfType;
    String data;
    ListView dramaListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drama_list_from_type);
        Bundle bundle = getIntent().getExtras();
        data=bundle.getString("Data");
        initView();
        setView();
        InitSetData();
        DrameListAdapter drameListAdapter = new DrameListAdapter(getApplicationContext());
        drameListAdapter.setMainDramaDatas(dramaDatas);
        dramaListView.setAdapter(drameListAdapter);
    }

//    5229
    private void  initView(){
        textViewOfType = (TextView) findViewById(R.id.textViewOfType);
        dramaListView= (ListView) findViewById(R.id.dramalist);
    }

    private  void setView(){
        textViewOfType.setText(data);
    }

    private void InitSetData() {
        DramaData mData1 = new DramaData(null,null,"第一部");
        dramaDatas.add(mData1);
        DramaData mData2 = new DramaData(null,null,"第er部");
        dramaDatas.add(mData2);
        DramaData mData3 = new DramaData(null,null,"第san部");
        dramaDatas.add(mData3);
    }
}
