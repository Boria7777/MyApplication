package cn.syndu.eldertip.elder.com.drama.domain;

/**
 * Created by Boria on 2015/12/11.
 */
public class MainDramaData {
    private String TypeName;

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String TypeName) {

        this.TypeName = TypeName;
    }

    public MainDramaData(String typeName) {
        TypeName = typeName;
    }
}
