
package com.bookyrself.bookyrself.models.searchrequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Match {

    @SerializedName("citystate")
    @Expose
    private String citystate;

    public String getCitystate() {
        return citystate;
    }

    public void setCitystate(String citystate) {
        this.citystate = citystate;
    }

}
