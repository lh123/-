package com.lh.flux.model.entity;

public class CapEntity
{
    private String status;
    private String msg;

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

    public boolean isSuccess()
    {
        return "000".equals(status);
    }
}
