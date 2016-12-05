package polus.ddns.net.rutortorrentsearch.utils;

import android.net.Uri;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Игорь on 05.12.2016.
 */

public class WallpaperManager {
    private static final String adress = "http://all-free-download.com/wallpapers";

    public static Uri getWallpaper() {
        List<Uri> uriList = new ArrayList<>();
        List<Element> elements = new ArrayList<>();
        try {
            Document document = Jsoup.connect(adress).userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36").referrer("http://www.google.com").get();
            elements.addAll(document.getElementsByClass("item"));
            for (Element element : elements) {
                List<Element> el = element.getElementsByAttribute("src");
                Uri uri = Uri.parse(el.get(0).absUrl("src"));
                if (uri.toString().contains("images")) {
                    uriList.add(Uri.parse(el.get(0).absUrl("src")));
                }
            }
            return uriList.get(new Random().nextInt(uriList.size()));
        } catch (IOException e) {
        }
        return null;
    }
}
