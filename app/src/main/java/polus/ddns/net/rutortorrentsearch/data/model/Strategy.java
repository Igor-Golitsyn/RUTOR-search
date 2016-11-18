package polus.ddns.net.rutortorrentsearch.data.model;

import java.util.List;

import polus.ddns.net.rutortorrentsearch.data.vo.EntrysFromSite;

/**
 * Created by Игорь on 17.11.2016.
 */

public interface Strategy {
    List<EntrysFromSite> getEntrysFromSite(String searchString);
}
