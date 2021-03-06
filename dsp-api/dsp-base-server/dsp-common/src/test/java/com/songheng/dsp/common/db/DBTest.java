package com.songheng.dsp.common.db;

import com.songheng.dsp.common.InitLoadConf;
import com.songheng.dsp.common.enums.ProjectEnum;
import com.songheng.dsp.common.model.AdplatformAdShowHistory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * @author: luoshaobing
 * @date: 2019/1/23 00:13
 * @description: DB工具类测试
 */
public class DBTest {

    @BeforeClass
    public static void initLoad(){
        InitLoadConf.init(ProjectEnum.H5);
    }

    @Before
    public void init() {
        System.out.println("开始测试-----------------");
    }

    @Test
    public void queryListTest(){
        String sql = "SELECT money FROM adplatform_adShowHistory WHERE adstate <> ? AND FIND_IN_SET(?,positionType) ";
        List<Long> list = DbUtils.queryList(ProjectEnum.H5.getDs()[0], sql, Long.class, -1, "pc");
        System.out.println(list);
    }

    @Test
    public void queryListTest2(){
        String sql = "SELECT hisId, adId, startTime, endTime, statusflag, money, unitprice FROM adplatform_adShowHistory WHERE adstate <> ? AND FIND_IN_SET(?,positionType) ";
        List<AdplatformAdShowHistory> list = DbUtils.queryList(ProjectEnum.H5.getDs()[0], sql, AdplatformAdShowHistory.class, -1, "pc");
        System.out.println(list);
    }

    @Test
    public void callProcedure(){
        String sql = "{call proc_advinfo_showlist_bychannel_new_v1(?,?)}";
        List<String> list = DbUtils.callProcedure(ProjectEnum.H5.getDs()[0], sql, String.class, "pc", "pc");
        for (String str : list){
            System.out.println(str);
        }
    }

    @After
    public void after() {
        System.out.println("测试结束-----------------");
    }

}
