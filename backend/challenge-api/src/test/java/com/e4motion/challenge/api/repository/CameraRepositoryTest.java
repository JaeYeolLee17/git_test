package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.domain.Camera;
import com.e4motion.challenge.api.domain.CameraRoad;
import com.e4motion.challenge.api.domain.Intersection;
import com.e4motion.challenge.api.domain.Region;
import com.e4motion.challenge.api.mapper.CameraMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CameraRepositoryTest {

    @Autowired
    RegionRepository regionRepository;

    @Autowired
    IntersectionRepository intersectionRepository;

    @Autowired
    CameraRepository cameraRepository;

    @Autowired
    CameraRoadRepository cameraRoadRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    CameraMapper cameraMapper;

    @Test
    void save_withRoad() {

        // camera1 in intersection1 in region1, camera2 in intersection2 in region2.
        Region region1 = regionRepository.save(TestDataHelper.getRegion1());
        Region region2 = regionRepository.save(TestDataHelper.getRegion2());

        Intersection intersection1 = TestDataHelper.getIntersection1();
        intersection1.setRegion(region1);
        intersection1 = intersectionRepository.save(intersection1);
        Intersection intersection2 = TestDataHelper.getIntersection2();
        intersection2.setRegion(region2);
        intersection2 = intersectionRepository.save(intersection2);

        Camera camera1 = TestDataHelper.getCamera1();
        camera1.setIntersection(intersection1);
        camera1.setDirection(intersection2);
        camera1 = cameraRepository.save(camera1);

        entityManager.flush();
        entityManager.clear();

        assertThat(cameraRepository.count()).isEqualTo(1);

        Optional<Camera> found = cameraRepository.findByCameraNo(camera1.getCameraNo());
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getRoad().getCamera().getCameraId()).isEqualTo(camera1.getCameraId());

        Optional<CameraRoad> cameraRoad = cameraRoadRepository.findByCamera_CameraNo(found.get().getCameraNo());
        assertThat(cameraRoad.isPresent()).isTrue();
        assertThat(cameraRoad.get().getCamera().getCameraNo()).isEqualTo(camera1.getCameraNo());
    }

    @Test
    void save_withRoadNull() {

        // save camera1
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

        Camera camera1 = TestDataHelper.getCamera1();
        camera1.setIntersection(intersection1);
        camera1.setDirection(intersection2);
        camera1.setRoad(null);
        camera1 = cameraRepository.save(camera1);
        entityManager.flush();
        entityManager.clear();

        Optional<Camera> found = cameraRepository.findByCameraNo(camera1.getCameraNo());
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getRoad()).isNull();

        Optional<CameraRoad> cameraRoad = cameraRoadRepository.findByCamera_CameraNo(found.get().getCameraNo());
        assertThat(cameraRoad.isPresent()).isFalse();
    }

    @Test
    void update_withRoad() throws JsonProcessingException {

        // camera1 in intersection1 in region1, camera2 in intersection2 in region2.
        Region region1 = regionRepository.save(TestDataHelper.getRegion1());
        Region region2 = regionRepository.save(TestDataHelper.getRegion2());

        Intersection intersection1 = TestDataHelper.getIntersection1();
        intersection1.setRegion(region1);
        intersection1 = intersectionRepository.save(intersection1);
        Intersection intersection2 = TestDataHelper.getIntersection2();
        intersection2.setRegion(region2);
        intersection2 = intersectionRepository.save(intersection2);

        Camera camera1 = TestDataHelper.getCamera1();
        camera1.setIntersection(intersection1);
        camera1.setDirection(intersection2);
        camera1 = cameraRepository.save(camera1);

        entityManager.flush();
        entityManager.clear();

        assertThat(cameraRepository.count()).isEqualTo(1);

        Optional<Camera> found = cameraRepository.findByCameraNo(camera1.getCameraNo());
        assertThat(found.isPresent()).isTrue();

        // 이미 Road 정보가 존재하는 경우 found entity 내 road 정보를 수정한다.
        CameraRoad cameraRoad = cameraMapper.toCameraRoad(TestDataHelper.getCameraDto2().getRoad());
        found.get().getRoad().setStartLine(cameraRoad.getStartLine());
        found.get().getRoad().setLane(cameraRoad.getLane());
        found.get().getRoad().setUturn(cameraRoad.getUturn());
        found.get().getRoad().setCrosswalk(cameraRoad.getCrosswalk());
        found.get().getRoad().setDirection(cameraRoad.getDirection());
        camera1 = cameraRepository.save(found.get());
        entityManager.flush();
        entityManager.clear();

        found = cameraRepository.findByCameraNo(camera1.getCameraNo());
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getRoad().getStartLine()).isEqualTo(cameraRoad.getStartLine());
        assertThat(found.get().getRoad().getLane()).isEqualTo(cameraRoad.getLane());
        assertThat(found.get().getRoad().getUturn()).isEqualTo(cameraRoad.getUturn());
        assertThat(found.get().getRoad().getCrosswalk()).isEqualTo(cameraRoad.getCrosswalk());
        assertThat(found.get().getRoad().getDirection()).isEqualTo(cameraRoad.getDirection());
    }

    @Test
    void update_withRoad_WhenNull() throws JsonProcessingException {

        // save camera1
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

        Camera camera1 = TestDataHelper.getCamera1();
        camera1.setIntersection(intersection1);
        camera1.setDirection(intersection2);
        camera1.setRoad(null);
        camera1 = cameraRepository.save(camera1);
        entityManager.flush();
        entityManager.clear();

        Optional<Camera> found = cameraRepository.findByCameraNo(camera1.getCameraNo());
        assertThat(found.isPresent()).isTrue();

        // Road 정보가 없었던 경우 새 road entity 를 생성하여 camera 칼럼을 채운 후 저장한다.
        CameraRoad cameraRoad = cameraMapper.toCameraRoad(TestDataHelper.getCameraDto2().getRoad());
        cameraRoad.setCamera(camera1);      // camera 칼럼 연결.
        assertThat(found.get().getRoad()).isNull();
        found.get().setRoad(cameraRoad);
        camera1 = cameraRepository.save(found.get());
        entityManager.flush();
        entityManager.clear();

        found = cameraRepository.findByCameraNo(camera1.getCameraNo());
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getRoad().getStartLine()).isEqualTo(cameraRoad.getStartLine());
        assertThat(found.get().getRoad().getLane()).isEqualTo(cameraRoad.getLane());
        assertThat(found.get().getRoad().getUturn()).isEqualTo(cameraRoad.getUturn());
        assertThat(found.get().getRoad().getCrosswalk()).isEqualTo(cameraRoad.getCrosswalk());
        assertThat(found.get().getRoad().getDirection()).isEqualTo(cameraRoad.getDirection());
    }

    @Test
    void findAllByRegionNoIntersectionNo() {

        // camera1 in intersection1 in region1, camera2 in intersection2 in region2.
        Region region1 = regionRepository.save(TestDataHelper.getRegion1());
        Region region2 = regionRepository.save(TestDataHelper.getRegion2());

        Intersection intersection1 = TestDataHelper.getIntersection1();
        intersection1.setRegion(region1);
        intersection1 = intersectionRepository.save(intersection1);
        Intersection intersection2 = TestDataHelper.getIntersection2();
        intersection2.setRegion(region2);
        intersection2 = intersectionRepository.save(intersection2);

        Camera camera1 = TestDataHelper.getCamera1();
        camera1.setIntersection(intersection1);
        camera1.setDirection(intersection2);
        camera1 = cameraRepository.save(camera1);
        Camera camera2 = TestDataHelper.getCamera2();
        camera2.setIntersection(intersection2);
        camera1.setDirection(intersection1);
        camera2 = cameraRepository.save(camera2);

        entityManager.flush();
        entityManager.clear();

        assertThat(cameraRepository.count()).isEqualTo(2);

        List<Camera> cameras = cameraRepository.findAllByRegionNoIntersectionNo(null, null);
        assertThat(cameras.size()).isEqualTo(2);

        cameras = cameraRepository.findAllByRegionNoIntersectionNo(region1.getRegionNo(), null);
        assertThat(cameras.size()).isEqualTo(1);
        cameras = cameraRepository.findAllByRegionNoIntersectionNo(region1.getRegionNo(), intersection1.getIntersectionNo());
        assertThat(cameras.size()).isEqualTo(1);
        cameras = cameraRepository.findAllByRegionNoIntersectionNo(null, intersection1.getIntersectionNo());
        assertThat(cameras.size()).isEqualTo(1);

        cameras = cameraRepository.findAllByRegionNoIntersectionNo(region2.getRegionNo(), null);
        assertThat(cameras.size()).isEqualTo(1);
        cameras = cameraRepository.findAllByRegionNoIntersectionNo(region2.getRegionNo(), intersection2.getIntersectionNo());
        assertThat(cameras.size()).isEqualTo(1);
        cameras = cameraRepository.findAllByRegionNoIntersectionNo(null, intersection2.getIntersectionNo());
        assertThat(cameras.size()).isEqualTo(1);

        // camera1, camera2 in intersection1 in region1, no camera in intersection2 in region2.
        camera2.setIntersection(intersection1);
        camera2 = cameraRepository.saveAndFlush(camera2);
        entityManager.clear();

        cameras = cameraRepository.findAllByRegionNoIntersectionNo(region1.getRegionNo(), null);
        assertThat(cameras.size()).isEqualTo(2);
        cameras = cameraRepository.findAllByRegionNoIntersectionNo(region1.getRegionNo(), intersection1.getIntersectionNo());
        assertThat(cameras.size()).isEqualTo(2);
        cameras = cameraRepository.findAllByRegionNoIntersectionNo(null, intersection1.getIntersectionNo());
        assertThat(cameras.size()).isEqualTo(2);

        cameras = cameraRepository.findAllByRegionNoIntersectionNo(region2.getRegionNo(), null);
        assertThat(cameras.size()).isEqualTo(0);
        cameras = cameraRepository.findAllByRegionNoIntersectionNo(region2.getRegionNo(), intersection2.getIntersectionNo());
        assertThat(cameras.size()).isEqualTo(0);
        cameras = cameraRepository.findAllByRegionNoIntersectionNo(null, intersection2.getIntersectionNo());
        assertThat(cameras.size()).isEqualTo(0);
    }
}