
package support.esri.com.fuse.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YahooAddress {

    @SerializedName("ResultSet")
    @Expose
    private ResultSet resultSet;

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

}
