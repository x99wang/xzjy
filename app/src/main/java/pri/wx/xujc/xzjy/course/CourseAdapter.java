package pri.wx.xujc.xzjy.course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import pri.wx.xujc.xzjy.R;
import pri.wx.xujc.xzjy.data.model.CourseEntity;

import java.util.List;

class CourseAdapter extends BaseAdapter {
    private static final String TAG = "CourseAdapter";

    private Context mContext;

    private List<CourseEntity> mList;

    CourseAdapter(Context mContext, List<CourseEntity> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CourseEntity getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_score, null);
        }
        TextView kcmc = convertView.findViewById(R.id.tv_kcmc);
        TextView xf = convertView.findViewById(R.id.tv_xf);
        TextView score = convertView.findViewById(R.id.tv_score);

        CourseEntity s = mList.get(position);

        kcmc.setText(s.getKcMc());
        xf.setText(String.valueOf(s.getKcXf()));

        return convertView;
    }
}
