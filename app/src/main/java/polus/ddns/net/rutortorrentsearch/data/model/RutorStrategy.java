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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import polus.ddns.net.rutortorrentsearch.data.vo.EntryTorrent;
import polus.ddns.net.rutortorrentsearch.data.vo.EntrysFromSite;
import polus.ddns.net.rutortorrentsearch.utils.ConstantManager;

/**
 * Created by Игорь on 17.11.2016.
 */

public class RutorStrategy implements Strategy {
    static final String TAG = ConstantManager.TAG_PREFIX + "RutorStrategy";
    private static final String URL_FORMAT = "http://rutor.info/search/%d/0/000/0/%s";

    public List<EntrysFromSite> getEntrysFromSite(String searchString) {
        Log.d(TAG, "getEntrysFromSite");
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
            } catch (Exception e) {
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

    private Document getDocument(String searchString, int page) throws IOException {
        Log.d(TAG, "getDocument");
        String url = URI.create(String.format(Locale.getDefault(), URL_FORMAT, page, searchString)).toASCIIString();
        Document document = null;
        try {
            document = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36").referrer("http://www.google.com").get();
            document.html();
        } catch (IOException e) {
        }
        return document;
    }

    public EntryTorrent getEntryFromUri(URI mainUri) throws IOException {
        Log.d(TAG, "getEntryFromUri " + mainUri);
        EntryTorrent entryTorrent = new EntryTorrent();
        Document document = Jsoup.connect(mainUri.toString()).userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36").referrer("http://www.google.com").get();
        Element details = document.getElementById("details");
        Element download = document.getElementById("download");
        entryTorrent.setImageUri(getImageUri(details));
        entryTorrent.setText(getText(details));
        entryTorrent.setLinkTorrent(getUriTorrentFile(download));
        return entryTorrent;
    }

    private URI getImageUri(Element details) {
        Log.d(TAG, "getImageUri");
        List<Element> elementList = details.getElementsByAttribute("src");
        try {
            return URI.create(elementList.get(1).absUrl("src"));
        } catch (Exception e) {
            return null;
        }
    }

    private String getText(Element details) {
        Log.d(TAG, "getText");
        List<Element> elementList = details.getElementsByTag("b");
        Map<String, String> data = new HashMap<>();
        StringBuilder rezult = new StringBuilder();
        for (Element element : elementList) {
            try {
                String key = element.text();
                String value = "";
                String[] parts = details.toString().split(element.toString());
                if (parts.length > 1) {
                    String[] subPart = parts[1].split("\\<br\\>", 2);
                    value = subPart[0];
                }
                if (value == null || value.isEmpty() || value.contains("http") || value.contains("href")) {
                    continue;
                }
                if (key != null) data.put(key, clearFromTag(value));
            } catch (Exception e) {
            }
        }
        List<String> set = new ArrayList<>(data.keySet());
        Collections.sort(set);
        for (String s : set) {
            String val = data.get(s).trim();
            if (!val.isEmpty()) rezult = rezult.append(s + val + "\n");
        }
        return rezult.toString();
    }

    private String clearFromTag(String string) {
        Log.d(TAG, "clearFromTag");
        string = string.trim();
        if (!string.contains("<")) return string;
        if (string.startsWith("<") && string.endsWith(">")) return "";
        while (string.contains("<")) {
            String[] parts = string.split("\\<", 2);
            if (parts.length > 1) {
                String[] subParts = parts[1].split("\\>", 2);
                string = parts[0] + subParts[1];
            }
        }
        return string;
    }

    private URI getUriTorrentFile(Element element) {
        Log.d(TAG, "getUriTorrentFile");
        return URI.create(element.child(1).absUrl("href"));
    }
}
