package com.fantasy.sylvanas.web;

import com.fantasy.sylvanas.service.pipeline.PipeInput;
import com.fantasy.sylvanas.service.pipeline.PipeLine;
import com.fantasy.sylvanas.service.pipeline.PipeOutput;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by jiaji on 2017/5/12.
 */
@Controller
public class PipeLineController {
    @Resource
    private PipeLine myPipe;

    @RequestMapping("/pipeline")
    @ResponseBody
    public String pipeline() {
        PipeInput pipeInput = new PipeInput();
        PipeOutput pipeOutput = new PipeOutput();
        myPipe.execute(pipeInput, pipeOutput);
        return "done";
    }
}
