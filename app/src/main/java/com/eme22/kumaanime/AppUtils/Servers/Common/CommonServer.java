
package com.eme22.kumaanime.AppUtils.Servers.Common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommonServer {

    @SerializedName("file")
    @Expose
    private String file;

    /**
     * No args constructor for use in serialization
     * 
     */
    public CommonServer() {
    }

    /**
     * 
     * @param file
     */
    public CommonServer(String file) {
        super();
        this.file = file;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public CommonServer withFile(String file) {
        this.file = file;
        return this;
    }

}
