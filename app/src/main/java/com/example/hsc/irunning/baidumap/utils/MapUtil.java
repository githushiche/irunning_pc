package com.example.hsc.irunning.baidumap.utils;

import android.annotation.TargetApi;
import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.Poi;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.TraceLocation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 地图帮助类
 *
 * @author Diviner
 * @date 2018-4-6 下午6:38:29
 */
public class MapUtil {

    /**
     * 经纬度是否为(0,0)点
     *
     * @return
     */
    public static boolean isZeroPoint(double latitude, double longitude) {
        return isEqualToZero(latitude) && isEqualToZero(longitude);
    }

    /**
     * 校验double数值是否为0
     *
     * @param value
     * @return
     */
    public static boolean isEqualToZero(double value) {
        return Math.abs(value - 0.0) < 0.01 ? true : false;
    }

    /**
     * 根据两点算取图标转的角度
     */
    public static double getAngle(LatLng fromPoint, LatLng toPoint) {
        double slope = getSlope(fromPoint, toPoint);
        if (slope == Double.MAX_VALUE) {
            if (toPoint.latitude > fromPoint.latitude) {
                return 0;
            } else {
                return 180;
            }
        }
        float deltAngle = 0;
        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
            deltAngle = 180;
        }
        double radio = Math.atan(slope);
        return 180 * (radio / Math.PI) + deltAngle - 90;
    }

    /**
     * 算斜率
     */
    public static double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        return (toPoint.latitude - fromPoint.latitude)
                / (toPoint.longitude - fromPoint.longitude);
    }

    /**
     * 地图配置信息
     *
     * @return LocationClientOption
     */
    public static LocationClientOption settingLocationOptin() {
        // 一般为驾车才设置这个配置
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");// 定义当前坐标类型
        option.setIsNeedAddress(true);// 如果要使用POI此处必须为true(打开)
        option.setIsNeedAltitude(true);// 设置是否需要返回海拔高度信息
        option.setIsNeedLocationPoiList(true);// 设置是否需要返回位置POI信息
        option.setLocationMode(LocationMode.Hight_Accuracy);// 高精度模式
        option.setOpenGps(true);// 打开GPS定位
        option.setScanSpan(1000);// eg:2000毫秒--2秒

        return option;
    }

    /**
     * 纠偏选项配置
     *
     * @return ProcessOption
     */
    //	public static ProcessOption settingProcessOption() {
    //		// 创建纠偏选项实例
    //		ProcessOption processOption = new ProcessOption();// 纠正需要设置的参数
    //		processOption.setNeedDenoise(true);// 设置需要去噪
    //		processOption.setNeedVacuate(true);// 设置需要抽稀
    //		// processOption.setNeedMapMatch(true);// 设置需要绑路
    //		processOption.setRadiusThreshold(50);// 设置精度过滤值(定位精度大于50米的过滤掉)
    //		processOption.setTransportMode(TransportMode.walking);// 设置为步行
    //
    //		return processOption;
    //	}

    /**
     * 获取定位返回的结果
     *
     * @param location
     * @return
     */
    public static String getLocationInformation(BDLocation location) {
        // 获取定位结果
        StringBuffer sb = new StringBuffer(256);

        sb.append("定位时间: ");
        sb.append(location.getTime()); // 获取定位时间

        sb.append("\n类型: ");
        sb.append(location.getLocType()); // 获取类型类型

        sb.append("\n纬度: ");
        sb.append(location.getLatitude()); // 获取纬度信息

        sb.append("\n经度: ");
        sb.append(location.getLongitude()); // 获取经度信息

        sb.append("\n定位精度: ");
        sb.append(location.getRadius()); // 获取定位精准度

        if (location.getLocType() == location.TypeGpsLocation) {

            // GPS定位结果
            sb.append("\n公里每小时: ");
            sb.append(location.getSpeed()); // 单位：公里每小时

            sb.append("\n卫星数: ");
            sb.append(location.getSatelliteNumber()); // 获取卫星数

            sb.append("\n海拔高度: ");
            sb.append(location.getAltitude()); // 获取海拔高度信息，单位米

            sb.append("\n方向: ");
            sb.append(location.getDirection()); // 获取方向信息，单位度

            sb.append("\n地址: ");
            sb.append(location.getAddrStr()); // 获取地址信息

            sb.append("\n定位是否成功: ");
            sb.append("gps定位成功");

        } else if (location.getLocType() == location.TypeNetWorkLocation) {

            // 网络定位结果
            sb.append("\n地址: ");
            sb.append(location.getAddrStr()); // 获取地址信息

            sb.append("\n运营商: ");
            sb.append(location.getOperators()); // 获取运营商信息

            sb.append("\n是否定位成功: ");
            sb.append("网络定位成功");

        } else if (location.getLocType() == location.TypeServerError) {

            sb.append("\n是否定位成功: ");
            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，");

        } else if (location.getLocType() == location.TypeNetWorkException) {

            sb.append("\n是否定位成功: ");
            sb.append("网络不同导致定位失败，请检查网络是否通畅");

        } else if (location.getLocType() == location.TypeCriteriaException) {

            sb.append("\n是否定位成功: ");
            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，"
                    + "处于飞行模式下一般会造成这种结果，可以试着重启手机");
        }
        sb.append("\nlocationdescribe : ");
        sb.append(location.getLocationDescribe()); // 位置语义化信息

        List<Poi> list = location.getPoiList(); // POI数据
        if (list != null) {
            sb.append("\nPOI长度 = : ");
            sb.append(list.size());
            for (Poi p : list) {
                sb.append("\nPOI信息= : ");
                sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
            }
        }
        return sb.toString();
    }

    /**
     * 将轨迹实时定位点转换为地图坐标
     */
    public static LatLng convertTraceLocation2Map(TraceLocation location) {
        if (null == location) {
            return null;
        }
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        if (Math.abs(latitude - 0.0) < 0.000001
                && Math.abs(longitude - 0.0) < 0.000001) {
            return null;
        }
        LatLng currentLatLng = new LatLng(latitude, longitude);
        if (CoordType.wgs84 == location.getCoordType()) {
            LatLng sourceLatLng = currentLatLng;
            CoordinateConverter converter = new CoordinateConverter();
            converter.from(CoordinateConverter.CoordType.GPS);
            converter.coord(sourceLatLng);
            currentLatLng = converter.convert();
        }
        return currentLatLng;
    }

    /**
     * 将轨迹坐标对象转换为地图坐标对象
     */
    public static LatLng convertTrace2Map(com.baidu.trace.model.LatLng traceLatLng) {
        return new LatLng(traceLatLng.latitude, traceLatLng.longitude);
    }

    @TargetApi(19 - 26)
    public static List<Map<String, String>> addressResolution(String address) {
        /*
         * java.util.regex是一个用正则表达式所订制的模式来对字符串进行匹配工作的类库包。它包括两个类：Pattern和Matcher Pattern
         * 一个Pattern是一个正则表达式经编译后的表现模式。 Matcher
         * 一个Matcher对象是一个状态机器，它依据Pattern对象做为匹配模式对字符串展开匹配检查。
         * 首先一个Pattern实例订制了一个所用语法与PERL的类似的正则表达式经编译后的模式，
         * 然后一个Matcher实例在这个给定的Pattern实例的模式控制下进行字符串的匹配工作。
         */
        String regex = "(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
        Matcher m = Pattern.compile(regex).matcher(address);
        String province = null, city = null, county = null, town = null, village = null;
        List<Map<String, String>> table = new ArrayList<Map<String, String>>();
        Map<String, String> row = null;
        while (m.find()) {
            row = new LinkedHashMap<String, String>();
            province = m.group("province");
            row.put("province", province == null ? "" : province.trim());

            city = m.group("city");
            row.put("city", city == null ? "" : city.trim());

            county = m.group("county");
            row.put("county", county == null ? "" : county.trim());

            town = m.group("town");
            row.put("town", town == null ? "" : town.trim());

            village = m.group("village");
            row.put("village", village == null ? "" : village.trim());

            table.add(row);
        }
        return table;
    }

    /**
     * 计算两点距离和所需时间
     *
     * @param start
     * @param end
     * @return
     */
    public static Map<String, String> getDistance(LatLng start, LatLng end) {
        double lat1 = (Math.PI / 180) * start.latitude;
        double lat2 = (Math.PI / 180) * end.latitude;

        double lon1 = (Math.PI / 180) * start.longitude;
        double lon2 = (Math.PI / 180) * end.longitude;

        // 地球半径
        double R = 6371;

        // 两点间距离 km，如果想要米的话，结果*1000
        double dis = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1)
                * Math.cos(lat2) * Math.cos(lon2 - lon1))
                * R;

        Map<String, String> map = new HashMap<>();
        NumberFormat nFormat = NumberFormat.getNumberInstance(); //数字格式化对象
        if (dis < 1) { //当小于1千米的时候用,用米做单位保留一位小数
            nFormat.setMaximumFractionDigits(1); //已可以设置为0，这样跟百度地图APP中计算的一样
            dis *= 1000;
            map.put("mileage", nFormat.format(dis) + "m");

            /*
             * 假设默认速度为5.5km/h(走路),时间=路程/速度
             */
            double kmTime = dis / (5.5 * 1000);// 计算所需时间
            double needTime = kmTime * 60;
            needTime = (double) Math.round(needTime * 100) / 100;

            String str = String.valueOf(needTime);
            String time[] = str.split("\\.");
            map.put("needTime", time[0]);
        } else {
            nFormat.setMaximumFractionDigits(2);
            map.put("mileage", nFormat.format(dis) + "km");

            /*
             * 假设默认速度为5.5km/h(走路)
             */
            double kmTime = (dis * 1000) / (5.5 * 1000);// 计算所需时间
            double needTime = kmTime * 60;
            needTime = (double) Math.round(needTime * 100) / 100;

            String str = String.valueOf(needTime);
            String time[] = str.split("\\.");
            map.put("needTime", time[0]);
        }
        return map;
    }

    /**
     * 计算里程所需时间
     *
     * @param distance
     * @return needTime
     */
    public static String distanceNeedTime(double distance) {
        /*
         * 假设默认速度为3.5km/h(走路)
         */
        double kmTime = distance / 3.5;// 计算所需事件
        double needTime = kmTime * 60;
        needTime = (double) Math.round(needTime * 100) / 100;

        String str = String.valueOf(needTime);
        String time[] = str.split("\\.");

        return time[0];
    }

    /**
     * 计算卡路里
     *
     * @param weight 体重
     * @param steps  步数
     * @return
     */
    public static int CalculationCalories(double weight, double steps) {
        DecimalFormat df = new DecimalFormat("#.##");// 时间格式化
        double totalMeters = steps * 0.7;
        // 保留两位有效数字
        double lc = Double.parseDouble(df.format(totalMeters / 1000));

        // 跑步热量（kcal）＝体重（kg）×距离（公里）×1.036
        int kll = (int) (weight * lc * 1.036);
        return kll;
    }

    /**
     * 复制和加载区域数据库中的数据
     *
     * @param context
     * @param SqliteFileName
     * @return
     * @throws IOException
     */
    public static String CopySqliteFileFromRawToDatabases(Context context,
                                                          String SqliteFileName) throws IOException {

        // 第一次运行应用程序时，加载数据库到data/data/当前包的名称/database/<db_name>

        File dir = new File("data/data/" + context.getPackageName()
                + "/databases");

        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }

        File file = new File(dir, SqliteFileName);
        InputStream inputStream = null;
        OutputStream outputStream = null;

        // 通过IO流的方式，将assets目录下的数据库文件，写入到SD卡中。
        if (!file.exists()) {
            try {
                file.createNewFile();

                inputStream = context.getClass().getClassLoader()
                        .getResourceAsStream("assets/" + SqliteFileName);
                outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int len;

                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
        return file.getPath();

    }
}
