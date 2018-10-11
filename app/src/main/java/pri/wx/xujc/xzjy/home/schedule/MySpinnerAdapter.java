package pri.wx.xujc.xzjy.home.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import pri.wx.xujc.xzjy.R;
import pri.wx.xujc.xzjy.data.model.Term;

import java.util.List;

public class MySpinnerAdapter extends BaseAdapter {

    private List<Term> mList;
    private Context mContext;

    public MySpinnerAdapter(Context mContext, List<Term> mList) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);
        convertView = _LayoutInflater.inflate(R.layout.item_spinner, null);
        if(convertView!=null){
            TextView item = convertView.findViewById(R.id.tv_spinner_item);
            item.setText(mList.get(position).getTmMc());
        }
        return convertView;
    }
}
