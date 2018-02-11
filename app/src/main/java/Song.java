/**
 * Created by Sun on 2/9/2018.
 */

public class Song {
    private String title;
    private String lastLocation;
    private String lastTime;
    private String lastDate;
    private boolean favorite = false;
    private boolean dislike = false;

    public Song (String name) {
        title = name;
    }

    public void setLocation(String newLocation) {
        lastLocation = newLocation;
    }

    public void setTime(String newTime) {
        lastTime = newTime;
    }

    public void setDate(String newDate) {
        lastDate = newDate;
    }

    public void dislike() {
        dislike = true;
    }

    public void favorite() {
        favorite = true;
    }

    public void neutral() {
        favorite = false;
        dislike = false;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return lastLocation;
    }

    public String getLastTime() {
        return lastTime;
    }

    public String getLastDate() {
        return lastDate;
    }

}
