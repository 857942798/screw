package com.ds.screw.license.core;

import java.net.InetAddress;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用于获取客户Linux服务器的基本信息
 *
 * @author dongsheng
 */
public class MacServerData extends AbstractServerData {

    @Override
    protected List<String> getIpAddress() throws Exception {
        List<String> result = null;
        //获取所有网络接口
        List<InetAddress> inetAddresses = getLocalAllInetAddress();
        if (inetAddresses != null && !inetAddresses.isEmpty()) {
            result = inetAddresses.stream().map(InetAddress::getHostAddress).distinct().map(String::toLowerCase).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    protected List<String> getMacAddress() throws Exception {
        List<String> result = null;
        //1. 获取所有网络接口
        List<InetAddress> inetAddresses = getLocalAllInetAddress();
        if (inetAddresses != null && !inetAddresses.isEmpty()) {
            //2. 获取所有网络接口的Mac地址
            result = inetAddresses.stream().map(this::getMacByInetAddress).distinct().collect(Collectors.toList());
        }
        return result;
    }

    @Override
    protected String getCPUSerial() throws Exception {
        return "";
    }

    @Override
    protected String getMainBoardSerial() throws Exception {
        return "";
    }
}
