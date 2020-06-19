package com.zeaho.TCP.domain.protocol;

import com.zeaho.TCP.domain.model.MachineDataRealTime;
import com.zeaho.TCP.domain.model.MachineLastLocation;
import com.zeaho.TCP.domain.repo.MachineDataRealTimeRepo;
import com.zeaho.TCP.domain.repo.MachineLastLocationRepo;
import com.zeaho.TCP.utils.BytesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//核心业务协议类
//返回ArrayList<Byte>类型的最终传入数据
@Component
public class TCPBytes {

    private static TCPBytes tcpBytes;

    @Autowired
    private MachineDataRealTimeRepo machineDataRealTimeRepo;

    @Autowired
    private MachineLastLocationRepo machineLastLocationRepo;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostConstruct
    public void init() {
        tcpBytes = this;
        tcpBytes.machineDataRealTimeRepo = this.machineDataRealTimeRepo;
        tcpBytes.machineLastLocationRepo = this.machineLastLocationRepo;
        tcpBytes.stringRedisTemplate = stringRedisTemplate;
    }

    public ArrayList<Byte> JointBytes(String machineCode, Long machineId) {
        ArrayList<Byte> bytes = new ArrayList<>();
        MachineDataRealTime mdtr = tcpBytes.machineDataRealTimeRepo.findByMachineId(machineId);
        if (mdtr == null) {//没有实时状态,不拼接
            return null;
        }

        //固定起始符
        String header = "##";
        BytesUtil.stringIntoBytes(bytes, header);

        //命令单元
        //0x01:终端登入,0x02:实时信息上报,0x03:补发信息上报,0x04:终端登出,0x05:终端校时,0x06~0x7F:系统预留
        bytes.add((byte) 0x02);

        //车辆识别号
        //不足17位左补足
        String cs = "";
        int length = machineCode.length();
        if (length < 17) {
            for (int i = 0; i < 17 - length; i++) {
                cs += "0";
            }
            machineCode = cs + machineCode;
        }
        BytesUtil.stringIntoBytes(bytes, machineCode);

        //终端软件版本号,有效范围0~255
        bytes.add((byte) 3);

        //数据加密方式
        //0x01:不加密,0x02:RSA加密,0x03:国密SM2加密,0xFE:异常,0xFF:无效
        bytes.add((byte) 0x01);

        /**----------------------数据单元----------------------*/
        //先拼接数据单元,再计算数据单元长度追加至总数据,再追加数据单元
        ArrayList<Byte> addBytes = new ArrayList<>();
        //数据采集时间
        //获取年月日时分秒
        int stateUpdateTs = mdtr.getStateUpdateTs();
        Date date = new Date(stateUpdateTs * 1000l);
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        int year = Integer.parseInt(new SimpleDateFormat("yy", Locale.CHINESE).format(ca.getTime()));//年取后两位
        addBytes.add((byte) year);//年
        addBytes.add((byte) (ca.get(Calendar.MONTH) + 1));//月
        addBytes.add((byte) ca.get(Calendar.DAY_OF_MONTH));//日
        addBytes.add((byte) ca.get(Calendar.HOUR));//时
        addBytes.add((byte) ca.get(Calendar.MINUTE));//分
        addBytes.add((byte) ca.get(Calendar.SECOND));//秒

        //信息类型标志
        //0x02国四及以上,0x87国三及以下
        addBytes.add((byte) 0x87);//国三及以下数据流

        //信息流水号,以天为单位,每次加1
        int count = 1;
        ValueOperations<String, String> stvos = tcpBytes.stringRedisTemplate.opsForValue();
        String strCount = stvos.get("count");
        if (strCount == null | "0".equals(strCount)) {
            stvos.set("count", "1");
        } else {
            count = Integer.parseInt(strCount);
            stvos.set("count", "" + (count + 1));
        }
        int currentInt = Integer.parseInt(stvos.get("count"));
        int[] ints = BytesUtil.int2bytes(currentInt, 2);
        for (int i = 0; i < 2; i++) {
            addBytes.add((byte) ints[i]);
        }

        /**------------信息体------------*/
        //油箱液位,百分比
        int fuelPercentage = (int) (mdtr.getFuelPercentage() * 100);//油量百分比
        addBytes.add((byte) fuelPercentage);

        //定位状态
        MachineLastLocation mll = tcpBytes.machineLastLocationRepo.findByMachineId(machineId);
        if (mll != null) {
            //定位状态
            addBytes.add((byte) 0);
            Float longitude = (float) mll.getLongitude();//精度
            Float latitude = (float) mll.getLatitude();//纬度
            int intLon = (int) (longitude * 100000);
            int intLat = (int) (latitude * 100000);
            int[] lonBytes = BytesUtil.int2bytes(intLon, 4);
            int[] latBytes = BytesUtil.int2bytes(intLat, 4);
            //byte[] longitudeBytes = BytesUtil.float2byte(longitude);
            for (int lon : lonBytes) {
                addBytes.add((byte) lon);
            }
            // byte[] latitudeBytes = BytesUtil.float2byte(latitude);
            for (int lat : latBytes) {
                addBytes.add((byte) lat);
            }

        } else {
            //定位状态
            addBytes.add((byte) 1);

            //精度
            addBytes.add((byte) 0x0);
            addBytes.add((byte) 0x0);
            addBytes.add((byte) 0x0);
            addBytes.add((byte) 0x0);
            //纬度
            addBytes.add((byte) 0x0);
            addBytes.add((byte) 0x0);
            addBytes.add((byte) 0x0);
            addBytes.add((byte) 0x0);
        }


        //工作状态
        //0x00:停止,0x01:怠速,0x02:工作,0x03:行驶,0x37:行程开始,0x38:行程结果
        String state = mdtr.getState();//状态
        switch (state) {
            case "working":
                addBytes.add((byte) 0x02);
                break;
            case "idle":
                addBytes.add((byte) 0x01);
            default:
                addBytes.add((byte) 0x00);
                break;
        }

        /**------------信息体------------*/
        /**----------------------拼接数据----------------------*/

        //计算数据长度
        int addBytesSize = addBytes.size();

        //追加数据长度
        bytes.add((byte) 0);
        bytes.add((byte) addBytesSize);

        //追加数据
        for (byte b : addBytes) {
            bytes.add(b);
        }

        return bytes;
    }
}
