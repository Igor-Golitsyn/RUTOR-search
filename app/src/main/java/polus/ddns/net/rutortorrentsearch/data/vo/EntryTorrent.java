package polus.ddns.net.rutortorrentsearch.data.vo;

import java.io.Serializable;
import java.net.URI;

/**
 * Created by Игорь on 21.11.2016.
 */

public class EntryTorrent implements Serializable {
    private static final long serialVersionUID = 0L;
    private URI imageUri;
    private String text;
    private URI linkTorrent;

    public EntryTorrent(URI imageUri, String text, URI linkTorrent) {
        this.imageUri = imageUri;
        this.text = text;
        this.linkTorrent = linkTorrent;
    }

    public EntryTorrent() {
    }

    public URI getImageUri() {
        return imageUri;
    }

    public void setImageUri(URI imageUri) {
        this.imageUri = imageUri;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public URI getLinkTorrent() {
        return linkTorrent;
    }

    public void setLinkTorrent(URI linkTorrent) {
        this.linkTorrent = linkTorrent;
    }
}
