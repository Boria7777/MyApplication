package cn.syndu.eldertip.elder.com.drama.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.syndu.eldertip.elder.R;
import cn.syndu.eldertip.elder.com.drama.domain.MainDramaData;

/**
 * Created by Boria on 2015/12/11.
 */
public class MainDramaAdapter extends BaseAdapter {
    private List<MainDramaData> mainDramaDatas;
    private LayoutInflater inflater;
    private Context context;

    public List<MainDramaData> getMainDramaDatas() {
        return mainDramaDatas;
    }

    public void setMainDramaDatas(List<MainDramaData> mainDramaDatas) {
        this.mainDramaDatas = mainDramaDatas;
    }

    public MainDramaAdapter(Context context) {
        this.context = context;
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        return mainDramaDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mainDramaDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = 	inflater.inflate(R.layout.drama_type_item, null);
            holder = new ViewHolder();
            holder.mainTypeName = (TextView) convertView.findViewById(R.id.typeName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MainDramaData oneData = mainDramaDatas.get(position);
        holder.mainTypeName.setText(oneData.getTypeName());

        return convertView;
    }

    public final class ViewHolder {
        public TextView mainTypeName;
    }
}
