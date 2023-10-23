package com.chowbus.qa.step;


import com.chowbus.qa.handler.*;

public class HttpStep extends BaseStep {

  @Override
  public void exec() {



    Handler.Builder builder = new Handler.Builder();
    builder.addHandler(new CaseWaitHandler())
            .addHandler(new HttpRequestHandler())
            .addHandler(new ResponseValidateHandler())
            .addHandler(new ResponseParamHandler());

    builder.build().doHandle(getWorkflowStep(), testContext);

  }
}
