package com.lh.flux.domain.event;

import com.lh.flux.model.entity.GrabInfoEntity;

public class GrabEvent
{
    private GrabInfoEntity data;

    public GrabEvent(GrabInfoEntity data)
    {
        this.data = data;
    }

    public void setData(GrabInfoEntity data)
    {
        this.data = data;
    }

    public GrabInfoEntity getData()
    {
        return data;
    }

    public boolean isSuccess()
    {
        return data != null;
    }
}
