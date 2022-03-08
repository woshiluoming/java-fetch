package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class musicFetch {
    public static void main(String[] args) {

        String url = "https://www.9ku.com";

        Document document = getDocument("https://www.9ku.com/geshou/798.htm");
        Elements musicElementsList = document.select("[class=singerMain]");
        Elements musicBody = musicElementsList.get(0).select("[id=body]");
        for (Element musicElement : musicBody){
            Elements songNames = musicElement.select("[class=singerMusic clearfix]").select("li");
            for (Element songNameA : songNames){
                // System.out.println(songNameA.select("[class=songNameA]").attr("href"));
                // System.out.println(songNameA.select("[class=songNameA]").text());
                String songName = songNameA.select("[class=songNameA]").text();
                String musicUrl = songNameA.select("[class=songNameA]").attr("href");
                System.out.println(url + musicUrl);
                downLoad(url + musicUrl,songName);
            }
        }

    }

    /**
     * 下载歌曲
     *
     * @param songName
     * @param url
     * @return
     */
    public static void downLoad(String url, String songName) {
        Document document = getDocument(url);
        String downUrl = "http://" + document.select("[class=pFl clearfix]").select("[class=ncol-btns]").select("[class=down]").attr("href").substring(2);
        System.out.println(downUrl);
        Document downDocument = getDocument(downUrl);
        Elements musicDownload = downDocument.select("[class=song-box clearfix]").select("[class=downInfo]").select("a");
        String downloadMusic = musicDownload.get(musicDownload.size() - 1).attr("href");
        InputStream in = null;
        FileOutputStream fileOutputStream = null;
        try {
            URL url1 = new URL(downloadMusic);
            in = url1.openStream();
            byte[] buffer = new byte[2048];
            File file = new File("D:\\Workspase\\luomingTest\\music\\" + songName + ".mp3");
            fileOutputStream = new FileOutputStream(file);
            int count = 0;
            while ((count = in.read(buffer)) > 0){
                fileOutputStream.write(buffer,0,count);
            }
        } catch (IOException e) {
            System.out.println(songName + "下载失败");
            e.printStackTrace();
        }finally {
            try {
                in.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(songName + "下载成功");
    }

    /**
     * @param url 访问路径
     * @return
     */
    public static Document getDocument(String url) {
        try {
            //5000是设置连接超时时间，单位ms
            return Jsoup.connect(url).timeout(5000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
