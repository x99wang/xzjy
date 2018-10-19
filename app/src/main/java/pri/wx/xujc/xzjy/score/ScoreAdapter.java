package pri.wx.xujc.xzjy.score;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import pri.wx.xujc.xzjy.R;
import pri.wx.xujc.xzjy.data.model.Score;

import java.util.List;

class ScoreAdapter extends BaseAdapter {
    private static final String TAG = "ScoreAdapter";

    private Context mContext;

    private List<Score> mList;

    ScoreAdapter(Context mContext, List<Score> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Score getItem(int position) {
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

        Score s = mList.get(position);

        kcmc.setText(s.getKcMc());
        xf.setText(String.valueOf(s.getKcXf()));
        score.setText(String.valueOf(s.getZcj()));

        if(s.getZcj()<60)
            score.setTextColor(Color.RED);

        return convertView;
    }
}
