package com.lh.flux.domain.event;

import com.lh.flux.model.entity.FluxEntity;

public class FluxEvent
{
    private FluxEntity data;

    public FluxEvent(FluxEntity data)
    {
        this.data = data;
    }

    public void setData(FluxEntity data)
    {
        this.data = data;
    }

    public FluxEntity getData()
    {
        return data;
    }

    public boolean isSuccess()
    {
        return data != null;
    }
}
