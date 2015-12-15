package cn.syndu.eldertip.elder.com.drama.domain;

/**
 * Created by Boria on 2015/12/14.
 */
public class DramaData {
    private String DramaName;
    private String DramaUrl;
    private String DramaImageUrl;


    public String getDramaUrl() {
        return DramaUrl;
    }

    public void setDramaUrl(String dramaUrl) {
        DramaUrl = dramaUrl;
    }

    public String getDramaImageUrl() {
        return DramaImageUrl;
    }

    public void setDramaImageUrl(String dramaImageUrl) {
        DramaImageUrl = dramaImageUrl;
    }

    public String getDramaName() {
        return DramaName;
    }

    public void setDramaName(String dramaName) {
        DramaName = dramaName;
    }

    public DramaData(String dramaUrl, String dramaImageUrl, String dramaName) {
        DramaUrl = dramaUrl;
        DramaImageUrl = dramaImageUrl;
        DramaName = dramaName;
    }
}
