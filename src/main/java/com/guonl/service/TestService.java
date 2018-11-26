package com.guonl.service;

import com.guonl.dao.LogsSqlRecordMapper;
import com.guonl.dao.UsersMapper;
import com.guonl.po.Users;
import com.guonl.po.UsersExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * Created by guonl
 * Date 2018/11/15 1:05 PM
 * Description:
 */
@Service
public class TestService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private LogsSqlRecordMapper logsSqlRecordMapper;

    public List<Users> queryList() {
        UsersExample usersExample = new UsersExample();
        return usersMapper.selectByExample(usersExample);
    }



}
