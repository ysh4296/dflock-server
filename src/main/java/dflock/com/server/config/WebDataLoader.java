package dflock.com.server.config;

import dflock.com.server.domain.FirstItem;
import dflock.com.server.domain.SecondItem;
import dflock.com.server.repository.FirstItemRepository;
import dflock.com.server.repository.SecondItemRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class WebDataLoader {

    @Autowired
    private FirstItemRepository firstItemRepository;

    @Autowired
    private SecondItemRepository secondItemRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String url = "https://df.nexon.com/probability?no=277&type=all"; // 여기에 자동으로 접속할 URL 입력

    @PostConstruct
    @Transactional
    public void loadDataFromUrl() {
        try {
            // 웹페이지 가져오기
            String html = restTemplate.getForObject(url, String.class);
            Document doc = Jsoup.parse(html);

            // 데이터 저장 리스트
            List<FirstItem> firstItemList = new ArrayList<>();
            List<SecondItem> secondItemList = new ArrayList<>();

            // 테이블 데이터 파싱
            Elements rows = doc.select("tr");
            for (Element row : rows) {
                if(row.hasClass("row0") || row.hasClass("row1")) continue;
                Elements cols = row.select("td");
                if (cols.size() % 3 == 0 && cols.size() >= 3) {
                    // 첫 번째 아이템
                    FirstItem firstItem = new FirstItem();
                    firstItem.setItemName(cols.get(0).text());
                    firstItem.setQuantity(Integer.parseInt(cols.get(1).text()));
                    firstItem.setProbability(cols.get(2).text());
                    firstItemList.add(firstItem);
                    if (cols.size() >= 6) {
                        // 두 번째 아이템
                        SecondItem secondItem = new SecondItem();
                        secondItem.setItemName(cols.get(3).text());
                        secondItem.setQuantity(Integer.parseInt(cols.get(4).text()));
                        secondItem.setProbability(cols.get(5).text());
                        secondItemList.add(secondItem);
                    }
                }
            }

            // 데이터베이스에 저장
            firstItemRepository.saveAll(firstItemList);
            secondItemRepository.saveAll(secondItemList);
            System.out.println("데이터 로딩 완료!");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("데이터 로딩 실패: " + e.getMessage());
        }
    }
}
