package com.songheng.dsp.datacenter.user.adx;

import com.alibaba.dubbo.config.annotation.Service;
import com.songheng.dsp.common.db.DbUtils;
import com.songheng.dsp.common.enums.ProjectEnum;
import com.songheng.dsp.common.utils.StringUtils;
import com.songheng.dsp.datacenter.config.db.SqlMapperLoader;
import com.songheng.dsp.datacenter.config.props.PropertiesLoader;
import com.songheng.dsp.dubbo.baseinterface.user.adx.DspUserService;
import com.songheng.dsp.model.adx.user.DspUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: luoshaobing
 * @date: 2019/2/25 16:58
 * @description: DSP用户池缓存接口实现类
 */
@Slf4j
@Service(interfaceClass = DspUserService.class,
        timeout = 1000)
@Component
public class DspUserImpl implements DspUserService {

    /**
     * dsp users cache
     * key: app,h5,pc
     * value: List<DspUserInfo>
     */
    private volatile Map<String, List<DspUserInfo>> dspUserInfoMap = new ConcurrentHashMap<>(6);
    /**
     * dsp users cache
     * key: app,h5,pc+dspId
     * value: List<DspUserInfo>
     */
    private volatile Map<String, List<DspUserInfo>> dspUserInfoDspIdMap = new ConcurrentHashMap<>(32);
    /**
     * dsp users cache
     * key: app,h5,pc+priority
     * value: List<DspUserInfo>
     */
    private volatile Map<String, List<DspUserInfo>> dspUserInfoPriorityMap = new ConcurrentHashMap<>(32);

    /**
     * 更新dsp用户
     */
    public void updateDspUsers(){
        String sql = SqlMapperLoader.getSql("DspUser", "queryDspUsers");
        if (StringUtils.isBlank(sql)){
            log.error("updateDspUsers error sql is null, namespace: DspUser, id: queryDspUsers");
            return;
        }
        List<DspUserInfo> users = DbUtils.queryList(ProjectEnum.DATACENTER.getDs()[0], sql, DspUserInfo.class);
        List<DspUserInfo> appUserList = new ArrayList<>();
        List<DspUserInfo> h5UserList = new ArrayList<>();
        List<DspUserInfo> pcUserList = new ArrayList<>();
        String terminal = "", k_dspId = "", k_priority = "";
        Map<String, List<DspUserInfo>> dspIdMapTmp = new ConcurrentHashMap<>(32);
        Map<String, List<DspUserInfo>> priorityMapTmp = new ConcurrentHashMap<>(32);
        for (DspUserInfo userInfo : users){
            if (userInfo.getDspid().equals(StringUtils.replaceInvalidString(
                    PropertiesLoader.getProperty("dfdspid"), "XP4Q4ELI7HTPVK7GHYM9"))){
                //DFDSP
                userInfo.setOneselfDsp(true);
            }
            terminal = StringUtils.isNotBlank(userInfo.getTerminal()) ? userInfo.getTerminal() : terminal;
            if (terminal.toLowerCase().indexOf("app") != -1){
                //设置h5,pc qps初始值
                userInfo.setH5Qps(100);
                userInfo.setPcQps(100);
                appUserList.add(userInfo);
            }
            if (terminal.toLowerCase().indexOf("h5") != -1){
                //设置app,pc qps初始值
                userInfo.setAppQps(100);
                userInfo.setPcQps(100);
                h5UserList.add(userInfo);
            }
            if (terminal.toLowerCase().indexOf("pc") != -1){
                //设置app,h5 qps初始值
                userInfo.setAppQps(100);
                userInfo.setH5Qps(100);
                pcUserList.add(userInfo);
            }
            for (String tml : terminal.split(",|，")){
                k_dspId = String.format("%s%s%s", tml, "_", userInfo.getDspid());
                k_priority = String.format("%s%s%s", tml, "_", userInfo.getPriority());
                if (dspIdMapTmp.containsKey(k_dspId)){
                    dspIdMapTmp.get(k_dspId).add(userInfo);
                } else {
                    List<DspUserInfo> tmp_dspId = new ArrayList<>();
                    tmp_dspId.add(userInfo);
                    dspIdMapTmp.put(k_dspId, tmp_dspId);
                }
                if (!priorityMapTmp.containsKey(k_priority)){
                    List<DspUserInfo> tmp_priority = new ArrayList<>();
                    tmp_priority.add(userInfo);
                    priorityMapTmp.put(k_priority, tmp_priority);
                } else {
                    priorityMapTmp.get(k_priority).add(userInfo);
                }
            }
        }
        if (appUserList.size() > 0){
            dspUserInfoMap.put("app", appUserList);
        }
        if (h5UserList.size() > 0){
            dspUserInfoMap.put("h5", h5UserList);
        }
        if (pcUserList.size() > 0){
            dspUserInfoMap.put("pc", pcUserList);
        }
        if (dspIdMapTmp.size() > 0){
            dspUserInfoDspIdMap = dspIdMapTmp;
        }
        if (priorityMapTmp.size() > 0){
            dspUserInfoPriorityMap = priorityMapTmp;
        }
        log.debug("dspUserSize: {}\tappUserSize: {}\th5UserSize: {}\tpcUserSize: {}",
                users.size(), appUserList.size(), h5UserList.size(), pcUserList.size());
    }

    /**
     * 根据terminal 获取 List<DspUserInfo>
     * @param terminal
     * @return
     */
    @Override
    public List<DspUserInfo> getDspUsers(String terminal){
        if (StringUtils.isBlank(terminal)){
            return new ArrayList<>();
        }
        List<DspUserInfo> result = dspUserInfoMap.get(terminal);
        return null != result ? result : new ArrayList<DspUserInfo>();
    }

    /**
     * 根据terminal,dspId 获取 List<DspUserInfo>
     * @param terminal
     * @param dspId
     * @return
     */
    @Override
    public List<DspUserInfo> getDspUsersByDspId(String terminal, String dspId){
        String tml_dspId = String.format("%s%s%s", terminal, "_", dspId);
        List<DspUserInfo> result = dspUserInfoDspIdMap.get(tml_dspId);
        return null != result ? result : new ArrayList<DspUserInfo>();
    }

    /**
     * 根据terminal,priority 获取 List<DspUserInfo>
     * @param terminal
     * @param priority
     * @return
     */
    @Override
    public List<DspUserInfo> getDspUsersByPriority(String terminal, String priority){
        String tml_priority = String.format("%s%s%s", terminal, "_", priority);
        List<DspUserInfo> result = dspUserInfoPriorityMap.get(tml_priority);
        return null != result ? result : new ArrayList<DspUserInfo>();
    }

    /**
     * 获取所有 List<DspUserInfo>
     * @return
     */
    @Override
    public Map<String, List<DspUserInfo>> getDspUsersListMap() {
        return dspUserInfoMap;
    }

    /**
     * 获取所有 List<DspUserInfo>
     * @return
     */
    @Override
    public Map<String, List<DspUserInfo>> getDspUsersByDspIdListMap() {
        return dspUserInfoDspIdMap;
    }

    /**
     * 获取所有 List<DspUserInfo>
     * @return
     */
    @Override
    public Map<String, List<DspUserInfo>> getDspUsersByPriorityListMap() {
        return dspUserInfoPriorityMap;
    }
}
