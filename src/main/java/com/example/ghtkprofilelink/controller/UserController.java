package com.example.ghtkprofilelink.controller;

import com.example.ghtkprofilelink.constants.RoleEnum;
import com.example.ghtkprofilelink.model.dto.UserDto;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.service.impl.UserServiceImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1.0/user")
public class UserController {

    private final UserServiceImpl userService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public UserController(UserServiceImpl userService, SimpMessagingTemplate simpMessagingTemplate) {
        this.userService = userService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    //    @GetMapping()
//    public ResponseEntity<?> getAll(@RequestParam int page, @RequestParam int pageSize) {
//        return  new ResponseEntity<>(userService.getAll(page, pageSize), HttpStatus.valueOf(200));
//    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Data> getById(@PathVariable Long id) {
//        return ResponseEntity.ok(userService.getById(id));
//    }

    @GetMapping("/username")
    public ResponseEntity<?> getByUsername(@RequestParam String username) {
        return new ResponseEntity<>(userService.getByUsername(username), HttpStatus.valueOf(200));
    }

    @PostMapping("add")
    public ResponseEntity<?> add(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.add(userDto), HttpStatus.valueOf(200));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody UserDto userDto, @PathVariable Long id) {
        return new ResponseEntity<>(userService.update(userDto, id), HttpStatus.valueOf(200));
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteByUsername(@RequestParam String username) {
        return new ResponseEntity<>(userService.deleteByUsername(username), HttpStatus.valueOf(200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.deleteById(id), HttpStatus.valueOf(200));
    }

    @PutMapping("/role-upgrade-request/{id}")
    public ResponseEntity<?> isUpdateRole(@PathVariable Long id,@RequestParam("is-upgrade-role") Boolean isUpgradeRole) {
        return new ResponseEntity<>(userService.roleUpgradeRequest(id,isUpgradeRole), HttpStatus.valueOf(200));
    }

    @PutMapping("/role-upgrade-request/list")
    public ResponseEntity<?> isUpdateRoleList(@RequestParam("is-upgrade-role") Boolean isUpgradeRole,@RequestBody List<UserDto> listUser) {
        return new ResponseEntity<>(userService.roleUpgradeRequestList(listUser,isUpgradeRole), HttpStatus.valueOf(200));
    }

    @GetMapping("/is-upgrade-role")
    public ResponseEntity<?> getListUserRequestedUpdateRole(
            @RequestParam("is-upgrade-role") Boolean isUpgradeRole,
            @RequestParam("username") String username,
            @RequestParam("page") Integer page,
            @RequestParam("page-size") Integer pageSize) {
        return new ResponseEntity<>(userService.getListUserRequestedUpgradeRole(isUpgradeRole, username, PageRequest.of(page, pageSize)), HttpStatus.OK);
    }

    @PutMapping("/upgrade-role/{id}")
    public ResponseEntity<?> upgradeRole(@RequestBody UserDto userDto, @PathVariable Long id) {
        userDto.setIsUpgradeRole(false);
        UserDto user = (UserDto) userService.update(userDto, id).getData();
        String message = "You have been upgraded to a ";
        if (user.getRole() == RoleEnum.USER_VIP) {
            message = message + "VIP USER";
        } else if (user.getRole() == RoleEnum.ADMIN) message = message + "ADMIN";

        simpMessagingTemplate.convertAndSend("/queue/notification/" + id, message);
        return new ResponseEntity<>(new Data(true, "success", user), HttpStatus.OK);
    }

    @PutMapping("/upgrade-role/list")
    public ResponseEntity<?> upgradeListUserByRole(@RequestBody List<UserDto> userDtos) {
        return new ResponseEntity<>(userService.upgradeListUserByRole(userDtos), HttpStatus.OK);
    }

    @GetMapping("/find-by-username")
    public ResponseEntity<?> findByUserName(
            @RequestParam("username") String userName,
            @RequestParam("page") Integer page,
            @RequestParam("page-size") Integer pageSize) {
        return new ResponseEntity<>(userService.findByUsername(userName, PageRequest.of(page, pageSize)), HttpStatus.OK);
    }
}
