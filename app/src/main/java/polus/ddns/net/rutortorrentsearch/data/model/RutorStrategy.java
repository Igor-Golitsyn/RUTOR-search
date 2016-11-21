package polus.ddns.net.rutortorrentsearch.data.model;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import polus.ddns.net.rutortorrentsearch.data.vo.EntrysFromSite;
import polus.ddns.net.rutortorrentsearch.utils.ConstantManager;

/**
 * Created by Игорь on 17.11.2016.
 */

public class RutorStrategy implements Strategy {
    static final String TAG = ConstantManager.TAG_PREFIX + "RutorStrategy";
    private static final String URL_FORMAT = "http://rutor.info/search/%d/0/000/0/%s";

    public List<EntrysFromSite> getEntrysFromSite(String searchString) {
        Log.d(TAG, "getEntrysFromSite " + searchString);
        int i = 0;
        List<Element> elements = new ArrayList<>();
        List<EntrysFromSite> siteList = new ArrayList<>();
        searchString = searchString.replaceAll(" ", "%20");
        while (true) {
            Document document;
            int oldSize = elements.size();
            try {
                document = getDocument(searchString, i++);
                elements.addAll(document.getElementsByClass("gai"));
                elements.addAll(document.getElementsByClass("tum"));
            } catch (IOException e) {
                break;
            }
            if (oldSize == elements.size()) break;
        }

        for (Element element : elements) {
            try {
                int sizeNode = element.childNodeSize();
                String date = element.child(0).text();
                String name = element.child(1).child(2).text();
                String size = sizeNode < 7 ? element.child(2).text() : element.child(3).text();
                URI uri = URI.create(element.child(1).child(2).absUrl("href"));
                String seed = element.getElementsByClass("green").get(0).text();
                seed = seed.substring(1, seed.length());
                int seeders = Integer.parseInt(seed);
                EntrysFromSite entrysFromSite = new EntrysFromSite(date, name, size, seeders, uri);
                if (seeders > 0) siteList.add(entrysFromSite);
            } catch (Exception e) {
            }
        }
        Collections.sort(siteList, new Comparator<EntrysFromSite>() {
            @Override
            public int compare(EntrysFromSite o1, EntrysFromSite o2) {
                int rezult = Double.compare(o2.getSeeders(), o1.getSeeders());
                if (rezult == 0) rezult = Double.compare(o2.getSizeDouble(), o1.getSizeDouble());
                return rezult;
            }
        });
        return siteList;
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        String url = URI.create(String.format(Locale.getDefault(), URL_FORMAT, page, searchString)).toASCIIString();
        Document document = null;
        try {
            document = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36").referrer("http://www.google.com").get();
            document.html();
        } catch (IOException e) {
        }
        return document;
    }
}
