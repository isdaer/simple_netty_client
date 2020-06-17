package com.zeaho.TCP.domain.protocol;

import com.zeaho.TCP.domain.model.MachineDataRealTime;
import com.zeaho.TCP.domain.model.MachineLastLocation;
import com.zeaho.TCP.domain.repo.MachineDataRealTimeRepo;
import com.zeaho.TCP.domain.repo.MachineLastLocationRepo;
import com.zeaho.TCP.utils.BytesUtil;
import com.zeaho.TCP.utils.CountFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//核心业务协议类
//返回ArrayList<Byte>类型的最终传入数据
@Component
public class TCPBytes {

    private static TCPBytes tcpBytes;

    @Autowired
    private MachineDataRealTimeRepo machineDataRealTimeRepo;

    @Autowired
    private MachineLastLocationRepo machineLastLocationRepo;

    @PostConstruct
    public void init() {
        tcpBytes = this;
        tcpBytes.machineDataRealTimeRepo = this.machineDataRealTimeRepo;
        tcpBytes.machineLastLocationRepo = this.machineLastLocationRepo;
    }

    public ArrayList<Byte> JointBytes(String machineCode, Long machineId) {
        ArrayList<Byte> bytes = new ArrayList<>();
        MachineDataRealTime mdtr = tcpBytes.machineDataRealTimeRepo.findByMachineId(machineId);
        if (mdtr == null) {//没有实时状态,不拼接
            return null;
        }

        //起始符
        //固定
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
        addBytes.add((byte) ca.get(Calendar.YEAR));//年
        addBytes.add((byte) (ca.get(Calendar.MONTH) + 1));//月
        addBytes.add((byte) ca.get(Calendar.DAY_OF_MONTH));//日
        addBytes.add((byte) ca.get(Calendar.HOUR));//时
        addBytes.add((byte) ca.get(Calendar.MINUTE));//分
        addBytes.add((byte) ca.get(Calendar.SECOND));//秒

        //信息类型标志
        //0x02国四及以上,0x87国三及以下
        addBytes.add((byte) 0x87);//国三及以下数据流

        //信息流水号,以天为单位,每次加1
        int count = CountFileUtil.updateCountFile();
        System.out.println("count:" + count);
        addBytes.add((byte) 0);
        addBytes.add((byte) 17);

        /**------------信息体------------*/
        //油箱液位,百分比
        System.out.println(mdtr.getFuelPercentage() * 100);
        int fuelPercentage = (int) (mdtr.getFuelPercentage() * 100);//油量百分比
        addBytes.add((byte) fuelPercentage);

        //定位状态
        boolean locationState = false;
        MachineLastLocation mll = tcpBytes.machineLastLocationRepo.findByMachineId(machineId);
        if (mll != null) {
            locationState = true;
            Float longitude = (float) mll.getLongitude();//精度
            Float latitude = (float) mll.getLatitude();//纬度
        }
        //定位状态
        addBytes.add((byte) 0);

        //精度
        addBytes.add((byte) 0x42);
        addBytes.add((byte) 0xF3);
        addBytes.add((byte) 0x0C);
        addBytes.add((byte) 0x4A);
        //纬度
        addBytes.add((byte) 0x41);
        addBytes.add((byte) 0xF7);
        addBytes.add((byte) 0xfb);
        addBytes.add((byte) 0x0B);

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
