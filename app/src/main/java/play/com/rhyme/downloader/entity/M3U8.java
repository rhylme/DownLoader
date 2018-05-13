package play.com.rhyme.downloader.entity;

/**
 * 作 者 : rhyme
 * 邮 箱 : rhymelph@qq.com
 * Q  Q : 708959817
 * 时 间 : 2018/5/13
 * 介 绍 :
 **/
public class M3U8 {
    private String id;
    private String address;
    private Integer progress;
    private Integer max_progress;
    private Integer comlete;//0未完成,1已完成
    private String  path;//路径


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getMax_progress() {
        return max_progress;
    }

    public void setMax_progress(Integer max_progress) {
        this.max_progress = max_progress;
    }

    public Integer getComlete() {
        return comlete;
    }

    public void setComlete(Integer comlete) {
        this.comlete = comlete;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

