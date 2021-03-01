
package com.eme22.kumaanime.AppUtils.Servers.Fembed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class MultiServer {

    private HashMap<String, String> data;

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }

    public MultiServer withData(HashMap<String, String> data){
        this.data = data;
        return this;
    }
}
