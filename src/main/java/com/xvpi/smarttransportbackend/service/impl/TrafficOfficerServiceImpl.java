package com.xvpi.smarttransportbackend.service.impl;

import com.xvpi.smarttransportbackend.config.GeoUtils;
import com.xvpi.smarttransportbackend.config.JwtTokenUtil;
import com.xvpi.smarttransportbackend.entity.AICommand;
import com.xvpi.smarttransportbackend.entity.DispatchTask;
import com.xvpi.smarttransportbackend.entity.RoadSection;
import com.xvpi.smarttransportbackend.entity.TrafficOfficer;
import com.xvpi.smarttransportbackend.repository.AICommandRepository;
import com.xvpi.smarttransportbackend.repository.DispatchTaskRepository;
import com.xvpi.smarttransportbackend.repository.RoadSectionRepository;
import com.xvpi.smarttransportbackend.repository.TrafficOfficerRepository;
import com.xvpi.smarttransportbackend.service.RoadSectionService;
import com.xvpi.smarttransportbackend.service.TotalRecordService;
import com.xvpi.smarttransportbackend.service.TrafficOfficerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TrafficOfficerServiceImpl implements TrafficOfficerService {
    @Autowired
    private TrafficOfficerRepository officerRepository;
    @Autowired private AICommandRepository aiCommandRepo;
    @Autowired private RoadSectionService roadSectionService;
    @Autowired private DispatchTaskRepository taskRepo;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    public TrafficOfficer register(TrafficOfficer officer) {
        if (officer.getPassword() == null || officer.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        officer.setPassword(passwordEncoder.encode(officer.getPassword()));
        officer.setStatus(1);
        return officerRepository.save(officer);
    }

    @Override
    public String login(String username, String rawPassword) {
        TrafficOfficer officer = officerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("账号不存在"));
        System.out.println("登录输入密码: " + rawPassword);
        System.out.println("数据库中密码: " + officer.getPassword());
        System.out.println("匹配结果: " + passwordEncoder.matches(rawPassword, officer.getPassword()));

        if (!passwordEncoder.matches(rawPassword, officer.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        return jwtTokenUtil.generateToken(officer);
    }

    @Override
    public TrafficOfficer getCurrentOfficer(HttpServletRequest request) {
            String token = jwtTokenUtil.resolveToken(request);
            if (token == null) {
                throw new RuntimeException("Token is missing");
            }

            String username = jwtTokenUtil.getUsername(token);
            if (username == null) {
                throw new RuntimeException("Invalid token");
            }

            return officerRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Officer not found"));
        }
        @Autowired
        DispatchTaskRepository dispatchTaskRepository;
        @Override
        public List<DispatchTask> getCurrentTasks(Long officerId){
            return dispatchTaskRepository.findByOfficerId(officerId);
        }
        @Override
         public List<TrafficOfficer>  getAllOfficers(){
            return officerRepository.findAllByOrderByIdAsc();
         }
    @Override
    public TrafficOfficer addOfficer(TrafficOfficer officer) {
        officer.setPassword(passwordEncoder.encode(officer.getPassword()));
        return officerRepository.save(officer);
    }

    @Override
    public TrafficOfficer updateOfficer(TrafficOfficer officer) {
        TrafficOfficer existing = officerRepository.findById(officer.getId())
                .orElseThrow(() -> new RuntimeException("Officer not found"));
        existing.setName(officer.getName());
        existing.setPhone(officer.getPhone());
        existing.setStatus(officer.getStatus());
        return officerRepository.save(existing);
    }

    @Override
    public void deleteOfficer(Long id) {
        officerRepository.deleteById(id);
    }

    @Override
    public void resetPassword(Long id, String newPassword) {
        TrafficOfficer officer = officerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Officer not found"));
        officer.setPassword(passwordEncoder.encode(newPassword));
        officerRepository.save(officer);
    }

    @Override
    public void updatePositionById(Long id, Double currentLat, Double currentLng) {
        // 查找交警
        Optional<TrafficOfficer> officerOptional = officerRepository.findById(id);
        if (officerOptional.isPresent()) {
            TrafficOfficer officer = officerOptional.get();
            officer.setCurrentLat(currentLat);
            officer.setCurrentLng(currentLng);
            officer.setLastUpdateTime(LocalDateTime.now()); // 更新位置时更新最后更新时间
            officerRepository.save(officer); // 保存更新
        } else {
            throw new RuntimeException("Traffic officer not found with id: " + id);
        }
    }
    @Override
    public boolean assignTask(Long commandId) {
        AICommand command = aiCommandRepo.findById(commandId).orElseThrow();

        if (command.getProcessed() >= command.getOfficerCount()) return false;

        String[] parts = command.getRoute().split("-");
        RoadSection road = roadSectionService.getByOAndDName(parts[0], parts[1]);
        if (road == null) return false;

        String[] originCoords = road.getOGis().split(",");
        double roadLng = Double.parseDouble(originCoords[0]);
        double roadLat = Double.parseDouble(originCoords[1]);

        List<TrafficOfficer> freeOfficers = officerRepository.findByStatus(1);
        if (freeOfficers.isEmpty()) return false;

        TrafficOfficer nearest = freeOfficers.stream()
                .min(Comparator.comparingDouble(officer ->
                        GeoUtils.calcDistance(officer.getCurrentLat(), officer.getCurrentLng(), roadLat, roadLng)))
                .orElse(null);

        if (nearest == null) return false;

        DispatchTask task = new DispatchTask();
        task.setOfficerId(nearest.getId());
        task.setCommandId(commandId);
        task.setStatus(0);//任务还没有被完成或接受所以暂时为0
        task.setAssignTime(LocalDateTime.now());
        taskRepo.save(task);

        nearest.setStatus(2); // 设置交警为“任务分配中”
        officerRepository.save(nearest);

        command.setProcessed(command.getProcessed() + 1);//该指令的分配人数+1
        aiCommandRepo.save(command);

        return true;
    }

    @Override
    public void cancelTask(Long taskId) {
        DispatchTask task = taskRepo.findById(taskId).orElseThrow(() -> new RuntimeException("任务不存在"));
        Long officerId = task.getOfficerId();
        Long commandId = task.getCommandId();
        TrafficOfficer officer =officerRepository.findById(officerId).orElseThrow(() -> new RuntimeException("交警不存在"));
        officer.setStatus(1);//交警空闲了
        task.setStatus(0);//任务仍不被接受
        AICommand command = aiCommandRepo.findById(commandId).orElseThrow(() -> new RuntimeException("指令不存在"));
        command.setProcessed(command.getProcessed()-1);//已分配任务数少1
        taskRepo.deleteById(taskId);
        officerRepository.save(officer);
        aiCommandRepo.save(command);
    }
    @Override
    public void acceptTask(Long taskId) {
        // 1. 查找并更新该任务为“已完成”
        DispatchTask task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("任务不存在"));
        task.setStatus(1); // 2 表示“已接收任务 进行中”
        taskRepo.save(task);

        Long commandId = task.getCommandId();
        Long officerId = task.getOfficerId();
        AICommand command = aiCommandRepo.findById(commandId)
                .orElseThrow(() -> new RuntimeException("指令不存在"));

        // 2. 查询该 commandId 下的所有任务
        List<DispatchTask> taskList = taskRepo.findByCommandId(commandId);

        // 3. 判断是否所有任务都已完成
        long acceptedCount = taskList.stream().filter(t -> t.getStatus() == 1).count();
        if (acceptedCount == command.getOfficerCount()) {
            // 4. 所有任务完成后，将所有交警设为空闲
            for (DispatchTask t : taskList) {
                t.setStatus(2);
                Long oid = t.getOfficerId();
                TrafficOfficer officer = officerRepository.findById(oid)
                        .orElseThrow(() -> new RuntimeException("交警不存在"));
                officer.setStatus(1); // 1 表示“空闲”
                officerRepository.save(officer);
            }

            command.setStatus(true); // 1 表示“已完成”
            aiCommandRepo.save(command);
        }
    }



    }


