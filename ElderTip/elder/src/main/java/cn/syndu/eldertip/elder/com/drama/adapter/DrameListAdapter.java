package cn.syndu.eldertip.elder.com.drama.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.syndu.eldertip.elder.R;
import cn.syndu.eldertip.elder.com.drama.domain.DramaData;

/**
 * Created by Boria on 2015/12/14.
 */
public class DrameListAdapter extends BaseAdapter {

    private List<DramaData> dramaDatas;
    private LayoutInflater inflater;
    private Context context;

    public DrameListAdapter(Context context) {
        this.context = context;
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }
    }

    public List<DramaData> getMainDramaDatas() {
        return dramaDatas;
    }

    public void setMainDramaDatas(List<DramaData> dramaDatas) {
        this.dramaDatas = dramaDatas;
    }

    @Override
    public int getCount() {
        return dramaDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return dramaDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = 	inflater.inflate(R.layout.drama_drama_item, null);
            holder = new ViewHolder();
            holder.dramaName = (TextView) convertView.findViewById(R.id.dramaName);
            holder.dramaImage = (ImageView) convertView.findViewById(R.id.dramaImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
    }

        holder.dramaName.setText(dramaDatas.get(position).getDramaName());
        return convertView;
    }

    public final class ViewHolder {
        public TextView dramaName;
        public ImageView dramaImage;
    }
}
