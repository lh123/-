package com.lh.flux.domain.event;

import com.lh.flux.model.entity.WelfareInfoEntity;

public class WelfareInfoEvent
{
    private WelfareInfoEntity data;

    public WelfareInfoEvent(WelfareInfoEntity data)
    {
        this.data = data;
    }

    public void setData(WelfareInfoEntity data)
    {
        this.data = data;
    }

    public WelfareInfoEntity getData()
    {
        return data;
    }

    public boolean isSuccess()
    {
        return data != null;
    }
}
