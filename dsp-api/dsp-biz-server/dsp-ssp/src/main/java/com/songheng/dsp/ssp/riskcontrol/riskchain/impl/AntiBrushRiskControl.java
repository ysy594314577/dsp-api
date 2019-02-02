package com.songheng.dsp.ssp.riskcontrol.riskchain.impl;

import com.songheng.dsp.model.flow.BaseFlow;
import com.songheng.dsp.ssp.riskcontrol.RiskControlResult;
import com.songheng.dsp.ssp.riskcontrol.riskchain.RiskControl;

/**
 * @description: 防刷验证
 * @author: zhangshuai@021.com
 * @date: 2019-02-01 10:05
 **/
public class AntiBrushRiskControl extends RiskControl{
    /**
     *重写风控验证失败的原因
     **/
    @Override
    protected RiskControlResult getFailResult(BaseFlow baseFlow) {
        return new RiskControlResult(false,"brushFlow","刷量流量",baseFlow);
    }
    @Override
    protected RiskControlResult doVerification(BaseFlow baseFlow) {
        if(baseFlow.isBrushFlow()){
            return getFailResult(baseFlow);
        }else{
            return getSuccessResult(baseFlow);
        }
    }
}