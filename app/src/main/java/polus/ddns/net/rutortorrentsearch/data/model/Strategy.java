package polus.ddns.net.rutortorrentsearch.data.model;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import polus.ddns.net.rutortorrentsearch.data.vo.EntryTorrent;
import polus.ddns.net.rutortorrentsearch.data.vo.EntrysFromSite;

/**
 * Created by Игорь on 17.11.2016.
 */

public interface Strategy {
    List<EntrysFromSite> getEntrysFromSite(String searchString);
    EntryTorrent getEntryFromUri(URI uri) throws IOException;
}
