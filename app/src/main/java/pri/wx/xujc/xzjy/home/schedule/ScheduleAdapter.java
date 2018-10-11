package pri.wx.xujc.xzjy.home.schedule;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import pri.wx.xujc.xzjy.R;
import pri.wx.xujc.xzjy.data.model.Course;

import java.util.List;

public class ScheduleAdapter extends BaseAdapter {
    private Context mContext;
    //保存内容的内部数组
    private List<Course> content;
    public ScheduleAdapter(Context context, List<Course> list) {
        this.mContext = context;
        this.content = list;
    }
    public int getCount() {
        return content.size();
    }
    public Object getItem(int position) {
        return content.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_schedule, null);
        }
        LinearLayout layout = convertView.findViewById(R.id.class_item);
        TextView name = convertView.findViewById(R.id.class_name);
        TextView room = convertView.findViewById(R.id.class_room);
        //如果有课,那么添加数据
        if (null != getItem(position)) {
            name.setText(((Course) getItem(position)).getName());
            name.setTextColor(Color.WHITE);
            room.setText(((Course) getItem(position)).getRoom());
            room.setTextColor(Color.WHITE);
            //变换颜色
            int rand = position % 7;
            switch (rand) {
                case 0:
                    layout.setBackgroundResource(R.drawable.item_bg_s1);
                    break;
                case 1:
                    layout.setBackgroundResource(R.drawable.item_bg_s2);
                    break;
                case 2:
                    layout.setBackgroundResource(R.drawable.item_bg_s3);
                    break;
                case 3:
                    layout.setBackgroundResource(R.drawable.item_bg_s4);
                    break;
                case 4:
                    layout.setBackgroundResource(R.drawable.item_bg_s5);
                    break;
                case 5:
                    layout.setBackgroundResource(R.drawable.item_bg_s6);
                    break;
                case 6:
                    layout.setBackgroundResource(R.drawable.item_bg_s7);
                    break;
            }
        }
        return convertView;
    }
}