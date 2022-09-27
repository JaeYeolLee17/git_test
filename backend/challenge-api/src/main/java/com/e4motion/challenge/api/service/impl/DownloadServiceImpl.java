package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.domain.*;
import com.e4motion.challenge.api.repository.CameraRepository;
import com.e4motion.challenge.api.repository.IntersectionRepository;
import com.e4motion.challenge.api.repository.LinkRepository;
import com.e4motion.challenge.api.repository.RegionRepository;
import com.e4motion.challenge.api.service.DownloadService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class DownloadServiceImpl implements DownloadService {

    private final RegionRepository regionRepository;
    private final IntersectionRepository intersectionRepository;
    private final LinkRepository linkRepository;
    private final CameraRepository cameraRepository;


    @Transactional
    public ResponseEntity<byte[]> downloadRegion(HttpServletResponse response) throws IOException {

        String csvFileName = "regions.csv";
        String[] csvHeader = {"region_no", "region_name", "lat", "lng"};

        List<List<Object>> csvBody = new ArrayList<>();
        List<Region> allRegion = regionRepository.findAll();

        String lastNo = null;

        for (Region region : allRegion) {
            for (RegionGps regionGps : region.getGps()) {
                if (Objects.equals(lastNo, region.getRegionNo())) {
                    csvBody.add(Arrays.asList("",
                            "",
                            regionGps.getLat(),
                            regionGps.getLng()));

                } else {
                    csvBody.add(Arrays.asList(region.getRegionNo(),
                            region.getRegionName(),
                            regionGps.getLat(),
                            regionGps.getLng()));
                }
                lastNo = region.getRegionNo();
            }
        }

        return makeCsv(csvFileName, csvHeader, csvBody);
    }

    @Transactional
    public ResponseEntity<byte[]> downloadIntersection(HttpServletResponse response) throws IOException {

        String csvFileName = "intersections.csv";
        String[] csvHeader = {"intersection_no", "intersection_name", "lat", "lng", "region_no", "national_id"};

        List<List<Object>> csvBody = new ArrayList<>();

        List<Intersection> allIntersection = intersectionRepository.findAll();

        for (Intersection intersection : allIntersection) {
            if (intersection.getRegion() == null) {
                csvBody.add(Arrays.asList(intersection.getIntersectionNo(),
                        intersection.getIntersectionName(),
                        intersection.getLat(),
                        intersection.getLng(),
                        null,
                        intersection.getNationalId()));
            } else {
                csvBody.add(Arrays.asList(intersection.getIntersectionNo(),
                        intersection.getIntersectionName(),
                        intersection.getLat(),
                        intersection.getLng(),
                        intersection.getRegion().getRegionNo(),
                        intersection.getNationalId()));
            }
        }

        return makeCsv(csvFileName, csvHeader, csvBody);
    }

    @Transactional
    public ResponseEntity<byte[]> downloadLink(HttpServletResponse response) throws IOException {

        String csvFileName = "links.csv";
        String[] csvHeader = {"start_no", "start_name", "end_no", "end_name", "lat", "lng"};

        List<List<Object>> csvBody = new ArrayList<>();
        List<Link> allLink = linkRepository.findAll();

        String lastStartNo = null;
        String lastEndNo = null;
        for (Link link : allLink) {
            for (LinkGps linkGps : link.getGps()) {
                if (Objects.equals(lastStartNo, link.getStart().getIntersectionNo()) &&
                        Objects.equals(lastEndNo, link.getEnd().getIntersectionNo())) {
                    csvBody.add(Arrays.asList(null,
                            null,
                            null,
                            null,
                            linkGps.getLat(),
                            linkGps.getLng()));
                } else {
                    csvBody.add(Arrays.asList(link.getStart().getIntersectionNo(),
                            link.getStart().getIntersectionName(),
                            link.getEnd().getIntersectionNo(),
                            link.getEnd().getIntersectionName(),
                            linkGps.getLat(),
                            linkGps.getLng()));
                }
                lastStartNo = link.getStart().getIntersectionNo();
                lastEndNo = link.getEnd().getIntersectionNo();
            }
        }

        return makeCsv(csvFileName, csvHeader, csvBody);
    }

    @Transactional
    public ResponseEntity<byte[]> downloadCamera(HttpServletResponse response) throws IOException {

        String csvFileName = "cameras.csv";
        String[] csvHeader = {"camera_no,password","intersection_no","direction_no","lat","lng","distance","rtsp_url","rtsp_id","rtsp_password","server_url","send_cycle","collect_cycle","s_width","s_height","l_width","l_height","degree","start_line","lane","uturn","crosswalk","direction"};

        List<List<Object>> csvBody = new ArrayList<>();

        List<Camera> allCamera = cameraRepository.findAll();

        for (Camera camera : allCamera) {
            csvBody.add(Arrays.asList(camera.getCameraNo(),
                    camera.getPassword(),
                    camera.getIntersection().getIntersectionNo(),
                    camera.getDirection().getIntersectionNo(),
                    camera.getLat(),
                    camera.getLng(),
                    camera.getDistance(),
                    camera.getRtspUrl(),
                    camera.getRtspId(),
                    camera.getRtspPassword(),
                    camera.getServerUrl(),
                    camera.getSendCycle(),
                    camera.getCollectCycle(),
                    camera.getSmallWidth(),
                    camera.getSmallHeight(),
                    camera.getLargeWidth(),
                    camera.getLargeHeight(),
                    camera.getDegree(),
                    camera.getRoad().getStartLine(),
                    camera.getRoad().getLane(),
                    camera.getRoad().getUturn(),
                    camera.getRoad().getCrosswalk(),
                    camera.getRoad().getDirection()));
        }

        return makeCsv(csvFileName, csvHeader, csvBody);
    }

    private static ResponseEntity<byte[]> makeCsv(String csvFileName, String[] csvHeader, List<List<Object>> csvBody) throws IOException {

        StringWriter sw = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(sw, CSVFormat.DEFAULT.withHeader(csvHeader));

        for (List<Object> record : csvBody) {
            csvPrinter.printRecord(record);
        }

        sw.flush();

        byte[] csvFile = sw.toString().getBytes(StandardCharsets.UTF_8);
        HttpHeaders header = new HttpHeaders();
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + csvFileName + "\"");
        header.setContentLength(csvFile.length);
        return new ResponseEntity<>(csvFile, header, HttpStatus.OK);
    }
}
