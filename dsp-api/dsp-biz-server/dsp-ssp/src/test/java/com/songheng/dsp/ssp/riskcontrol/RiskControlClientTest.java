package com.songheng.dsp.ssp.riskcontrol;

import com.songheng.dsp.common.InitLoadConf;
import com.songheng.dsp.common.enums.ProjectEnum;
import com.songheng.dsp.model.flow.BaseFlow;
import com.songheng.dsp.ssp.riskcontrol.riskchain.impl.BlackListControl;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;



/**
 *风控测试类
 **/
public class RiskControlClientTest {


    @BeforeClass
    public static void initLoad(){
        InitLoadConf.init(ProjectEnum.H5);
    }

    BaseFlow baseFlow = new BaseFlow();

    @Before
    public void setUp()  {
        baseFlow.setTestFlow(false);
        baseFlow.setBrushFlow(false);
        baseFlow.setTerminal("pc");
    }

    @After
    public void tearDown() {
    }

}