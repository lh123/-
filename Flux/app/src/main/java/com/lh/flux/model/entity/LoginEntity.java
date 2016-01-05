package com.lh.flux.model.entity;

public class LoginEntity
{
    private String status;
    private String msg;
    private String token;
    private String sessionID;

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getToken()
    {
        return token;
    }

    public void setSessionID(String sessionID)
    {
        this.sessionID = sessionID;
    }

    public String getSessionID()
    {
        return sessionID;
    }

    public boolean isSuccess()
    {
        return "000".equals(status);
    }
}
