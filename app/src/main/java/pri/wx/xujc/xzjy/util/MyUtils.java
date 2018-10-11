package pri.wx.xujc.xzjy.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyUtils {

    public static String getNowTerm(){
        /*
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH )+1;
        */
        return "20181";
    }

    public static String getNowDate(){
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy年MM月dd日");
        return ft.format(dNow);
    }

    public static int getXq(String str){
        if("周一".equals(str)){
            return 1;
        } else if("周二".equals(str)){
            return 2;
        } else if("周三".equals(str)){
            return 3;
        } else if("周四".equals(str)){
            return 4;
        } else if("周五".equals(str)){
            return 5;
        } else if("周六".equals(str)){
            return 6;
        } else if("周日".equals(str)){
            return 7;
        } else{
            return -1;
        }

    }
}
