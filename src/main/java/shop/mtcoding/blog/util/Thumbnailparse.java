package shop.mtcoding.blog.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Thumbnailparse {

    public static String thParse(String a) {
        String img = "";
        Document doc = Jsoup.parse(a); // 데이터, 파일을 받아서 html로 파싱해줌
        Elements els = doc.select("img");
        if (els.size() == 0) {
            return "/images/dora.png";
            // 임시 사진 제공해주기
            // 디비 thumnail -> /images/profile.jfif
        } else {
            Element el = els.get(0);
            img += el.attr("src"); // attribute를 src Key로 받아서 값을 파싱
            return img;
            // 디비 thumnail -> img
        }
    }
}
