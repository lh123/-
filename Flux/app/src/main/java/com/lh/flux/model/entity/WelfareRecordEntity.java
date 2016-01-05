package com.lh.flux.model.entity;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class WelfareRecordEntity
{
    private String returnCode;
    private ArrayList<Data> data;

    public void setReturnCode(String returnCode)
    {
        this.returnCode = returnCode;
    }

    public String getReturnCode()
    {
        return returnCode;
    }

    public void setData(ArrayList<Data> data)
    {
        this.data = data;
    }

    public ArrayList<Data> getData()
    {
        return data;
    }

    public boolean isSuccess()
    {
        return "000".equals(returnCode);
    }

    public static class Data
    {
        private String phone;
        private String envelopes;
        private int status;
        private String addtime;
        private WelfareEnvelopEntity prize;

        public void setPhone(String phone)
        {
            this.phone = phone;
        }

        public String getPhone()
        {
            return phone;
        }

        public void setEnvelopes(String envelopes)
        {
            this.envelopes = envelopes;
        }

        public String getEnvelopes()
        {
            return envelopes;
        }

        public void setStatus(int status)
        {
            this.status = status;
        }

        public int getStatus()
        {
            return status;
        }

        public void setAddtime(String addtime)
        {
            this.addtime = addtime;
        }

        public String getAddtime()
        {
            return addtime;
        }

        public void setPrize(WelfareEnvelopEntity prize)
        {
            this.prize = prize;
        }

        public WelfareEnvelopEntity getPrize()
        {
            return prize;
        }
    }
}
