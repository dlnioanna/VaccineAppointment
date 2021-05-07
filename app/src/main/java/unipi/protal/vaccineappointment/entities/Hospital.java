package unipi.protal.vaccineappointment.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Hospital implements Parcelable {
    private String title;
    private String description;
    private String area;
    private Double latitude;
    private Double longitute;
    private Float distance;

    public Hospital() {
    }

    public Hospital(String title, String description, String area, Double latitude, Double longitute) {
        this.title = title;
        this.description = description;
        this.area = area;
        this.latitude = latitude;
        this.longitute = longitute;
    }

    protected Hospital(Parcel in) {
        title = in.readString();
        description = in.readString();
        area = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitute = null;
        } else {
            longitute = in.readDouble();
        }
    }

    public static final Creator<Hospital> CREATOR = new Creator<Hospital>() {
        @Override
        public Hospital createFromParcel(Parcel in) {
            return new Hospital(in);
        }

        @Override
        public Hospital[] newArray(int size) {
            return new Hospital[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(area);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitute == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitute);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitute() {
        return longitute;
    }

    public void setLongitute(Double longitute) {
        this.longitute = longitute;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }


}
