package com.lh.flux.model.api;

import com.lh.flux.model.entity.User;
import com.lh.flux.model.utils.StreamUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class FluxApiImpl implements FluxApi
{
    @Override
    public String getFluxInfo(User u)
    {
        URL url;
        HttpURLConnection conn;
        String temp = null;
        try
        {
            url = new URL(ApiStore.getFluxApi());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setRequestProperty("Origin", "http://game.hb189.mobi");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Referer", "http://game.hb189.mobi/android-personal-center?phone=" + u.getPhone() + "&sessionId=" + u.getSessionID());
            conn.connect();
            String postInfo = "{\"phone\":\"" + u.getPhone() + "\"}";
            StreamUtils.writeToStream(conn.getOutputStream(), postInfo.getBytes());
            temp = StreamUtils.readFromStream(conn.getInputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return temp;
    }
}
