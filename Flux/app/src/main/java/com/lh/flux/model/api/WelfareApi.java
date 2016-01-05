package com.lh.flux.model.api;

import com.lh.flux.model.entity.User;

public interface WelfareApi
{
    String getWelfareStatus(User u);

    String getWelfareCookie(User u);

    String grabWelfare(User u);
}
