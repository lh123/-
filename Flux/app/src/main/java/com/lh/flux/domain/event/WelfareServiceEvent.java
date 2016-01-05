package com.lh.flux.domain.event;

public class WelfareServiceEvent
{
    private String msg;
    private boolean isGrabing;

    public WelfareServiceEvent(String msg, boolean isGrabing)
    {
        this.msg = msg;
        this.isGrabing = isGrabing;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getMsg()
    {
        return msg;
    }

//    public void setIsGrabing(boolean isGrabing)
//    {
//        this.isGrabing = isGrabing;
//    }

    public boolean isGrabing()
    {
        return isGrabing;
    }
}
