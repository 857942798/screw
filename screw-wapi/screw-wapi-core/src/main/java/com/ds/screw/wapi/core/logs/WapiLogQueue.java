package com.ds.screw.wapi.core.logs;

import java.util.concurrent.LinkedBlockingQueue;


public class WapiLogQueue extends LinkedBlockingQueue<WapiLog> {

    private static final long serialVersionUID = 0x012323221900001L;

    public WapiLogQueue() {
        super(100000);
    }

}
