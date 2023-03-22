package com.ds.screw.auth.listener;

import com.ds.screw.auth.domain.LoginModel;

/**
 *
 * <p>
 *      侦听器的默认实现：do nothing
 * </p>
 *
 * @author dongsheng
 */
public class DefaultAuthEventListener implements AuthEventListener {
    @Override
    public void onLogin(String loginId, String tokenValue, LoginModel loginModel) { /*do nothing*/}

    @Override
    public void onLogout(String loginId, String tokenValue) { /*do nothing*/}

    @Override
    public void onReplaced(String loginId, String tokenValue) { /*do nothing*/}

    @Override
    public void onDisable(String loginId, long disableTime) { /*do nothing*/}

    @Override
    public void onEnable(String loginId) { /*do nothing*/}

    @Override
    public void onSessionCreated(String id) { /*do nothing*/}

    @Override
    public void onSessionInvalidate(String id) { /*do nothing*/}
}
