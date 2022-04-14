package com.micropos.products.repository;

import com.micropos.products.model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class JDRepository implements ProductRepository {

    private List<Product> products = new ArrayList<>();

    @Override
    public List<Product> allProducts() {
        try {
            products = parseJD("Java");
        } catch (IOException e) {
            products = new ArrayList<>();
        }
        return products;
    }

    @Override
    public Optional<Product> findProduct(String productId) {
        for (Product p : products) {
            if (p.getId().equals(productId)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    public static List<Product> parseJD(String keyword) throws IOException {
        System.out.println("request jd");
        String url = "https://search.jd.com/Search?keyword=" + keyword;
        String cookie = "shshshfpa=24935d0b-40f3-ea49-6208-877a1202d3d8-1632556252; __jdu=1641276466807758613656; pinId=FeUwk7pxx_I28hJm49hX8LV9-x-f3wj7; qrsc=3; pin=jd_781fc057ee201; unick=Yi1123581321; _tp=RQIXzMe1UAZGrcjs21wqk7vTrwjlOJt2XYVjCk4px9c=; _pst=jd_781fc057ee201; TrackID=15G0ynvtxVvmAUULWJKti_fbkCRN2olEqUhft0rrI85E2I9rhcoYNdqnxKyav28RJwdMfjjUzxIH5FtzwnyR3d1uaZqYM3eE5C89VGjpI2XOKLZyOkeaYuq8JidoSM0bU; __jdv=122270672|google|AmericaBrandC013|cpc|notset|1649739968982; shshshfpb=wVaJSZBPz6T0niRTpQs3G3g==; areaId=12; ipLoc-djd=12-904-0-0; rkv=1.0; PCSYCityID=CN_320000_320100_0; __jda=122270672.1641276466807758613656.1641276467.1649226285.1649739969.13; __jdb=122270672.4.1641276466807758613656|13.1649739969; __jdc=122270672; shshshfp=05e530810fad632874254cc9927cc369; shshshsID=093780fd2d39058f304a209ea2622a98_4_1649740283372; 3AB9D23F7A4B3C9B=E2BFUUXEKPG3RMFYYSDTPLLAK5OPPXB26CFA5EHWYJVGAKHNTZQ7TEQ5BVI7J7BV7ROZGSAE27H7DW2XI6Z2A3WLRA";
        HashMap<String, String> cookieMap = new HashMap<>();
        String[] items = cookie.trim().split(";");
        for (String item : items) {
            String[] kv = item.trim().split("=");
            cookieMap.put(kv[0], kv[1]);
        }
        Document document = Jsoup.connect(url).cookies(cookieMap).timeout(10000).get();
        Element element = document.getElementById("J_goodsList");

        if (element == null){
            return new ArrayList<>();
        }
        Elements elements = element.getElementsByTag("li");

        List<Product> list = new ArrayList<>();

        for (Element el : elements) {
            String id = el.attr("data-spu");
            String img = "https:".concat(el.getElementsByTag("img").eq(0).attr("data-lazy-img"));
            String price = el.getElementsByAttribute("data-price").text();
            String title = el.getElementsByClass("p-name").eq(0).text();
            if (title.contains("，"))
                title = title.substring(0, title.indexOf("，"));

            Product product = new Product(id, title, Double.parseDouble(price), img);
            list.add(product);
        }
        return list;
    }
}
