package dflock.com.server.config;

import dflock.com.server.domain.FirstItem;
import dflock.com.server.domain.ItemMetadata;
import dflock.com.server.domain.SecondItem;
import dflock.com.server.repository.FirstItemRepository;
import dflock.com.server.repository.ItemMetadataRepository;
import dflock.com.server.repository.SecondItemRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Component
public class WebDataLoader {

    @Autowired
    private FirstItemRepository firstItemRepository;

    @Autowired
    private SecondItemRepository secondItemRepository;

    @Autowired
    private ItemMetadataRepository itemMetadataRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ITEM_DATA_URL}")
    private String itemDataUrl;

    @Value("${OPEN_API_URL}")
    private String openApiUrl;

    @Value("${OPEN_API_PRIVATE_KEY}")
    private String openApiKey;

    @PostConstruct
    @Transactional
    public void crawItemFromUrl() {
        try {
            // 웹페이지 가져오기
            String html = restTemplate.getForObject(itemDataUrl, String.class);
            Document doc = Jsoup.parse(html);

            // 데이터 저장 리스트
            List<FirstItem> firstItemList = new ArrayList<>();
            List<SecondItem> secondItemList = new ArrayList<>();
            List<ItemMetadata> itemMetadataList = new ArrayList<>();

            firstItemRepository.deleteAll();
            secondItemRepository.deleteAll();
            itemMetadataRepository.deleteAll();

            Set<String> uniqueItemNames = new HashSet<>();

            // 테이블 데이터 파싱
            Elements rows = doc.select("tr");
            for (Element row : rows) {
                if(row.hasClass("row0") || row.hasClass("row1")) continue;
                Elements cols = row.select("td");
                if (cols.size() % 3 == 0 && cols.size() >= 3) {
                    // 첫 번째 아이템
                    FirstItem firstItem = new FirstItem();
                    firstItem.setName(cols.get(0).text());
                    firstItem.setQuantity(Integer.parseInt(cols.get(1).text()));
                    firstItem.setProbability(cols.get(2).text());
                    firstItemList.add(firstItem);
                    uniqueItemNames.add(firstItem.getName());
                    if (cols.size() >= 6) {
                        // 두 번째 아이템
                        SecondItem secondItem = new SecondItem();
                        secondItem.setName(cols.get(3).text());
                        secondItem.setQuantity(Integer.parseInt(cols.get(4).text()));
                        secondItem.setProbability(cols.get(5).text());
                        secondItemList.add(secondItem);
                        uniqueItemNames.add(secondItem.getName());
                    }
                }
            }

            // 데이터베이스에 저장
            firstItemRepository.saveAll(firstItemList);
            secondItemRepository.saveAll(secondItemList);
            loadItemMetaData(uniqueItemNames,itemMetadataList);
            updatePriceMetaData();
            itemMetadataRepository.saveAll(itemMetadataList);
            System.out.println("크롤링 데이터 로딩 완료!");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("크롤링 데이터 로딩 실패: " + e.getMessage());
        }
    }

    /**
     * 시세 정보 최신화 (매 시간마다 실행)
     */
    @Scheduled(fixedRate = 3600000)  // 1시간 (3600000ms)마다 실행
    @Transactional
    public void updatePriceMetaData() {
        try {
            List<ItemMetadata> itemMetadataList = itemMetadataRepository.findAll();
            for (ItemMetadata itemMetadata : itemMetadataList) {
                Integer queryLimit = 10;

                String queryUrl = UriComponentsBuilder.fromHttpUrl(openApiUrl + "/auction-sold")
                        .queryParam("itemName", itemMetadata.getItemName())
                        .queryParam("apikey", openApiKey)
                        .queryParam("limit", queryLimit)
                        .build()
                        .toUriString();

                ResponseEntity<Map> response = restTemplate.getForEntity(queryUrl, Map.class);

                Map<?, ?> body = response.getBody();
                if (body != null && body.containsKey("rows")) {
                    List<Map<String, Object>> rows = (List<Map<String, Object>>) body.get("rows");
                    Integer avgPrice = 0;
                    for (Map<String, Object> row : rows) {
                        Integer unitPrice = (Integer) row.get("unitPrice");
                        if (unitPrice != null) {
                            avgPrice += unitPrice;
                        }
                    }
                    itemMetadata.setUnitPrice(avgPrice / queryLimit);
                    itemMetadataRepository.save(itemMetadata);
                    System.out.println(itemMetadata.getItemName() + " 시세 갱신: " + itemMetadata.getUnitPrice());
                }
            }
            System.out.println("모든 아이템 시세 정보 갱신 완료!");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Open API 시세 데이터 로딩 실패: " + e.getMessage());
        }
    }

    /**
     * 추가 데이터 로드
     * 중복 없는 아이템 이름 목록을 기반으로 메타데이터 저장
     * @param uniqueItemNames
     * @param itemMetadataList
     */
    private void loadItemMetaData(Set<String> uniqueItemNames, List<ItemMetadata> itemMetadataList) {
        try {
            for (String itemName : uniqueItemNames) {
                String queryUrl = UriComponentsBuilder.fromHttpUrl(openApiUrl + "/items")
                        .queryParam("itemName", itemName)
                        .queryParam("apikey", openApiKey)
                        .build()
                        .toUriString();

                ResponseEntity<Map> response = restTemplate.getForEntity(queryUrl, Map.class);
                Map<?, ?> body = response.getBody();
                if (body != null && body.containsKey("rows")) {
                    List<Map<String, Object>> rows = (List<Map<String, Object>>) body.get("rows");
                    for (Map<String, Object> row : rows) {
                        String itemId = (String) row.get("itemId");
                        String itemNameFromApi = (String) row.get("itemName");
                        if (itemId != null && itemNameFromApi != null) {
                            ItemMetadata itemMetadata = new ItemMetadata();
                            itemMetadata.setItemId(itemId);
                            itemMetadata.setItemName(itemNameFromApi);
                            itemMetadataList.add(itemMetadata);
                        }
                        break;
                    }
                }
            }
            System.out.println(itemMetadataList);
            System.out.println("아이템 메타데이터 로딩 완료!");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Open API 데이터 로딩 실패: " + e.getMessage());
        }
    }
}
