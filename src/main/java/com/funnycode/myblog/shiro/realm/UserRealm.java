package com.funnycode.myblog.shiro.realm;

import com.funnycode.myblog.pojo.PO.User;
import com.funnycode.myblog.service.UserService;
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

/**
 * @author gaoshucc
 * @create 2018-11-22 17:33
 */
public class UserRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(UserRealm.class);

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("用户权限认证");
        if(principalCollection == null){
            logger.info("参数PrincipalCollection不能为空");
            throw new AuthorizationException("参数PrincipalCollection不能为空");
        }
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        if(principalCollection.getPrimaryPrincipal() instanceof User){
            User user = (User) principalCollection.getPrimaryPrincipal();
            logger.info("当前用户：" + user.getNickname());
            authorizationInfo.addRole("user");
        }

        return authorizationInfo;
        //String username = (String) principalCollection.getPrimaryPrincipal();
        //String role = userService.getRoleByUsername(username);
        //logger.info(username + ":" + role);
        //Set<String> roles = new HashSet<>();
        //roles.add(role);
        //authorizationInfo.setRoles(roles);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.info("用户身份认证");
        UserToken token = (UserToken) authenticationToken;
        logger.info("token:" + token.getUsername());
        SimpleAuthenticationInfo simpleAuthenticationInfo = null;
        //从数据库中查找用户
        User user = userService.getUserByUsername(token.getUsername());
        if(user != null){
            ByteSource salt = ByteSource.Util.bytes(user.getUsername());
            simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), salt, getName());
        }else{
            throw new AuthenticationException();
        }

        return simpleAuthenticationInfo;
    }
}
