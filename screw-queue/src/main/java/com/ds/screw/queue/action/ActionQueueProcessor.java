package com.ds.screw.queue.action;

import java.util.List;

interface ActionQueueProcessor<T> {
    void processData(List<T> list);
}
