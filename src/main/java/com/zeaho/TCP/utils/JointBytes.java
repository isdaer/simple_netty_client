package com.zeaho.TCP.utils;

import com.zeaho.TCP.domain.model.MachineDataRealTime;
import com.zeaho.TCP.domain.model.MachineLastLocation;
import com.zeaho.TCP.domain.model.OpenApiShhkMachine;
import com.zeaho.TCP.domain.repo.MachineDataRealTimeRepo;
import com.zeaho.TCP.domain.repo.MachineLastLocationRepo;
import com.zeaho.TCP.domain.repo.OpenApiShhkMachineRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//核心业务工具类
//返回ArrayList<Byte>类型的最终传入数据
//工具类调用repo
@Component
public class JointBytes {

    @Autowired
    private OpenApiShhkMachineRepo openApiShhkMachineRepo;

    @Autowired
    private MachineDataRealTimeRepo machineDataRealTimeRepo;

    @Autowired
    private MachineLastLocationRepo machineLastLocationRepo;

    private static JointBytes jointBytes;

    @PostConstruct
    public void init() {
        jointBytes = this;
        jointBytes.openApiShhkMachineRepo = this.openApiShhkMachineRepo;
        jointBytes.machineDataRealTimeRepo = this.machineDataRealTimeRepo;
        jointBytes.machineLastLocationRepo = this.machineLastLocationRepo;
    }

    public ArrayList<Byte> JointBytes() {
        ArrayList<Byte> bytes = new ArrayList<>();
        List<OpenApiShhkMachine> list = jointBytes.openApiShhkMachineRepo.findAll();

        for (OpenApiShhkMachine oasm : list) {
            String machineCode = oasm.getMachineCode();
            Long machineId = oasm.getMachineId();
            if (!"".equals(machineCode) && machineCode != null
                    && !"".equals(machineId) && machineId != null) {//设备编号和机械id不为空
                bytes = eachByte(bytes, machineCode, machineId);
            }

        }
        return bytes;
    }

    private ArrayList<Byte> eachByte(ArrayList<Byte> bytes, String machineCode, Long machineId) {
        MachineDataRealTime mdtr = jointBytes.machineDataRealTimeRepo.findByMachineId(machineId);
        if (mdtr == null) {//没有实时状态,不拼接
            return bytes;
        }

        String state = mdtr.getState();//状态
        int fuelVolume = (int) mdtr.getFuelVolume();//油量

        //获取年月日时分秒
        int stateUpdateTs = mdtr.getStateUpdateTs();
        System.out.println(stateUpdateTs);
        Date date = new Date(stateUpdateTs * 1000l);
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int day = ca.get(Calendar.DAY_OF_MONTH);
        int hour = ca.get(Calendar.HOUR);
        int minute = ca.get(Calendar.MINUTE);
        int second = ca.get(Calendar.SECOND);

        Boolean locationState = false;
        MachineLastLocation mll = jointBytes.machineLastLocationRepo.findByMachineId(machineId);
        if (mll != null) {
            locationState = true;
            Double longitude = mll.getLongitude();//精度
            Double latitude = mll.getLatitude();//纬度
        }

        //起始符
        //固定
        String header = "##";
        StringIntoBytes.intoBytes(bytes, header);

        //命令单元
        //0x01:终端登入,0x02:实时信息上报,0x03:补发信息上报,0x04:终端登出,0x05:终端校时,0x06~0x7F:系统预留
        bytes.add((byte) 0x02);

        //车辆识别号
        StringIntoBytes.intoBytes(bytes, machineCode);

        //终端软件版本号,有效范围0~255
        bytes.add((byte) 3);

        //数据加密方式
        //0x01:不加密,0x02:RSA加密,0x03:国密SM2加密,0xFE:异常,0xFF:无效
        bytes.add((byte) 0x01);

        /**----------------------数据单元----------------------*/
        //先拼接数据单元,再计算数据单元长度追加至总数据,再追加数据单元
        ArrayList<Byte> addBytes = new ArrayList<>();
        //数据采集时间
        addBytes.add((byte) year);//年
        addBytes.add((byte) month);//月
        addBytes.add((byte) day);//日
        addBytes.add((byte) hour);//时
        addBytes.add((byte) minute);//分
        addBytes.add((byte) second);//秒
        //信息类型标志
        //0x02国四及以上,0x87国三及以下
        addBytes.add((byte) 0x87);//国三及以下数据流
        //信息流水号
        addBytes.add((byte) 0);
        addBytes.add((byte) 1);
        /**------------信息体------------*/
        //油箱液位
        addBytes.add((byte) fuelVolume);
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
        addBytes.add((byte) 0x02);
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
