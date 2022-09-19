package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.domain.*;
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
class LinkRepositoryTest {

	@Autowired
	LinkRepository linkRepository;

	@Autowired
	LinkGpsRepository linkGpsRepository;

	@Autowired
	RegionRepository regionRepository;

	@Autowired
	IntersectionRepository intersectionRepository;

	@Autowired
	EntityManager entityManager;

	Region region1;
	Region region2;
	Intersection intersection1;
	Intersection intersection2;

	@Test
	void save_withLinkGps() {

		Link link = saveLink1();

		Optional<Link> found = linkRepository.findByStart_IntersectionNoAndEnd_IntersectionNo(link.getStart().getIntersectionNo(), link.getEnd().getIntersectionNo());
		assertThat(found.isPresent()).isTrue();
		assertThat(found.get().getGps().size()).isEqualTo(link.getGps().size());

		assertEquals(found.get(), link);

		List<LinkGps> linkGps = linkGpsRepository.findAllByLink_LinkIdOrderByGpsOrder(link.getLinkId());
		assertThat(linkGps.size()).isEqualTo(link.getGps().size());
	}

	@Test
	void save_withNullLinkGps() {

		saveRegion1_2();
		saveIntersection1_2();

		Link link = TestDataHelper.getLink1();
		link.setStart(intersection1);
		link.setEnd(intersection2);
		link.setGps(null);
		link = linkRepository.save(link);

		linkRepository.saveAndFlush(link);
		entityManager.clear();

		// gps 목록을 null 로 저장해도 region gps 가 null 이 아닌 empty list 로 조회된다.
		Optional<Link> found = linkRepository.findByStart_IntersectionNoAndEnd_IntersectionNo(link.getStart().getIntersectionNo(), link.getEnd().getIntersectionNo());
		assertThat(found.isPresent()).isTrue();
		assertThat(found.get().getGps().size()).isEqualTo(0);

		assertEquals(found.get(), link);

		List<LinkGps> linkGps = linkGpsRepository.findAllByLink_LinkIdOrderByGpsOrder(link.getLinkId());
		assertThat(linkGps.size()).isEqualTo(0);
	}

	@Test
	void save_withEmptyLinkGps() {

		saveRegion1_2();
		saveIntersection1_2();

		Link link = TestDataHelper.getLink1();
		link.setStart(intersection1);
		link.setEnd(intersection2);
		link.getGps().clear();
		link = linkRepository.save(link);

		linkRepository.saveAndFlush(link);
		entityManager.clear();

		Optional<Link> found = linkRepository.findByStart_IntersectionNoAndEnd_IntersectionNo(link.getStart().getIntersectionNo(), link.getEnd().getIntersectionNo());
		assertThat(found.isPresent()).isTrue();
		assertThat(found.get().getGps().size()).isEqualTo(0);

		assertEquals(found.get(), link);

		List<LinkGps> linkGps = linkGpsRepository.findAllByLink_LinkIdOrderByGpsOrder(link.getLinkId());
		assertThat(linkGps.size()).isEqualTo(0);
	}

	@Test
	void save_withDisorderedLinkGps() {

		saveRegion1_2();
		saveIntersection1_2();

		Link link = TestDataHelper.getLink1();
		link.setStart(intersection1);
		link.setEnd(intersection2);
		List<LinkGps> gps = link.getGps();
		gps.get(0).setGpsOrder(2);
		gps.get(1).setGpsOrder(1);
		gps.get(2).setGpsOrder(0);

		linkRepository.saveAndFlush(link);
		entityManager.clear();

		Optional<Link> found = linkRepository.findByStart_IntersectionNoAndEnd_IntersectionNo(link.getStart().getIntersectionNo(), link.getEnd().getIntersectionNo());
		assertThat(found.isPresent()).isTrue();
		assertThat(found.get().getGps().size()).isEqualTo(link.getGps().size());

		// check ordered
		assertThat(gps.get(2).getLat()).isEqualTo(found.get().getGps().get(0).getLat());
		assertThat(gps.get(2).getLng()).isEqualTo(found.get().getGps().get(0).getLng());
		assertThat(gps.get(1).getLat()).isEqualTo(found.get().getGps().get(1).getLat());
		assertThat(gps.get(1).getLng()).isEqualTo(found.get().getGps().get(1).getLng());
		assertThat(gps.get(0).getLat()).isEqualTo(found.get().getGps().get(2).getLat());
		assertThat(gps.get(0).getLng()).isEqualTo(found.get().getGps().get(2).getLng());
	}

	@Test
	void save_sameLinkGps() {

		saveRegion1_2();
		saveIntersection1_2();

		Link link = TestDataHelper.getLink1();
		link.setStart(intersection1);
		link.setEnd(intersection2);

		link.getGps().get(1).setLat(link.getGps().get(0).getLat()); 	// first == second
		link.getGps().get(1).setLng(link.getGps().get(0).getLng());

		assertThrows(DataIntegrityViolationException.class, () -> linkRepository.saveAndFlush(link));
	}

	@Test
	void save_sameLinkGpsOrder() {

		saveRegion1_2();
		saveIntersection1_2();

		Link link = TestDataHelper.getLink1();
		link.setStart(intersection1);
		link.setEnd(intersection2);

		link.getGps().get(1).setGpsOrder(link.getGps().get(0).getGpsOrder()); 	// first == second

		assertThrows(DataIntegrityViolationException.class, () -> linkRepository.saveAndFlush(link));
	}

	@Test
	void update_withAnotherGps() {

		Link link = saveLink1();

		Optional<Link> found = linkRepository.findByStart_IntersectionNoAndEnd_IntersectionNo(link.getStart().getIntersectionNo(), link.getEnd().getIntersectionNo());
		assertThat(found.isPresent()).isTrue();

		// new gps
		List<LinkGps> gps = new ArrayList<>();
		gps.add(LinkGps.builder().link(found.get()).lat(35.9999).lng(128.99999).gpsOrder(1).build());
		gps.add(LinkGps.builder().link(found.get()).lat(35.88888).lng(128.88888).gpsOrder(2).build());
		gps.add(LinkGps.builder().link(found.get()).lat(35.33333).lng(128.33333).gpsOrder(3).build());

		// gps 목록 수정 시 목록 자체를 대체하면 안된다. list 를 clear 후 add 하여 저장한다.
		// 만약 기존 목록에서 동일한 위도, 경도(unique 위반) 가 있을 경우 모두 제거 후 flush 한 다음 다시 목록 채워서 저장해야 한다.
		found.get().getGps().clear();
		linkRepository.saveAndFlush(found.get());

		found.get().getGps().addAll(gps);
		linkRepository.saveAndFlush(found.get());
		entityManager.clear();

		found = linkRepository.findByStart_IntersectionNoAndEnd_IntersectionNo(link.getStart().getIntersectionNo(), link.getEnd().getIntersectionNo());
		assertThat(found.isPresent()).isTrue();

		assertThat(found.get().getGps().size()).isEqualTo(3);
		assertThat(found.get().getGps().get(0).getLat()).isEqualTo(gps.get(0).getLat());
		assertThat(found.get().getGps().get(0).getLng()).isEqualTo(gps.get(0).getLng());
		assertThat(found.get().getGps().get(1).getLat()).isEqualTo(gps.get(1).getLat());
		assertThat(found.get().getGps().get(1).getLng()).isEqualTo(gps.get(1).getLng());
		assertThat(found.get().getGps().get(2).getLat()).isEqualTo(gps.get(2).getLat());
		assertThat(found.get().getGps().get(2).getLng()).isEqualTo(gps.get(2).getLng());

		List<LinkGps> regionGps = linkGpsRepository.findAllByLink_LinkIdOrderByGpsOrder(link.getLinkId());
		assertThat(regionGps.size()).isEqualTo(3);
	}

	@Test
	void update_withEmptyGps() {

		Link link = saveLink1();

		Optional<Link> found = linkRepository.findByStart_IntersectionNoAndEnd_IntersectionNo(link.getStart().getIntersectionNo(), link.getEnd().getIntersectionNo());
		assertThat(found.isPresent()).isTrue();

		// update start, end, empty gps
		found.get().setStart(intersection2);
		found.get().setEnd(intersection1);
		found.get().getGps().clear();			// gps 목록 수정 시 목록 자체를 대체하면 안된다. list 를 삭제하고자 한다면 목록을 clear 하여 저장한다.
		//found.get().setGps(null);				// gps 목록을 지우고자 set null 하면 안된다.

		linkRepository.saveAndFlush(found.get());
		entityManager.clear();

		found = linkRepository.findByStart_IntersectionNoAndEnd_IntersectionNo(link.getEnd().getIntersectionNo(), link.getStart().getIntersectionNo());
		assertThat(found.isPresent()).isTrue();

		assertThat(found.get().getStart().getIntersectionNo()).isEqualTo(intersection2.getIntersectionNo());
		assertThat(found.get().getEnd().getIntersectionNo()).isEqualTo(intersection1.getIntersectionNo());
		assertThat(found.get().getGps().size()).isEqualTo(0);

		List<LinkGps> regionGps = linkGpsRepository.findAllByLink_LinkIdOrderByGpsOrder(link.getLinkId());
		assertThat(regionGps.size()).isEqualTo(0);
	}

	@Test
	void deleteByStart_IntersectionNoAndEnd_IntersectionNo() {

		Link link = saveLink1();

		linkRepository.deleteByStart_IntersectionNoAndEnd_IntersectionNo(link.getStart().getIntersectionNo(), link.getEnd().getIntersectionNo());
		entityManager.flush();
		entityManager.clear();

		Optional<Link> found = linkRepository.findByStart_IntersectionNoAndEnd_IntersectionNo(link.getStart().getIntersectionNo(), link.getEnd().getIntersectionNo());
		assertThat(linkRepository.count()).isEqualTo(0);
        assertThat(found.isPresent()).isFalse();

		List<LinkGps> regionGps = linkGpsRepository.findAllByLink_LinkIdOrderByGpsOrder(link.getLinkId());
		assertThat(regionGps.size()).isEqualTo(0);
	}

	private void saveRegion1_2() {

		region1 = TestDataHelper.getRegion1();
		region1 = regionRepository.save(region1);

		region2 = TestDataHelper.getRegion2();
		region2 = regionRepository.save(region2);
		entityManager.flush();
		entityManager.clear();

		assertThat(regionRepository.count()).isEqualTo(2);
	}

	private void saveIntersection1_2() {

		intersection1 = TestDataHelper.getIntersection1();
		intersection1.setRegion(region1);
		intersection1 = intersectionRepository.save(intersection1);

		intersection2 = TestDataHelper.getIntersection2();
		intersection2.setRegion(region2);
		intersection2 = intersectionRepository.save(intersection2);
		entityManager.flush();
		//entityManager.clear();	// Do not clear here to get start, end for link later

		assertThat(intersectionRepository.count()).isEqualTo(2);
	}

	private Link saveLink1() {

		saveRegion1_2();
		saveIntersection1_2();

		Link link = TestDataHelper.getLink1();
		link.setStart(intersection1);
		link.setEnd(intersection2);
		Link saved = linkRepository.saveAndFlush(link);
		entityManager.clear();

		assertThat(linkRepository.count()).isEqualTo(1);
		return saved;
	}

	private void assertEquals(Link link1, Link link2) {

		assertThat(link1.getStart().getIntersectionNo()).isEqualTo(link2.getStart().getIntersectionNo());
		assertThat(link1.getEnd().getIntersectionNo()).isEqualTo(link2.getEnd().getIntersectionNo());
		if (link1.getGps() == null) {
			assertThat(link2.getGps()).isNull();
		} else {
			int i = 0;
			for (LinkGps gps : link1.getGps()) {
				assertThat(gps.getLat()).isEqualTo(link2.getGps().get(i).getLat());
				assertThat(gps.getLng()).isEqualTo(link2.getGps().get(i).getLng());
				i++;
			}
		}
	}
}
