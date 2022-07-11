package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.domain.Camera;

import java.util.List;

public interface CameraRepositoryCustom  {

    List<Camera> findAllByRegionNoIntersectionNo(String regionNo, String intersectionNo);

}
