package com.funnycode.myblog.shiro;

import com.funnycode.myblog.shiro.realm.AdminRealm;
import com.funnycode.myblog.shiro.realm.MyModularRealmAuthenticator;
import com.funnycode.myblog.shiro.realm.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author gaoshucc
 * @create 2018-11-20 20:17
 */
@Configuration
public class ShiroConfig {

    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //配置登录页面路径
        shiroFilterFactoryBean.setLoginUrl("/user/loginpage");
        //设置无权限时跳转的url
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauth");

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //放行静态资源
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/admin/css/**", "anon");
        filterChainDefinitionMap.put("/admin/js/**", "anon");
        filterChainDefinitionMap.put("/admin/image/**", "anon");
        filterChainDefinitionMap.put("/admin/font/**", "anon");

        filterChainDefinitionMap.put("/user/css/**", "anon");
        filterChainDefinitionMap.put("/user/js/**", "anon");
        filterChainDefinitionMap.put("/user/image/**", "anon");
        filterChainDefinitionMap.put("/user/font/**", "anon");
        filterChainDefinitionMap.put("/user/user-editormd/**", "anon");
        filterChainDefinitionMap.put("/user/kindeditor/**", "anon");

        filterChainDefinitionMap.put("/commons/css/**", "anon");
        filterChainDefinitionMap.put("/commons/js/**", "anon");
        filterChainDefinitionMap.put("/commons/image/**", "anon");
        filterChainDefinitionMap.put("/commons/font/**", "anon");
        filterChainDefinitionMap.put("/commons/layui/**", "anon");
        //游客
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/visitor/**", "anon");
        //用户权限
        filterChainDefinitionMap.put("/user/", "anon");
        filterChainDefinitionMap.put("/user/loginpage", "anon");
        filterChainDefinitionMap.put("/user/homepage", "anon");
        filterChainDefinitionMap.put("/user/login", "anon");
        filterChainDefinitionMap.put("/user/regist", "anon");
        filterChainDefinitionMap.put("/user/userexists", "anon");
        filterChainDefinitionMap.put("/user/nicknameexists", "anon");
        filterChainDefinitionMap.put("/user/valicode", "anon");
        filterChainDefinitionMap.put("/user/checkValicode", "anon");
        filterChainDefinitionMap.put("/user/findAllNotesLimit", "anon");
        filterChainDefinitionMap.put("/user/findAllQuestionsLimit", "anon");
        //放行可供未登录用户阅读
        filterChainDefinitionMap.put("/user/noteArticle", "anon");
        filterChainDefinitionMap.put("/user/findNoteCate", "anon");
        filterChainDefinitionMap.put("/user/findAllNotes", "anon");
        filterChainDefinitionMap.put("/user/findNoteBelongToType", "anon");
        filterChainDefinitionMap.put("/user/note", "anon");
        filterChainDefinitionMap.put("/user/readNote", "anon");
        filterChainDefinitionMap.put("/user/findComments", "anon");
        filterChainDefinitionMap.put("/user/questArticle", "anon");
        filterChainDefinitionMap.put("/user/findQuestCate", "anon");
        filterChainDefinitionMap.put("/user/findAllQuestions", "anon");
        filterChainDefinitionMap.put("/user/findQuestBelongToType", "anon");
        filterChainDefinitionMap.put("/user/question", "anon");
        filterChainDefinitionMap.put("/user/readQuestion", "anon");
        filterChainDefinitionMap.put("/user/findAnswers", "anon");
        filterChainDefinitionMap.put("/user/authorDetail", "anon");
        filterChainDefinitionMap.put("/user/whetherTheLogin", "anon");
        filterChainDefinitionMap.put("/user/**", "roles[user]");
        //管理员权限
        filterChainDefinitionMap.put("/admin/adminexists", "anon");
        filterChainDefinitionMap.put("/admin/nicknameexists", "anon");
        filterChainDefinitionMap.put("/admin/regist", "anon");
        filterChainDefinitionMap.put("/admin/loginpage", "anon");
        filterChainDefinitionMap.put("/admin/login", "anon");
        filterChainDefinitionMap.put("/admin/**", "roles[admin]");
        //对所有资源放行，必须放最后（因为LinkedHashMap是有序的，按顺序进行拦截）
        filterChainDefinitionMap.put("/*", "anon");
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        logger.info("Shiro拦截器注入成功");

        return shiroFilterFactoryBean;
    }

    /**
     * 安全管理器
     */
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(modularRealmAuthenticator());
        ArrayList<Realm> realms = new ArrayList<>();
        realms.add(adminRealm(hashedCredentialsMatcher()));
        realms.add(userRealm(hashedCredentialsMatcher()));
        //向安全管理器注入Realms
        securityManager.setRealms(realms);
        securityManager.setRememberMeManager(cookieRememberMeManager());

        return securityManager;
    }

    @Bean
    public ModularRealmAuthenticator modularRealmAuthenticator(){
        MyModularRealmAuthenticator myModularRealmAuthenticator = new MyModularRealmAuthenticator();
        //至少要有一个Realm
        myModularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());

        return myModularRealmAuthenticator;
    }
    /**
     * 管理员Realm
     */
    @Bean
    public AdminRealm adminRealm(HashedCredentialsMatcher matcher){
        AdminRealm adminRealm = new AdminRealm();
        adminRealm.setCredentialsMatcher(matcher);

        return adminRealm;
    }
    /**
     * 用户Realm
     */
    @Bean
    public UserRealm userRealm(HashedCredentialsMatcher matcher){
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(matcher);

        return userRealm;
    }

    /**
     * 因为我们的密码是加过密的，
     * 所以，如果要Shiro验证用户身份的话，
     * 需要告诉它我们用的是md5加密的，并且是加密了两次。
     * 同时我们在自己的Realm中也通过SimpleAuthenticationInfo返回了加密时使用的盐。
     * 这样Shiro就能顺利的解密密码并验证用户名和密码是否正确了。
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        //散列的次数，比如散列两次，相当于 md5(md5(""));
        hashedCredentialsMatcher.setHashIterations(512);

        return hashedCredentialsMatcher;
    }

    /**
     * 实现“记住我”功能
     */
    @Bean
    public SimpleCookie rememberMeCookie(){
        //cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //只能通过http请求访问，防御XSS攻击
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        //设最大生存时间为7天
        simpleCookie.setMaxAge(7*24*60*60);

        return simpleCookie;
    }

    @Bean
    public CookieRememberMeManager cookieRememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        /*可在此对cookie的加密算法进行修改*/
        return cookieRememberMeManager;
    }

    @Bean
    public FormAuthenticationFilter formAuthenticationFilter(){
        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
        //对应前端的checkbox的name = rememberMe
        formAuthenticationFilter.setRememberMeParam("rememberMe");
        return formAuthenticationFilter;
    }

}
