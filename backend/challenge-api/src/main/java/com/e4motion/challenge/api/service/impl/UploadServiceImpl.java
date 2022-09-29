package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.domain.*;
import com.e4motion.challenge.api.repository.*;
import com.e4motion.challenge.api.service.UploadService;
import com.e4motion.challenge.common.exception.customexception.IntersectionNotFoundException;
import com.e4motion.challenge.common.exception.customexception.RegionNotFoundException;
import com.e4motion.challenge.common.utils.DateTimeHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service
public class UploadServiceImpl implements UploadService {

    private final static String TEXT_CSV = "text/csv";
    private final RegionRepository regionRepository;
    private final IntersectionRepository intersectionRepository;
    private final LinkRepository linkRepository;
    private final CameraRepository cameraRepository;
    private final DataStatsRepository dataStatsRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void uploadRegion(MultipartFile file) throws IOException {

        Iterable<CSVRecord> records = parseCsv(file, RegionHeaders.class);
        if (records == null) {
            return;
        }

        ArrayList<RegionGps> regionGps = new ArrayList<>();
        AtomicInteger gpsOrder = new AtomicInteger(0);
        Region region;

        String regionNo = "";
        String regionName = "";
        String lastRegionNo = "";

        for (CSVRecord record : records) {

            if (getString(record, RegionHeaders.region_no) != null) {
                regionNo = getString(record, RegionHeaders.region_no);
                regionName = getString(record, RegionHeaders.region_name);
                regionGps.clear();
            }

            Optional<Region> savedRegion = regionRepository.findByRegionNo(regionNo);

            if (savedRegion.isPresent()) {
                region = savedRegion.get();

                if (Objects.equals(regionNo, getString(record, RegionHeaders.region_no))) {
                    region.getGps().clear();
                    regionRepository.saveAndFlush(region);
                }
            } else {
                region = new Region();
                region.setGps(regionGps);
            }

            region.setRegionNo(regionNo);
            region.setRegionName(regionName);
            region.setIntersections(getIntersectionList(regionNo));

            if (!Objects.equals(lastRegionNo, regionNo)) {
                gpsOrder = new AtomicInteger(0);
            }

            region.getGps().add(RegionGps.builder()
                    .region(region)
                    .lat(getDouble(record, RegionHeaders.lat))
                    .lng(getDouble(record, RegionHeaders.lng))
                    .gpsOrder(gpsOrder.incrementAndGet())
                    .build());

            regionRepository.save(region);

            lastRegionNo = regionNo;
        }
    }

    @Transactional
    public void uploadIntersection(MultipartFile file) throws IOException {

        Iterable<CSVRecord> records = parseCsv(file, IntersectionHeaders.class);
        if (records == null) {
            return;
        }

        ArrayList<Intersection> intersections = new ArrayList<>();
        Intersection intersection;

        for (CSVRecord record : records) {

            Optional<Intersection> savedIntersection =
                    intersectionRepository.findByIntersectionNo(getString(record, IntersectionHeaders.intersection_no));

            intersection = savedIntersection.orElseGet(Intersection::new);

            intersection.setIntersectionNo(getString(record, IntersectionHeaders.intersection_no));
            intersection.setIntersectionName(getString(record, IntersectionHeaders.intersection_name));
            intersection.setLat(getDouble(record, IntersectionHeaders.lat));
            intersection.setLng(getDouble(record, IntersectionHeaders.lng));
            intersection.setNationalId(getLong(record, IntersectionHeaders.national_no));
            intersection.setRegion(getRegion(getString(record, IntersectionHeaders.region_no)));
            intersection.setCameras(getCameraList(getString(record, IntersectionHeaders.region_no),
                    getString(record, IntersectionHeaders.intersection_no)));

            intersectionRepository.save(intersection);
        }
    }

    @Transactional
    public void uploadLink(MultipartFile file) throws IOException {

        Iterable<CSVRecord> records = parseCsv(file, LinkHeaders.class);
        if (records == null) {
            return;
        }

        ArrayList<LinkGps> linkGps = new ArrayList<>();
        AtomicInteger gpsOrder = new AtomicInteger(0);
        Link link;

        String startNo = "";
        String endNo = "";
        String lastStratNo = "";

        for (CSVRecord record : records) {
            if (getString(record, LinkHeaders.start_no) != null) {
                startNo = getString(record, LinkHeaders.start_no);
                endNo = getString(record, LinkHeaders.end_no);
                linkGps.clear();
            }

            Optional<Link> savedLink = linkRepository.findByStart_IntersectionNoAndEnd_IntersectionNo(startNo, endNo);

            if (savedLink.isPresent()) {
                link = savedLink.get();

                if (Objects.equals(startNo, getString(record, LinkHeaders.start_no))) {
                    link.getGps().clear();
                    linkRepository.saveAndFlush(link);
                }
            } else {
                link = new Link();
                link.setGps(linkGps);
            }

            link.setStart(getIntersection(startNo));
            link.setEnd(getIntersection(endNo));

            if (!Objects.equals(lastStratNo, startNo)) {
                gpsOrder = new AtomicInteger(0);
            }

            link.getGps().add(LinkGps.builder()
                    .link(link)
                    .lat(getDouble(record, LinkHeaders.lat))
                    .lng(getDouble(record, LinkHeaders.lng))
                    .gpsOrder(gpsOrder.incrementAndGet())
                    .build());

            linkRepository.save(link);

            lastStratNo = startNo;
        }
    }

    @Transactional
    public void uploadCamera(MultipartFile file) throws IOException {

        Iterable<CSVRecord> records = parseCsv(file, CameraHeaders.class);
        if (records == null) {
            return;
        }

        String password = "";
        ObjectMapper mapper = new ObjectMapper();
        Camera camera;

        for (CSVRecord record : records) {

            if (getString(record, CameraHeaders.password) != null) {
                password = passwordEncoder.encode(getString(record, CameraHeaders.password));
            } else {
                password = passwordEncoder.encode("camera12!@");
            }

            Optional<Camera> savedCamera = cameraRepository.findByCameraNo(getString(record, CameraHeaders.camera_no));

            camera = savedCamera.orElseGet(Camera::new);

            camera.setCameraNo(getString(record, CameraHeaders.camera_no));
            camera.setPassword(password);
            camera.setIntersection(getIntersection(getString(record, CameraHeaders.intersection_no)));
            camera.setDirection(getIntersection(getString(record, CameraHeaders.direction_no)));
            camera.setLat(getDouble(record, CameraHeaders.lat));
            camera.setLng(getDouble(record, CameraHeaders.lng));
            camera.setDistance(getInteger(record, CameraHeaders.distance));
            camera.setRtspUrl(getString(record, CameraHeaders.rtsp_url));
            camera.setRtspId(getString(record, CameraHeaders.rtsp_id));
            camera.setRtspPassword(getString(record, CameraHeaders.rtsp_password));
            camera.setServerUrl(getString(record, CameraHeaders.server_url));
            camera.setSendCycle(getInteger(record, CameraHeaders.send_cycle));
            camera.setCollectCycle(getInteger(record, CameraHeaders.collect_cycle));
            camera.setSmallWidth(getInteger(record, CameraHeaders.s_width));
            camera.setSmallHeight(getInteger(record, CameraHeaders.s_height));
            camera.setLargeWidth(getInteger(record, CameraHeaders.l_width));
            camera.setLargeHeight(getInteger(record, CameraHeaders.l_height));
            camera.setDegree(getInteger(record, CameraHeaders.degree));
            camera.setSettingsUpdated(true);
            camera.setLastDataTime(null);

            String lane = null;
            String l = getString(record, CameraHeaders.lane);
            if (l != null) {

                lane = mapper.writeValueAsString(mapper.readValue(l.replace("{", "[").replace("}", "]"), String[].class));
            }

            String direction = null;
            String d = getString(record, CameraHeaders.direction);
            if (d != null) {
                direction = mapper.writeValueAsString(mapper.readValue(d.replace("{", "[").replace("}", "]"), Boolean[][].class));
            }

            camera.setRoad(CameraRoad.builder()
                    .camera(camera)
                    .startLine(record.get(CameraHeaders.start_line))
                    .lane(lane)
                    .uturn(record.get(CameraHeaders.uturn))
                    .crosswalk(record.get(CameraHeaders.crosswalk))
                    .direction(direction)
                    .build());

            cameraRepository.save(camera);
        }
    }

    @Transactional
    public void uploadDataStats(MultipartFile[] files) throws IOException, ParseException {

        for (MultipartFile file : files) {

            Iterable<CSVRecord> records = parseCsv(file, DataStatsHeaders.class);
            if (records == null) {
                continue;
            }

            ArrayList<DataStats> dataStats = new ArrayList<>();

            for (CSVRecord record : records) {
                dataStats.add(DataStats.builder()
                        .t(DateTimeHelper.parseLocalDateTime(record.get(DataStatsHeaders.time)))
                        .c(record.get(DataStatsHeaders.camera_no))
                        .i(record.get(DataStatsHeaders.intersection_no))
                        .r(record.get(DataStatsHeaders.region_no))
                        .p(Integer.parseInt(record.get(DataStatsHeaders.p)))
                        .sr0(Integer.parseInt(record.get(DataStatsHeaders.sr0)))
                        .sr1(Integer.parseInt(record.get(DataStatsHeaders.sr1)))
                        .sr2(Integer.parseInt(record.get(DataStatsHeaders.sr2)))
                        .sr3(Integer.parseInt(record.get(DataStatsHeaders.sr3)))
                        .sr4(Integer.parseInt(record.get(DataStatsHeaders.sr4)))
                        .qmsrLen(Integer.parseInt(record.get(DataStatsHeaders.qmsr_len)))
                        .qmsr0(Integer.parseInt(record.get(DataStatsHeaders.qmsr0)))
                        .qmsr1(Integer.parseInt(record.get(DataStatsHeaders.qmsr1)))
                        .qmsr2(Integer.parseInt(record.get(DataStatsHeaders.qmsr2)))
                        .qmsr3(Integer.parseInt(record.get(DataStatsHeaders.qmsr3)))
                        .qmsr4(Integer.parseInt(record.get(DataStatsHeaders.qmsr4)))
                        .qtsr0(Integer.parseInt(record.get(DataStatsHeaders.qtsr0)))
                        .qtsr1(Integer.parseInt(record.get(DataStatsHeaders.qtsr1)))
                        .qtsr2(Integer.parseInt(record.get(DataStatsHeaders.qtsr2)))
                        .qtsr3(Integer.parseInt(record.get(DataStatsHeaders.qtsr3)))
                        .qtsr4(Integer.parseInt(record.get(DataStatsHeaders.qtsr4)))
                        .lu0(Integer.parseInt(record.get(DataStatsHeaders.lu0)))
                        .lu1(Integer.parseInt(record.get(DataStatsHeaders.lu1)))
                        .lu2(Integer.parseInt(record.get(DataStatsHeaders.lu2)))
                        .lu3(Integer.parseInt(record.get(DataStatsHeaders.lu3)))
                        .lu4(Integer.parseInt(record.get(DataStatsHeaders.lu4)))
                        .qmluLen(Integer.parseInt(record.get(DataStatsHeaders.qmlu_len)))
                        .qmlu0(Integer.parseInt(record.get(DataStatsHeaders.qmlu0)))
                        .qmlu1(Integer.parseInt(record.get(DataStatsHeaders.qmlu1)))
                        .qmlu2(Integer.parseInt(record.get(DataStatsHeaders.qmlu2)))
                        .qmlu3(Integer.parseInt(record.get(DataStatsHeaders.qmlu3)))
                        .qmlu4(Integer.parseInt(record.get(DataStatsHeaders.qmlu4)))
                        .qtlu0(Integer.parseInt(record.get(DataStatsHeaders.qtlu0)))
                        .qtlu1(Integer.parseInt(record.get(DataStatsHeaders.qtlu1)))
                        .qtlu2(Integer.parseInt(record.get(DataStatsHeaders.qtlu2)))
                        .qtlu3(Integer.parseInt(record.get(DataStatsHeaders.qtlu3)))
                        .qtlu4(Integer.parseInt(record.get(DataStatsHeaders.qtlu4)))
                        .qtT(Integer.parseInt(record.get(DataStatsHeaders.qt_time)))
                        .build());
            }

            dataStatsRepository.saveAll(dataStats);
        }
    }

    private Integer getInteger(CSVRecord record, Enum<?> header) {
        String s = record.get(header);
        if (s != null && s.length() > 0 && !s.equals("NULL")) {
            return Integer.parseInt(s);
        }
        return null;
    }

    private Long getLong(CSVRecord record, Enum<?> header) {
        String s = record.get(header);
        if (s != null && s.length() > 0 && !s.equals("NULL")) {
            return Long.parseLong(s);
        }
        return null;
    }

    private Double getDouble(CSVRecord record, Enum<?> header) {
        String s = record.get(header);
        if (s != null && s.length() > 0 && !s.equals("NULL")) {
            return Double.parseDouble(s);
        }
        return null;
    }

    private String getString(CSVRecord record, Enum<?> header) {
        String s = record.get(header);
        if (s != null && s.length() > 0 && !s.equals("NULL")) {
            return s;
        }
        return null;
    }

    private Region getRegion(String regionNo) {

        if (regionNo != null) {
            return regionRepository.findByRegionNo(regionNo)
                    .orElseThrow(() -> new RegionNotFoundException(RegionNotFoundException.INVALID_REGION_NO));
        }
        return null;
    }

    private Intersection getIntersection(String intersectionNo) {

        if (intersectionNo != null) {
            return intersectionRepository.findByIntersectionNo(intersectionNo)
                    .orElseThrow(() -> new IntersectionNotFoundException(IntersectionNotFoundException.INVALID_INTERSECTION_NO));
        }
        return null;
    }

    private List<Intersection> getIntersectionList(String regionNo) {

        if (regionNo != null) {
            return intersectionRepository.findAllByRegion_RegionNo(regionNo, Sort.sort(Intersection.class));
        }
        return null;
    }

    private List<Camera> getCameraList(String regionNo, String intersectionNo) {

        if (regionNo != null && intersectionNo != null) {
            return cameraRepository.findAllByRegionNoIntersectionNo(regionNo, intersectionNo);
        }
        return null;
    }

    private CSVParser parseCsv(MultipartFile file, Class<? extends Enum<?>> headers) throws IOException {

        if (!TEXT_CSV.equals(file.getContentType())) {
            return null;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        return CSVFormat.DEFAULT.builder()
                .setHeader(headers)
                .setSkipHeaderRecord(true)
                .build()
                .parse(reader);
    }

    private enum RegionHeaders {
        region_no,
        region_name,
        lat,
        lng
    }

    private enum IntersectionHeaders {
        intersection_no,
        intersection_name,
        lat,
        lng,
        region_no,
        national_no
    }

    private enum LinkHeaders {
        start_no,
        start_name,
        end_no,
        end_name,
        lat,
        lng
    }

    private enum CameraHeaders {
        camera_no,
        password,
        intersection_no,
        direction_no,
        lat,
        lng,
        distance,
        rtsp_url,
        rtsp_id,
        rtsp_password,
        server_url,
        send_cycle,
        collect_cycle,
        s_width,
        s_height,
        l_width,
        l_height,
        degree,
        start_line,
        lane,
        uturn,
        crosswalk,
        direction
    }

    private enum DataStatsHeaders {
        time,
        camera_no,
        intersection_no,
        region_no,
        p,
        sr0,
        sr1,
        sr2,
        sr3,
        sr4,
        qmsr_len,
        qmsr0,
        qmsr1,
        qmsr2,
        qmsr3,
        qmsr4,
        qtsr0,
        qtsr1,
        qtsr2,
        qtsr3,
        qtsr4,
        lu0,
        lu1,
        lu2,
        lu3,
        lu4,
        qmlu_len,
        qmlu0,
        qmlu1,
        qmlu2,
        qmlu3,
        qmlu4,
        qtlu0,
        qtlu1,
        qtlu2,
        qtlu3,
        qtlu4,
        qt_time
    }
}
