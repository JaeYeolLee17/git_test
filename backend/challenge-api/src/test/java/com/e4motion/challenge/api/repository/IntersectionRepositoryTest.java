package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.domain.Intersection;
import com.e4motion.challenge.api.domain.Region;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class IntersectionRepositoryTest {

    @Autowired
    RegionRepository regionRepository;

    @Autowired
    IntersectionRepository intersectionRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void findAllByRegion_RegionNo() {

        // intersection1 in region1, intersection2 in region2.
        Region region1 = TestDataHelper.getRegion1();
        region1 = regionRepository.save(region1);
        Region region2 = TestDataHelper.getRegion2();
        region2 = regionRepository.save(region2);

        Intersection intersection1 = TestDataHelper.getIntersection1();
        intersection1.setRegion(region1);
        intersection1 = intersectionRepository.save(intersection1);
        Intersection intersection2 = TestDataHelper.getIntersection2();
        intersection2.setRegion(region2);
        intersection2 = intersectionRepository.save(intersection2);
        entityManager.flush();
        entityManager.clear();

        assertThat(intersectionRepository.count()).isEqualTo(2);
        assertThat(intersectionRepository.findAll().size()).isEqualTo(2);

        List<Intersection> intersections = intersectionRepository.findAllByRegion_RegionNo(region1.getRegionNo(), null);
        assertThat(intersections.size()).isEqualTo(1);
        intersections = intersectionRepository.findAllByRegion_RegionNo(region2.getRegionNo(), null);
        assertThat(intersections.size()).isEqualTo(1);

        // intersection1, intersection2 in region1, no intersection in region2
        intersection2.setRegion(region1);
        intersection2 = intersectionRepository.saveAndFlush(intersection2);
        entityManager.clear();

        intersections = intersectionRepository.findAllByRegion_RegionNo(region1.getRegionNo(), null);
        assertThat(intersections.size()).isEqualTo(2);
        intersections = intersectionRepository.findAllByRegion_RegionNo(region2.getRegionNo(), null);
        assertThat(intersections.size()).isEqualTo(0);
    }
}