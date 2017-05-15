package support.esri.com.fuse.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Resources {

    /*   @SerializedName("users")
       @Expose
       private Users users;
       @SerializedName("statuses")
       @Expose
       private Statuses statuses;
       @SerializedName("help")
       @Expose
       private Help help;*/
    @SerializedName("search")
    @Expose
    private Search search;

    /*public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Statuses getStatuses() {
        return statuses;
    }

    public void setStatuses(Statuses statuses) {
        this.statuses = statuses;
    }

    public Help getHelp() {
        return help;
    }

    public void setHelp(Help help) {
        this.help = help;
    }*/

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search trend) {
        this.search = trend;
    }


}
