package com.caoyang.tapon.model.base;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by tonychen on 16/3/9.
 */
public class BaseM implements Serializable {
    public String toJson() {
        return new Gson().toJson(this);
    }

}
