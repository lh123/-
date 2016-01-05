package com.lh.flux.model.api;

import com.lh.flux.model.entity.User;

public interface LoginApi
{
    String getCap(User u);

    String login(User u, String cap);

    String login(User u);
}
