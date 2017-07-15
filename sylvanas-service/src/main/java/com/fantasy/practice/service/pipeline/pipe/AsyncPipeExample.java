package com.fantasy.sylvanas.service.pipeline.pipe;

import com.fantasy.sylvanas.service.pipeline.AsyncPipe;
import com.fantasy.sylvanas.service.pipeline.IPipeIterator;
import com.fantasy.sylvanas.service.pipeline.PipeInput;
import com.fantasy.sylvanas.service.pipeline.PipeOutput;

/**
 * Created by jiaji on 16/12/27.
 */
public class AsyncPipeExample extends AsyncPipe {

    @Override
    public void doPipe(PipeInput pipeInput, PipeOutput pipeOutput, IPipeIterator pipeIterator) {
        //do some biz async
        System.out.println("async biz pipe");
        new Thread(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        pipeIterator.next();
    }
}
