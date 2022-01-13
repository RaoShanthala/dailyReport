package co.jp.arche1.kdrs.common;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.jp.arche1.kdrs.namecollection.mapper.PvNamecollectionMapper;
import co.jp.arche1.kdrs.namecollection.repository.PvNamecollectionRepository;

public class BaseService {

    @Autowired
    PvNamecollectionMapper pvNamecollectionMapper;

    // 引数なしコンストラクタの定義
	public BaseService() {
	}

	public void makeResponseTitle(BaseDto baseDto) throws Exception {
		makeResponseTitle(baseDto, null);
	}

	public void makeResponseTitle(BaseDto baseDto,List<String> listProgress) throws Exception {

		String dtoClassName = baseDto.getClass().getSimpleName();

		for (Field field : baseDto.getClass().getDeclaredFields()) {
	        //field.setAccessible(true);
	        String instanceName = field.getName();
	        String typeName = field.getType().getSimpleName();
	        //String sname = field.get(baseDto).getClass().getSimpleName();	// インスタンスがnullだとfield.get(baseDto)がnullになる。
	        //String sname = "";
	        //System.out.println("sname=" + sname + ", typeName=" + typeName + ", instanceName=" + instanceName);
        	String dtoResClassName = null;
	        if (typeName.equals("Object")) {
	        	if (instanceName.equals("resHdTitle")) {
	        		dtoResClassName = "ResponseHdTitle";
	        	} else if (instanceName.equals("resDtTitle")) {
	        		dtoResClassName = "ResponseDtTitle";
	        	}
	        }
	        if (dtoResClassName != null) {
				List<PvNamecollectionRepository> list = pvNamecollectionMapper.selectResponseDtTitel(dtoClassName, dtoResClassName, "true");

				Object resTitle = null;

				if (list.size() == 0) {
					return;
				}

				StringBuilder sbResTitle = new StringBuilder("");

				for (Iterator<PvNamecollectionRepository> it = list.iterator(); it.hasNext();) {
					PvNamecollectionRepository pvNamecollRepo = it.next();
					sbResTitle.append(sbResTitle.length() == 0 ? "{\"" : "\",\"");
					sbResTitle.append(pvNamecollRepo.getNameAlpha()).append("\":\"").append(pvNamecollRepo.getNameShort());
				}
				sbResTitle.append("\"");
				if (listProgress != null) {
					StringBuilder sbProgTitle = new StringBuilder();
					for (Iterator<String> it = listProgress.iterator(); it.hasNext();) {
						String progress = it.next();
						sbProgTitle.append(sbProgTitle.length() == 0 ? ",\"listProgress\":[\"" : "\",\"");
						sbProgTitle.append(progress);
					}
					sbProgTitle.append("\"]");
					sbResTitle.append(sbProgTitle.toString());
				}
				sbResTitle.append("}");

				//JSON変換用のクラス
			    ObjectMapper mapper = new ObjectMapper();
				resTitle = mapper.readValue(sbResTitle.toString(), Object.class);

		        field.setAccessible(true);
            	field.set(baseDto, resTitle);
	        }
		}

		return;

	}

}
