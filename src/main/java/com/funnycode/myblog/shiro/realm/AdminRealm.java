package com.funnycode.myblog.shiro.realm;

import com.funnycode.myblog.pojo.Admin;
import com.funnycode.myblog.pojo.User;
import com.funnycode.myblog.service.AdminService;
import com.funnycode.myblog.shiro.common.UserToken;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @author gaoshucc
 * @create 2018-11-22 17:42
 */
public class AdminRealm extends AuthorizingRealm{

    private static final Logger logger = LoggerFactory.getLogger(AdminRealm.class);

    private AdminService adminService;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("管理员权限认证");
        if(principalCollection == null){
            logger.info("参数PrincipalCollection不能为空");
            throw new AuthorizationException("参数PrincipalCollection不能为空");
        }
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String adminname = (String) principalCollection.getPrimaryPrincipal();
        String role = adminService.getRoleByAdminname(adminname);
        logger.info(adminname + ":" + role);
        Set<String> roles = new HashSet<>();
        roles.add(role);
        authorizationInfo.setRoles(roles);

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.info("管理员身份认证");
        UserToken token = (UserToken) authenticationToken;
        logger.info("管理员:" + token.getUsername());
        SimpleAuthenticationInfo simpleAuthenticationInfo = null;
        //从数据库中查找用户
        Admin admin = adminService.getAdminByAdminname(token.getUsername());
        if(admin != null){
            logger.info("admin存在");
            ByteSource salt = ByteSource.Util.bytes(admin.getAdminname());
            simpleAuthenticationInfo = new SimpleAuthenticationInfo(admin.getAdminname(), admin.getPassword(), salt, getName());
        }else{
            throw new AuthenticationException();
        }

        return simpleAuthenticationInfo;
    }
}
