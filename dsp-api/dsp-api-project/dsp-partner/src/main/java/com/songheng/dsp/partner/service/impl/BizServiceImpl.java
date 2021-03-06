package com.songheng.dsp.partner.service.impl;

import com.songheng.dsp.common.utils.StringUtils;
import com.songheng.dsp.model.adx.user.DspUserInfo;
import com.songheng.dsp.model.client.ClientResponse;
import com.songheng.dsp.model.client.SspClientRequest;
import com.songheng.dsp.model.flow.BaseFlow;
import com.songheng.dsp.model.ssp.AdvSspSlot;
import com.songheng.dsp.partner.dc.invoke.AdvSspSlotInvoke;
import com.songheng.dsp.partner.dc.invoke.BlackListConfigInvoke;
import com.songheng.dsp.partner.dc.invoke.DspUserInvoke;
import com.songheng.dsp.partner.service.BizService;
import com.songheng.dsp.partner.utils.RemoteIpUtil;
import com.songheng.dsp.ssp.SspClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @description: 业务逻辑组装
 * @author: zhangshuai@021.com
 * @date: 2019-03-21 15:13
 **/
@Service
public class BizServiceImpl implements BizService {

    @Autowired
    private AdvSspSlotInvoke advSspSlotInvoke;

    @Autowired
    private DspUserInvoke dspUserInvoke;

    @Autowired
    private BlackListConfigInvoke blackListConfigInvoke;

    /**
     * 初始化流量信息
     * */
    @Override
    public SspClientRequest initSspClientRequestObj(HttpServletRequest request, BaseFlow apiArg){
        //获取客户端IP
        String remoteIp = RemoteIpUtil.getRemoteIp(request);
        //获取客户端ua
        String ua = request.getHeader("user-agent");
        //获取客户端referer
        String referer = request.getHeader("referer");
        apiArg.setRemoteIp(remoteIp);
        apiArg.setUa(ua);
        apiArg.setReferer(referer);
        apiArg.setSlotIds(StringUtils.replaceInvalidString(apiArg.getSlotIds(),""));
        //获取广告位集合数据
        List<String> slotIds = StringUtils.strToList(apiArg.getSlotIds());
        Map<String, AdvSspSlot> slotMap = new HashMap<>(slotIds.size());
        for(String slotId : slotIds){
            AdvSspSlot advSspSlot = advSspSlotInvoke.getAdvSspSlotById(slotId);
            if(advSspSlot!=null) {
                slotMap.put(slotId,advSspSlot);
            }
        }
        Map<String, List<String>> blackList = blackListConfigInvoke.getBlackList();
        SspClientRequest ssp = new SspClientRequest(slotMap, apiArg);
        ssp.setBlackListMap(blackList);
        return ssp;
    }
    /**
     * 执行风控业务
     * */
    @Override
    public ClientResponse execute(SspClientRequest request){
        return SspClient.verification(request);
    }
    /**
     * 获取第三方adx企业信息列表
     * */
    @Override
    public List<DspUserInfo> getThirdAdxUserList(String terminal){
        List<DspUserInfo> result = new ArrayList<>();
        List<DspUserInfo> list = dspUserInvoke.getDspUsers(terminal);
        Iterator<DspUserInfo> iterable = list.iterator();
        while (iterable.hasNext()){
            DspUserInfo dspUserInfo = iterable.next();
            dspUserInfo.setOneselfDsp(true);
            if(!dspUserInfo.isOneselfDsp()){
                result.add(dspUserInfo);
            }
        }
        return result;
    }
    /**
     * 获取本方adx企业信息
     * */
    @Override
    public DspUserInfo getOneSelfAdxUser(String terminal) {
        List<DspUserInfo> list = dspUserInvoke.getDspUsers(terminal);
        Iterator<DspUserInfo> iterable = list.iterator();
        while (iterable.hasNext()){
            DspUserInfo dspUserInfo = iterable.next();
            dspUserInfo.setOneselfDsp(true);
            if(dspUserInfo.isOneselfDsp()){
               return dspUserInfo;
            }
        }
        return null;
    }
}
