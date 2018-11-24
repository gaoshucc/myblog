package com.funnycode.myblog.shiro.realm;

import com.funnycode.myblog.shiro.common.UserToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 重写ModularRealmAuthenticator实现配置多个Realm
 * 以使不同角色通过不同登录页面登录，并分开认证
 *
 * @author gaoshucc
 * @create 2018-11-22 16:54
 */
public class MyModularRealmAuthenticator extends ModularRealmAuthenticator {
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        assertRealmsConfigured();
        //强制转换为自定义的UserToken
        UserToken userToken = (UserToken) authenticationToken;
        String loginType = userToken.getLoginType();
        //所有的realm
        Collection<Realm> realms = getRealms();
        //登录类型对应的realm
        ArrayList<Realm> loginTypeRealms = new ArrayList<>();
        //将与LoginType对应的realm加入loginTypeRealms
        for (Realm realm:realms) {
            if(realm.getName().contains(loginType)){
                loginTypeRealms.add(realm);
            }
        }
        if(loginTypeRealms.size() == 1){
            return doSingleRealmAuthentication(loginTypeRealms.get(0), userToken);
        }else {
            return doMultiRealmAuthentication(loginTypeRealms, userToken);
        }
    }
}
