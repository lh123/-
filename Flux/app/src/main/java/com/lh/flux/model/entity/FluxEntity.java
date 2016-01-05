package com.lh.flux.model.entity;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class FluxEntity
{
    private String returnCode;
    private Data data;
    private String msg;

    public void setReturnCode(String returnCode)
    {
        this.returnCode = returnCode;
    }

    public String getReturnCode()
    {
        return returnCode;
    }

    public void setData(Data data)
    {
        this.data = data;
    }

    public Data getData()
    {
        return data;
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
        return "000".equals(returnCode);
    }

    @SuppressWarnings("unused")
    public static class Data
    {
        private Sum sum;
        private String updatedTime;
        private ArrayList<Packages> packages;

        public void setSum(Sum sum)
        {
            this.sum = sum;
        }

        public Sum getSum()
        {
            return sum;
        }

        public void setUpdatedTime(String updatedTime)
        {
            this.updatedTime = updatedTime;
        }

        public String getUpdatedTime()
        {
            return updatedTime;
        }

        public void setPackages(ArrayList<Packages> packages)
        {
            this.packages = packages;
        }

        public ArrayList<Packages> getPackages()
        {
            return packages;
        }
    }

    @SuppressWarnings("unused")
    public static class Sum
    {
        private int available;
        private int total;


        public void setAvailable(int available)
        {
            this.available = available;
        }

        public int getAvailable()
        {
            return available;
        }

        public void setTotal(int total)
        {
            this.total = total;
        }

        public int getTotal()
        {
            return total;
        }
    }

    @SuppressWarnings("unused")
    public static class Packages
    {
        private String name;
        private int available;
        private int total;
        private int trans;
        private int unitTypeId;
        private String unitType;

        public void setName(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }

        public void setAvailable(int available)
        {
            this.available = available;
        }

        public int getAvailable()
        {
            return available;
        }

        public void setTotal(int total)
        {
            this.total = total;
        }

        public int getTotal()
        {
            return total;
        }

        public void setTrans(int trans)
        {
            this.trans = trans;
        }

        public int getTrans()
        {
            return trans;
        }

        public void setUnitTypeId(int unitTypeId)
        {
            this.unitTypeId = unitTypeId;
        }

        public int getUnitTypeId()
        {
            return unitTypeId;
        }

        public void setUnitType(String unitType)
        {
            this.unitType = unitType;
        }

        public String getUnitType()
        {
            return unitType;
        }
    }
}
