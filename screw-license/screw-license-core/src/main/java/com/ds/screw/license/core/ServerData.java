package com.ds.screw.license.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServerData implements Serializable {

    private static final long serialVersionUID = 2770109886058285951L;
    /**
     * 可被允许的IP地址
     */
    private List<String> ipAddress = new ArrayList<>();

    /**
     * 可被允许的MAC地址
     */
    private List<String> macAddress = new ArrayList<>();

    /**
     * 可被允许的CPU序列号
     */
    private String cpuSerial = "";

    /**
     * 可被允许的主板序列号
     */
    private String mainBoardSerial = "";

    public ServerData() {
    }

    public ServerData(List<String> ipAddress, List<String> macAddress, String cpuSerial, String mainBoardSerial) {
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.cpuSerial = cpuSerial;
        this.mainBoardSerial = mainBoardSerial;
    }

    public List<String> getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(List<String> ipAddress) {
        this.ipAddress = ipAddress;
    }

    public List<String> getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(List<String> macAddress) {
        this.macAddress = macAddress;
    }

    public String getCpuSerial() {
        return cpuSerial;
    }

    public void setCpuSerial(String cpuSerial) {
        this.cpuSerial = cpuSerial;
    }

    public String getMainBoardSerial() {
        return mainBoardSerial;
    }

    public void setMainBoardSerial(String mainBoardSerial) {
        this.mainBoardSerial = mainBoardSerial;
    }

    public static ServerDataBuilder builder() {
        return new ServerDataBuilder();
    }

    public static final class ServerDataBuilder {
        private List<String> ipAddress = new ArrayList<>();
        private List<String> macAddress = new ArrayList<>();
        private String cpuSerial;
        private String mainBoardSerial;


        private ServerDataBuilder() {

        }

        public ServerDataBuilder withIpAddress(List<String> ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public ServerDataBuilder addIpAddress(String ipAddress) {
            if (this.ipAddress == null) {
                this.ipAddress = new ArrayList<>();
            }
            this.ipAddress.add(ipAddress);
            return this;
        }

        public ServerDataBuilder withMacAddress(List<String> macAddress) {
            this.macAddress = macAddress;
            return this;
        }

        public ServerDataBuilder addMacAddress(String macAddress) {
            if (this.macAddress == null) {
                this.macAddress = new ArrayList<>();
            }
            this.macAddress.add(macAddress);
            return this;
        }

        public ServerDataBuilder withCpuSerial(String cpuSerial) {
            this.cpuSerial = cpuSerial;
            return this;
        }

        public ServerDataBuilder withMainBoardSerial(String mainBoardSerial) {
            this.mainBoardSerial = mainBoardSerial;
            return this;
        }

        public ServerData build() {
            return new ServerData(ipAddress, macAddress, cpuSerial, mainBoardSerial);
        }
    }

    @Override
    public String toString() {
        return "ServerData{" +
                "ipAddress=" + ipAddress +
                ", macAddress=" + macAddress +
                ", cpuSerial='" + cpuSerial + '\'' +
                ", mainBoardSerial='" + mainBoardSerial + '\'' +
                '}';
    }
}
