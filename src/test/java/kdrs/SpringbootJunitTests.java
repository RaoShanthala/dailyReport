package kdrs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import co.jp.arche1.kdrs.KdrsApplication;
import co.jp.arche1.kdrs.construction.mapper.PtConstructionMapper;
import co.jp.arche1.kdrs.construction.repository.PtConstructionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KdrsApplication.class)
public class SpringbootJunitTests {
  @Autowired
  private PtConstructionMapper ptConstructionMapper;

  @Test
  public void testCheckout() {

	    PtConstructionRepository ptConstructionRepository = new PtConstructionRepository();
	//	ptConstructionRepository.setConstId(111);
		ptConstructionRepository.setConstCode("acjsdhcicsk-0002");
		ptConstructionRepository.setConstName("inserted using junit test");
		ptConstructionRepository.setStartDate(LocalDate.of(2021,12,28));
		ptConstructionRepository.setEndDate(LocalDate.of(2022,12,28));
		ptConstructionRepository.setCreatedAt(LocalDateTime.now());
		ptConstructionRepository.setUpdatedAt(LocalDateTime.now());
		ptConstructionRepository.setUserId(11);

		int id = ptConstructionMapper.insert(ptConstructionRepository);

		Assert.assertNotNull(ptConstructionMapper.selectOne(id));
  }

  @Test
  public void testConstruction(){
      System.out.println("testConstruction");
      List<PtConstructionRepository>  listptConstructionRepository = ptConstructionMapper.selectMany(21, null, null, (byte)2);
      Assert.assertNull(listptConstructionRepository);
  }


}
