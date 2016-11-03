package com.wei.ipc.model;

import java.io.Serializable;

/**
 * Created by WEI on 2016/7/25.
 */
public class User implements Serializable
{
    public int userId;
    public String userName;
    public boolean isMale;

    public User(int userId, String userName, boolean isMale)
    {
        this.userId = userId;
        this.userName = userName;
        this.isMale = isMale;
    }
}
