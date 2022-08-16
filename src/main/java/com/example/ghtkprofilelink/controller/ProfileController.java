package com.example.ghtkprofilelink.controller;

import com.example.ghtkprofilelink.model.dto.ProfileDto;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.service.ProfileService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.time.Duration;

@CrossOrigin
@RestController
@RequestMapping("api/v1.0/profile")
public class ProfileController {

    private final Bucket bucket;

    @Autowired
    private ProfileService profileService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;


    @GetMapping("/{id}")
    public ResponseEntity<?> get(
            @PathVariable Long id) {
        return new ResponseEntity<>(profileService.getById(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> add(@ModelAttribute ProfileDto profileDto,
                                 @RequestParam(required = false) MultipartFile file) {
        return new ResponseEntity<>(profileService.add(profileDto, file), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @ModelAttribute ProfileDto profileDto,
            @RequestParam(required = false) MultipartFile file,
            @PathVariable Long id) {
        return new ResponseEntity<>(profileService.update(profileDto, file, id), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id) {

        return new ResponseEntity<>(profileService.delete(id), HttpStatus.OK);
    }

    // hàm dùng để giới hạn số lượng requests
    public ProfileController() {
        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(3, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder()
                .addLimit(limit)
                .build();
    }

    @GetMapping("/shortbio/{shortBio}")
    public ResponseEntity<?> getByShortBio(HttpSession session, @PathVariable String shortBio) {
        if (bucket.tryConsume(1)) {
            Data data = profileService.getProfileByShortBio(session, shortBio);
            ProfileDto profileDto = (ProfileDto) data.getData();
//            OutputMessage out = new OutputMessage(
//                new SimpleDateFormat("HH:mm").format(new Date()),
//                    "someone",
//                    "Someone is viewing your profile"
//                );
            if (!data.getMessage().equals("success your profile")) {
                String message = data.getMessage();
                simpMessagingTemplate.convertAndSend("/queue/notification/" + profileDto.getId().toString(), message);
            }

            return ResponseEntity.ok(data);
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @GetMapping("/findprofile/{shortBio}")
    public ResponseEntity<?> findProfileByShortBio(@PathVariable String shortBio) {
        return new ResponseEntity<>(profileService.findProfileByShortBio(shortBio), HttpStatus.OK);
    }

//    @MessageMapping("/ws/{idUser}")
//    public void sendSpecific(
//            @Payload Message msg) throws Exception {
//        OutputMessage out = new OutputMessage(
//                msg.getFrom(),
//                msg.getText(),
//                new SimpleDateFormat("HH:mm").format(new Date()));
//        simpMessagingTemplate.convertAndSendToUser(msg.getTo(),"/queue/notification/"+msg.getTo(),out);
//    }

    @GetMapping("/top")
    public ResponseEntity<?> getTopProfile(@RequestParam("page") int page, @RequestParam("page-size") int pageSize) {
        return new ResponseEntity<>(profileService.getTopProfile(page, pageSize), HttpStatus.OK);
    }

}
