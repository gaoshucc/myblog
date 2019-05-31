package com.funnycode.myblog.service.impl;

import com.funnycode.myblog.mapper.AdminMapper;
import com.funnycode.myblog.pojo.PO.Admin;
import com.funnycode.myblog.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gaoshucc
 * @create 2018-11-22 19:08
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public String adminexists(String adminname) {
        int exists = adminMapper.adminexists(adminname);

        return exists > 0 ? "false" : "true";
    }

    @Override
    public String nicknameexists(String nickname) {
        int exists = adminMapper.nicknameexists(nickname);

        return exists > 0 ? "false" : "true";
    }

    @Override
    public boolean regist(Admin register) {
        int regSuccess = adminMapper.regist(register);

        return regSuccess > 0 ? true : false;
    }

    @Override
    public String getRoleByAdminname(String adminname) {
        return adminMapper.getRoleByAdminname(adminname);
    }

    @Override
    public Admin getAdminByAdminname(String username) {
        return adminMapper.getAdminByAdminname(username);
    }
}
