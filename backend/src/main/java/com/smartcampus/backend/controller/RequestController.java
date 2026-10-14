package com.smartcampus.backend.controller;

import com.smartcampus.backend.model.ServiceRequest;
import com.smartcampus.backend.model.User;
import com.smartcampus.backend.repository.ServiceRequestRepository;
import com.smartcampus.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class RequestController {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{userId}")
    public ResponseEntity<?> createRequest(@PathVariable Long userId, @RequestBody ServiceRequest request) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        
        request.setUser(userOptional.get());
        ServiceRequest savedRequest = serviceRequestRepository.save(request);
        return ResponseEntity.ok(savedRequest);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserRequests(@PathVariable Long userId) {
        List<ServiceRequest> requests = serviceRequestRepository.findByUserId(userId);
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping
    public ResponseEntity<?> getAllRequests() {
        return ResponseEntity.ok(serviceRequestRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRequestById(@PathVariable Long id) {
        Optional<ServiceRequest> requestOpt = serviceRequestRepository.findById(id);
        if (requestOpt.isPresent()) {
            return ResponseEntity.ok(requestOpt.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request not found");
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateRequestStatus(@PathVariable Long id, @RequestBody java.util.Map<String, String> body) {
        Optional<ServiceRequest> requestOpt = serviceRequestRepository.findById(id);
        if (!requestOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request not found");
        }
        
        ServiceRequest req = requestOpt.get();
        req.setStatus(body.get("status"));
        serviceRequestRepository.save(req);
        
        return ResponseEntity.ok(req);
    }
}
