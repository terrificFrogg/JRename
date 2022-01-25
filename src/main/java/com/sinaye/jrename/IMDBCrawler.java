package com.sinaye.jrename;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class IMDBCrawler {

    public IMDBCrawler(){}

    public String[] getEpisodeNames(String link) throws IOException{
        String[] episodeList = null;
        if(!link.isEmpty()){
            Document doc = Jsoup.connect(link).get();
            Elements data = doc.select(".list_item .info strong a");
            episodeList = new String[data.size()];
            int i = 0;
            byte[] bytes = null;
            for (Element episodeName : data) {
                bytes = episodeName.text().getBytes(StandardCharsets.UTF_8);
                episodeList[i] = new String(bytes, StandardCharsets.UTF_8);
                i++;
            }
        }
        return episodeList;
    }
}
