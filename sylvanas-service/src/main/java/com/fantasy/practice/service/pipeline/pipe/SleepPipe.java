package com.fantasy.sylvanas.service.pipeline.pipe;

import com.fantasy.sylvanas.service.pipeline.PipeInput;
import com.fantasy.sylvanas.service.pipeline.PipeOutput;
import com.fantasy.sylvanas.service.pipeline.SyncPipe;

/**
 * Created by jiaji on 2017/5/12.
 */
public class SleepPipe extends SyncPipe {
    @Override
    public void doPipe(PipeInput pipeInput, PipeOutput pipeOutput) {
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
