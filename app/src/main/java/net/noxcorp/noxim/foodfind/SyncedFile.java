package net.noxcorp.noxim.foodfind;

/**
 * Created by Noxim on 14.7.2016.
 */
public class SyncedFile {

    public static final int TYPE_RESTAURANT = 0;
    public static final int TYPE_FOOD       = 1;
    public static final int TYPE_OTHER      = 2;

    public String  filename;
    public String  contents;
    public String  updateURL;
    public int     type;
    public boolean updated;

    public SyncedFile(String filename, int type, String contents, String updateURL)
    {
        this.filename  = filename;
        this.contents  = contents;
        this.updateURL = updateURL;
        this.type      = type;

        updated = false;
    }

    public void beginAsyncUpdate()
    {
        //TODO: Connect to server and download latest file
        updated = true;
    }



}
