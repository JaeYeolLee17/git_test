package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.domain.Region;
import com.e4motion.challenge.api.domain.RegionGps;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class RegionRepositoryTest {

	@Autowired
	RegionRepository regionRepository;

	@Autowired
	RegionGpsRepository regionGpsRepository;

	@Autowired
	EntityManager entityManager;

	@Test
	void save_withRegionGps() {

		Region region = saveRegion1();

		Optional<Region> found = regionRepository.findByRegionNo(region.getRegionNo());
		assertThat(found.isPresent()).isTrue();
		assertThat(found.get().getGps().size()).isEqualTo(region.getGps().size());

		assertEquals(found.get(), region);

		List<RegionGps> regionGps = regionGpsRepository.findByRegion_RegionIdOrderByGpsOrder(region.getRegionId());
		assertThat(regionGps.size()).isEqualTo(region.getGps().size());
	}

	@Test
	void save_withNullRegionGps() {

		Region region = TestDataHelper.getRegion1();
		region.setGps(null);

		Region saved = regionRepository.save(region);
		entityManager.flush();
		entityManager.clear();

		Optional<Region> found = regionRepository.findByRegionNo(region.getRegionNo());
		assertThat(found.isPresent()).isTrue();
		assertThat(found.get().getGps().size()).isEqualTo(0);

		assertEquals(found.get(), region);

		List<RegionGps> regionGps = regionGpsRepository.findByRegion_RegionIdOrderByGpsOrder(region.getRegionId());
		assertThat(regionGps.size()).isEqualTo(0);
	}

	@Test
	void save_withEmptyRegionGps() {

		Region region = TestDataHelper.getRegion1();
		region.getGps().clear();

		Region saved = regionRepository.save(region);
		entityManager.flush();
		entityManager.clear();

		Optional<Region> found = regionRepository.findByRegionNo(region.getRegionNo());
		assertThat(found.isPresent()).isTrue();
		assertThat(found.get().getGps().size()).isEqualTo(0);

		assertEquals(found.get(), region);

		List<RegionGps> regionGps = regionGpsRepository.findByRegion_RegionIdOrderByGpsOrder(region.getRegionId());
		assertThat(regionGps.size()).isEqualTo(0);
	}

	@Test
	void save_withDisorderRegionGps() {

		Region region = TestDataHelper.getRegion2();
		List<RegionGps> gps = region.getGps();

		List<RegionGps> disorderGps = new ArrayList<>();
		disorderGps.add(gps.get(2));
		disorderGps.add(gps.get(1));
		disorderGps.add(gps.get(0));
		region.setGps(disorderGps);

		// check disordered
		assertThat(gps.get(0).getLatitude()).isEqualTo(disorderGps.get(2).getLatitude());
		assertThat(gps.get(0).getLongitude()).isEqualTo(disorderGps.get(2).getLongitude());
		assertThat(gps.get(1).getLatitude()).isEqualTo(disorderGps.get(1).getLatitude());
		assertThat(gps.get(1).getLongitude()).isEqualTo(disorderGps.get(1).getLongitude());
		assertThat(gps.get(2).getLatitude()).isEqualTo(disorderGps.get(0).getLatitude());
		assertThat(gps.get(2).getLongitude()).isEqualTo(disorderGps.get(0).getLongitude());

		regionRepository.save(region);
		entityManager.flush();
		entityManager.clear();

		Optional<Region> found = regionRepository.findByRegionNo(region.getRegionNo());
		assertThat(found.isPresent()).isTrue();
		assertThat(found.get().getGps().size()).isEqualTo(region.getGps().size());

		// check ordered
		assertThat(gps.get(0).getLatitude()).isEqualTo(found.get().getGps().get(0).getLatitude());
		assertThat(gps.get(0).getLongitude()).isEqualTo(found.get().getGps().get(0).getLongitude());
		assertThat(gps.get(1).getLatitude()).isEqualTo(found.get().getGps().get(1).getLatitude());
		assertThat(gps.get(1).getLongitude()).isEqualTo(found.get().getGps().get(1).getLongitude());
		assertThat(gps.get(2).getLatitude()).isEqualTo(found.get().getGps().get(2).getLatitude());
		assertThat(gps.get(2).getLongitude()).isEqualTo(found.get().getGps().get(2).getLongitude());
	}

	@Test
	void update_withAnotherGps() {

		Region region = saveRegion1();

		Optional<Region> found = regionRepository.findByRegionNo(region.getRegionNo());
		assertThat(found.isPresent()).isTrue();

		// new gps
		List<RegionGps> gps = new ArrayList<>();
		gps.add(RegionGps.builder().region(found.get()).latitude(35.9).longitude(128.9).gpsOrder(1).build());
		gps.add(RegionGps.builder().region(found.get()).latitude(35.8).longitude(128.8).gpsOrder(2).build());
		gps.add(RegionGps.builder().region(found.get()).latitude(35.3).longitude(128.3).gpsOrder(3).build());	// already exists gps.

		found.get().getGps().clear();			// To change list, you should not replace list itself. just clear and add.
		regionRepository.save(found.get());		// If duplicated region_id, latitude, longitude exists, save and flush first before add.
		entityManager.flush();

		found.get().getGps().addAll(gps);
		regionRepository.save(found.get());
		entityManager.flush();
		entityManager.clear();

		found = regionRepository.findByRegionNo(region.getRegionNo());
		assertThat(found.isPresent()).isTrue();

		assertThat(found.get().getGps().size()).isEqualTo(3);
		assertThat(found.get().getGps().get(0).getLatitude()).isEqualTo(gps.get(0).getLatitude());
		assertThat(found.get().getGps().get(0).getLongitude()).isEqualTo(gps.get(0).getLongitude());
		assertThat(found.get().getGps().get(1).getLatitude()).isEqualTo(gps.get(1).getLatitude());
		assertThat(found.get().getGps().get(1).getLongitude()).isEqualTo(gps.get(1).getLongitude());

		List<RegionGps> regionGps = regionGpsRepository.findByRegion_RegionIdOrderByGpsOrder(region.getRegionId());
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
		found.get().getGps().clear();			// To change list, you should not replace list itself. just clear and add.
		//found.get().setGps(null);				// Do not set null, set null to update will occur exception.

		regionRepository.save(found.get());
		entityManager.flush();
		entityManager.clear();

		found = regionRepository.findByRegionNo(found.get().getRegionNo());
		assertThat(found.isPresent()).isTrue();

		assertThat(found.get().getRegionNo()).isEqualTo("R99");
		assertThat(found.get().getRegionName()).isEqualTo("변경리전");
		assertThat(found.get().getGps().size()).isEqualTo(0);

		List<RegionGps> regionGps = regionGpsRepository.findByRegion_RegionIdOrderByGpsOrder(region.getRegionId());
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

		List<RegionGps> regionGps = regionGpsRepository.findByRegion_RegionIdOrderByGpsOrder(region.getRegionId());
		assertThat(regionGps.size()).isEqualTo(0);
	}

	private Region saveRegion1() {

		Region region = TestDataHelper.getRegion1();
		Region saved = regionRepository.save(region);
		entityManager.flush();
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
				assertThat(gps.getLatitude()).isEqualTo(region2.getGps().get(i).getLatitude());
				assertThat(gps.getLongitude()).isEqualTo(region2.getGps().get(i).getLongitude());
				i++;
			}
		}
	}
}
