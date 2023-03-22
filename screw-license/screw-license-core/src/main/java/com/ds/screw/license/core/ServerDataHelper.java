package com.ds.screw.license.core;

import de.schlichtherle.license.LicenseContentException;

import java.util.List;

public class ServerDataHelper {
    ServerDataHelper() {
    }

    private static ServerData serverData;

    public static ServerData getServerData() {
        synchronized (ServerDataHelper.class) {
            if (serverData == null) {
                serverData = getCurrentServerData();
            }
            return serverData;
        }
    }

    /**
     * 获取当前服务器需要额外校验的License参数
     *
     * @return ServerData
     * @author yotasky
     * @date 2022-07-30
     * @since 1.0.0
     */
    private static ServerData getCurrentServerData() {
        //操作系统类型
        String osName = System.getProperty("os.name").toLowerCase();
        AbstractServerData abstractServerData = null;

        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerData = new WindowsServerData();
        } else if (osName.startsWith("linux")) {
            abstractServerData = new LinuxServerData();
        } else if (osName.startsWith("mac")) {
            abstractServerData = new MacServerData();
        } else {//其他服务器类型
            abstractServerData = new LinuxServerData();
        }
        return abstractServerData.getServerData();
    }


    /**
     * 校验当前服务器的IP地址是否在可被允许的IP范围内<br/>
     * 如果存在IP在可被允许的IP地址范围内，则返回true
     *
     * @author yotasky
     * @date 2022-07-30
     * @since 1.0.0
     */
    public static void checkServerData(ServerData data) throws LicenseContentException {
        //校验IP地址
        if (!ServerDataHelper.checkIpAddress(data.getIpAddress())) {
            throw new LicenseContentException("当前服务器的IP没在授权范围内");
        }

        //校验Mac地址
        if (!ServerDataHelper.checkMacAddress(data.getMacAddress())) {
            throw new LicenseContentException("当前服务器的Mac地址没在授权范围内");
        }

        //校验CPU序列号
        if (!ServerDataHelper.checkCpuSerial(data.getCpuSerial())) {
            throw new LicenseContentException("当前服务器的CPU序列号没在授权范围内");
        }

        //校验主板序列号
        if (!ServerDataHelper.checkMainBoardSerial(data.getMainBoardSerial())) {
            throw new LicenseContentException("当前服务器的主板序列号没在授权范围内");
        }
    }


    /**
     * 校验当前服务器的IP/Mac地址是否在可被允许的IP范围内<br/>
     * 如果存在IP在可被允许的IP/Mac地址范围内，则返回true
     *
     * @return boolean
     * @author yotasky
     * @date 2022-07-30
     * @since 1.0.0
     */
    private static boolean checkAddress(List<String> expectedList, List<String> serverList) {
        if (expectedList != null && !expectedList.isEmpty()) {
            if (serverList != null && !serverList.isEmpty()) {
                for (String expected : expectedList) {
                    if (serverList.contains(expected.trim())) {
                        return true;
                    }
                }
            }

            return false;
        } else {
            return true;
        }
    }


    /**
     * 校验当前服务器硬件（主板、CPU等）序列号是否在可允许范围内
     *
     * @return boolean
     * @author yotasky
     * @date 2022-07-30
     * @since 1.0.0
     */
    private static boolean checkSerial(String expectedSerial, String serverSerial) {
        if (expectedSerial != null && !"".equals(expectedSerial)) {
            if (serverSerial != null && !"".equals(serverSerial)) {
                return expectedSerial.equals(serverSerial);
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * 校验当前服务器的IP地址是否在可被允许的IP范围内<br/>
     * 如果存在IP在可被允许的IP地址范围内，则返回true
     *
     * @return boolean
     * @author yotasky
     * @date 2022-07-30
     * @since 1.0.0
     */
    public static boolean checkIpAddress(List<String> expectedList) {
        return checkAddress(expectedList, getServerData().getIpAddress());
    }

    /**
     * 校验当前服务器的Mac地址是否在可被允许的IP范围内<br/>
     * 如果存在IP在可被允许的IP地址范围内，则返回true
     *
     * @return boolean
     * @author yotasky
     * @date 2022-07-30
     * @since 1.0.0
     */
    public static boolean checkMacAddress(List<String> expectedList) {
        return checkAddress(expectedList, getServerData().getMacAddress());
    }

    /**
     * 校验当前服务器硬件（主板）序列号是否在可允许范围内
     *
     * @return boolean
     * @author yotasky
     * @date 2022-07-30
     * @since 1.0.0
     */
    public static boolean checkMainBoardSerial(String expectedList) {
        return checkSerial(expectedList, getServerData().getMainBoardSerial());
    }

    /**
     * 校验当前服务器硬件（CPU等）序列号是否在可允许范围内
     *
     * @return boolean
     * @author yotasky
     * @date 2022-07-30
     * @since 1.0.0
     */
    public static boolean checkCpuSerial(String expectedList) {
        return checkSerial(expectedList, getServerData().getCpuSerial());
    }
}
