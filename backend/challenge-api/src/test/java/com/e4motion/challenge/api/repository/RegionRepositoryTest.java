package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.domain.Intersection;
import com.e4motion.challenge.api.domain.Region;
import com.e4motion.challenge.api.domain.RegionGps;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RegionRepositoryTest {

	@Autowired
	RegionRepository regionRepository;

	@Autowired
	RegionGpsRepository regionGpsRepository;

	@Autowired
	IntersectionRepository intersectionRepository;

	@Autowired
	EntityManager entityManager;

	@Test
	void save_withRegionGps() {

		Region region = saveRegion1();

		Optional<Region> found = regionRepository.findByRegionNo(region.getRegionNo());
		assertThat(found.isPresent()).isTrue();
		assertThat(found.get().getGps().size()).isEqualTo(region.getGps().size());

		assertEquals(found.get(), region);

		List<RegionGps> regionGps = regionGpsRepository.findAllByRegion_RegionIdOrderByGpsOrder(region.getRegionId());
		assertThat(regionGps.size()).isEqualTo(region.getGps().size());
	}

	@Test
	void save_withNullRegionGps() {

		Region region = TestDataHelper.getRegion1();
		region.setGps(null);

		regionRepository.saveAndFlush(region);
		entityManager.clear();

		// gps 목록을 null 로 저장해도 region gps 가 null 이 아닌 empty list 로 조회된다.
		Optional<Region> found = regionRepository.findByRegionNo(region.getRegionNo());
		assertThat(found.isPresent()).isTrue();
		assertThat(found.get().getGps().size()).isEqualTo(0);

		assertEquals(found.get(), region);

		List<RegionGps> regionGps = regionGpsRepository.findAllByRegion_RegionIdOrderByGpsOrder(region.getRegionId());
		assertThat(regionGps.size()).isEqualTo(0);
	}

	@Test
	void save_withEmptyRegionGps() {

		Region region = TestDataHelper.getRegion1();
		region.getGps().clear();

		regionRepository.saveAndFlush(region);
		entityManager.clear();

		Optional<Region> found = regionRepository.findByRegionNo(region.getRegionNo());
		assertThat(found.isPresent()).isTrue();
		assertThat(found.get().getGps().size()).isEqualTo(0);

		assertEquals(found.get(), region);

		List<RegionGps> regionGps = regionGpsRepository.findAllByRegion_RegionIdOrderByGpsOrder(region.getRegionId());
		assertThat(regionGps.size()).isEqualTo(0);
	}

	@Test
	void save_withDisorderedRegionGps() {

		Region region = TestDataHelper.getRegion2();
		List<RegionGps> gps = region.getGps();
		gps.get(0).setGpsOrder(2);
		gps.get(1).setGpsOrder(1);
		gps.get(2).setGpsOrder(0);

		regionRepository.saveAndFlush(region);
		entityManager.clear();

		Optional<Region> found = regionRepository.findByRegionNo(region.getRegionNo());
		assertThat(found.isPresent()).isTrue();
		assertThat(found.get().getGps().size()).isEqualTo(region.getGps().size());

		// check ordered
		assertThat(gps.get(2).getLat()).isEqualTo(found.get().getGps().get(0).getLat());
		assertThat(gps.get(2).getLng()).isEqualTo(found.get().getGps().get(0).getLng());
		assertThat(gps.get(1).getLat()).isEqualTo(found.get().getGps().get(1).getLat());
		assertThat(gps.get(1).getLng()).isEqualTo(found.get().getGps().get(1).getLng());
		assertThat(gps.get(0).getLat()).isEqualTo(found.get().getGps().get(2).getLat());
		assertThat(gps.get(0).getLng()).isEqualTo(found.get().getGps().get(2).getLng());
	}

	@Test
	void save_sameRegionGps() {

		Region region = TestDataHelper.getRegion1();
		region.getGps().get(1).setLat(region.getGps().get(0).getLat()); 	// first == second
		region.getGps().get(1).setLng(region.getGps().get(0).getLng());

		assertThrows(DataIntegrityViolationException.class, () -> regionRepository.saveAndFlush(region));
	}

	@Test
	void save_sameRegionGpsOrder() {

		Region region = TestDataHelper.getRegion1();
		region.getGps().get(1).setGpsOrder(region.getGps().get(0).getGpsOrder()); 	// first == second

		assertThrows(DataIntegrityViolationException.class, () -> regionRepository.saveAndFlush(region));
	}

	@Test
	void update_withAnotherGps() {

		Region region = saveRegion1();

		Optional<Region> found = regionRepository.findByRegionNo(region.getRegionNo());
		assertThat(found.isPresent()).isTrue();

		// new gps
		List<RegionGps> gps = new ArrayList<>();
		gps.add(RegionGps.builder().region(found.get()).lat(35.9).lng(128.9).gpsOrder(1).build());
		gps.add(RegionGps.builder().region(found.get()).lat(35.8).lng(128.8).gpsOrder(2).build());
		gps.add(RegionGps.builder().region(found.get()).lat(35.3).lng(128.3).gpsOrder(3).build());

		// gps 목록 수정 시 목록 자체를 대체하면 안된다. list 를 clear 후 add 하여 저장한다.
		// 만약 기존 목록에서 동일한 위도, 경도(unique 위반) 가 있을 경우 모두 제거 후 flush 한 다음 다시 목록 채워서 저장해야 한다.
		found.get().getGps().clear();
		regionRepository.saveAndFlush(found.get());

		found.get().getGps().addAll(gps);
		regionRepository.saveAndFlush(found.get());
		entityManager.clear();

		found = regionRepository.findByRegionNo(region.getRegionNo());
		assertThat(found.isPresent()).isTrue();

		assertThat(found.get().getGps().size()).isEqualTo(3);
		assertThat(found.get().getGps().get(0).getLat()).isEqualTo(gps.get(0).getLat());
		assertThat(found.get().getGps().get(0).getLng()).isEqualTo(gps.get(0).getLng());
		assertThat(found.get().getGps().get(1).getLat()).isEqualTo(gps.get(1).getLat());
		assertThat(found.get().getGps().get(1).getLng()).isEqualTo(gps.get(1).getLng());
		assertThat(found.get().getGps().get(2).getLat()).isEqualTo(gps.get(2).getLat());
		assertThat(found.get().getGps().get(2).getLng()).isEqualTo(gps.get(2).getLng());

		List<RegionGps> regionGps = regionGpsRepository.findAllByRegion_RegionIdOrderByGpsOrder(region.getRegionId());
		assertThat(regionGps.size()).isEqualTo(3);
	}

	@Test
	void update_withEmptyGps() {

		Region region = saveRegion1();

		Optional<Region> found = regionRepository.findByRegionNo(region.getRegionNo());
		assertThat(found.isPresent()).isTrue();

		// update no, name, empty gps
		found.get().setRegionNo("R99");
		found.get().setRegionName("변경리전");
		found.get().getGps().clear();			// gps 목록 수정 시 목록 자체를 대체하면 안된다. list 를 삭제하고자 한다면 목록을 clear 하여 저장한다.
		//found.get().setGps(null);				// gps 목록을 지우고자 set null 하면 안된다.

		regionRepository.saveAndFlush(found.get());
		entityManager.clear();

		found = regionRepository.findByRegionNo(found.get().getRegionNo());
		assertThat(found.isPresent()).isTrue();

		assertThat(found.get().getRegionNo()).isEqualTo("R99");
		assertThat(found.get().getRegionName()).isEqualTo("변경리전");
		assertThat(found.get().getGps().size()).isEqualTo(0);

		List<RegionGps> regionGps = regionGpsRepository.findAllByRegion_RegionIdOrderByGpsOrder(region.getRegionId());
		assertThat(regionGps.size()).isEqualTo(0);
	}

	@Test
	void delete_withRegionGps() {

		Region region = saveRegion1();

		regionRepository.deleteByRegionNo(region.getRegionNo());
		entityManager.flush();
		entityManager.clear();

		Optional<Region> found = regionRepository.findByRegionNo(region.getRegionNo());
		assertThat(regionRepository.count()).isEqualTo(0);
        assertThat(found.isPresent()).isFalse();

		List<RegionGps> regionGps = regionGpsRepository.findAllByRegion_RegionIdOrderByGpsOrder(region.getRegionId());
		assertThat(regionGps.size()).isEqualTo(0);
	}

	@Test
	void get_withIntersection() {

		Region region = saveRegion1();

		Optional<Region> found = regionRepository.findByRegionNo(region.getRegionNo());
		assertThat(found.isPresent()).isTrue();
		assertThat(found.get().getGps().size()).isEqualTo(region.getGps().size());
		assertThat(found.get().getIntersections().size()).isEqualTo(0);

		saveIntersection1_2(region);
		found = regionRepository.findByRegionNo(region.getRegionNo());
		assertThat(found.get().getIntersections().size()).isEqualTo(2);
	}

	private void saveIntersection1_2(Region region) {

		Intersection intersection = TestDataHelper.getIntersection1();
		intersection.setRegion(region);

		intersectionRepository.save(intersection);

		intersection = TestDataHelper.getIntersection2();
		intersection.setRegion(region);
		intersectionRepository.save(intersection);
		entityManager.flush();
		entityManager.clear();

		assertThat(intersectionRepository.count()).isEqualTo(2);
	}

	private Region saveRegion1() {

		Region region = TestDataHelper.getRegion1();
		Region saved = regionRepository.saveAndFlush(region);
		entityManager.clear();

		assertThat(regionRepository.count()).isEqualTo(1);

		return saved;
	}

	private void assertEquals(Region region1, Region region2) {

		assertThat(region1.getRegionNo()).isEqualTo(region2.getRegionNo());
		assertThat(region1.getRegionName()).isEqualTo(region2.getRegionName());
		if (region1.getGps() == null) {
			assertThat(region2.getGps()).isNull();
		} else {
			int i = 0;
			for (RegionGps gps : region1.getGps()) {
				assertThat(gps.getLat()).isEqualTo(region2.getGps().get(i).getLat());
				assertThat(gps.getLng()).isEqualTo(region2.getGps().get(i).getLng());
				i++;
			}
		}
	}
}
