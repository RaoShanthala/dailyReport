package co.jp.arche1.kdrs.gaitoserver.service;

import co.jp.arche1.kdrs.gaitoserver.dto.ZipcodeDto;

public interface GaitoServerService {
    // 戻り値をString → ZipcodeDto に変更
    public ZipcodeDto getAddress(String zipCode);
}

/*@Service
public class GaitoServerService {

	final static Logger logger = LoggerFactory.getLogger(GaitoServerService.class);

	@Autowired
    @Qualifier("zipCodeSearchRestTemplate")
    RestTemplate restTemplate;

    /** 郵便番号検索API リクエストURL */
/*    private static final String URL = "http://zipcloud.ibsnet.co.jp/api/search?zipcode={zipcode}";

    public ZipcodeDto getAddress(String zipcode) {
        return restTemplate.getForObject(URL, ZipcodeDto.class, zipcode);
    }

}*/
