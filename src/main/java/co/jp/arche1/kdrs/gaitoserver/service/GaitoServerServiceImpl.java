package co.jp.arche1.kdrs.gaitoserver.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//import com.fasterxml.jackson.databind.ObjectMapper;

import co.jp.arche1.kdrs.gaitoserver.dto.ZipcodeDto;

@Service
public class GaitoServerServiceImpl implements GaitoServerService {
  // ObjectMapperを追加
  /* @Autowired
   ObjectMapper objectMapper;

    // URLのパラメータを正規表現に変更
   private static final String URL = "https://zip-cloud.appspot.com/api/search?zipcode=%s";

   @Override
   public ZipcodeDto getAddress(String zipCode) {
       ZipcodeDto zipcodeDto = null;;
       try {
           // ObjectMapperでURLと受け取りクラスを指定
           java.net.URL url = new java.net.URL(String.format(URL,zipCode));
           zipcodeDto = objectMapper.readValue(url, ZipcodeDto.class);
       } catch (Exception e) {
           e.getStackTrace();
           zipcodeDto.setMessage("Unsuccessful: No address exists for the given zipCode");
           zipcodeDto.setStatus(1);
       }
       zipcodeDto.setStatus(0);

       return zipcodeDto;
   } */

	  // restTemplateを追加
    RestTemplate restTemplate = new RestTemplate();

    private static final String URL = "https://zip-cloud.appspot.com/api/search?zipcode={zipCode}";

    @Override
    public ZipcodeDto getAddress(String zipCode) {
        ZipcodeDto zipcodeDto = null;;
        try {

           // reatTemplateのmessageConverterにMappingJackson2HttpMessageConverter を追加
            MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
            List<MediaType> supportedMediaTypes = new ArrayList<>(messageConverter.getSupportedMediaTypes());
            supportedMediaTypes.add(MediaType.TEXT_PLAIN);
            messageConverter.setSupportedMediaTypes(supportedMediaTypes);
            restTemplate.setMessageConverters(Collections.singletonList(messageConverter));
            zipcodeDto = restTemplate.getForObject(URL, ZipcodeDto.class, zipCode);
        } catch (Exception e) {
            e.getStackTrace();
        }
        return zipcodeDto;
    }
}