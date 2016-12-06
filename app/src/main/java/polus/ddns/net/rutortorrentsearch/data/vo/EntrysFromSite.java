package polus.ddns.net.rutortorrentsearch.data.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URI;

/**
 * Created by Игорь on 17.11.2016.
 */

public class EntrysFromSite implements Parcelable {
    private String date;
    private String name;
    private String size;
    private int seeders;
    private URI uri;

    public EntrysFromSite(String date, String name, String size, int seeders, URI uri) {
        this.date = date;
        this.name = name;
        this.size = size;
        this.seeders = seeders;
        this.uri = uri;
    }

    protected EntrysFromSite(Parcel in) {
        date = in.readString();
        name = in.readString();
        size = in.readString();
        seeders = in.readInt();
        uri = URI.create(in.readString());
    }

    public static final Creator<EntrysFromSite> CREATOR = new Creator<EntrysFromSite>() {
        @Override
        public EntrysFromSite createFromParcel(Parcel in) {
            return new EntrysFromSite(in);
        }

        @Override
        public EntrysFromSite[] newArray(int size) {
            return new EntrysFromSite[size];
        }
    };

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setSeeders(int seeders) {
        this.seeders = seeders;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public double getSizeDouble() {
        double sizeDouble = Double.parseDouble(size.substring(0, size.length() - 3));
        if (size.endsWith("MB")) sizeDouble = sizeDouble / 1000;
        return sizeDouble;
    }

    public int getSeeders() {
        return seeders;
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "EntrysFromSite{" +
                "date='" + date + '\'' +
                ", seeders=" + seeders + '\'' +
                ", size='" + size + '\'' +
                ", name='" + name + '\'' +
                ", uri=" + uri +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(name);
        parcel.writeString(size);
        parcel.writeInt(seeders);
        parcel.writeString(uri.toString());
    }
}