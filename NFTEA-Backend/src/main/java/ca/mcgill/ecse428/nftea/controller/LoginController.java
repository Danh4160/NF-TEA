package ca.mcgill.ecse428.nftea.controller;


import ca.mcgill.ecse428.nftea.dto.UserAccountDto;
import ca.mcgill.ecse428.nftea.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse428.nftea.utils.DtoUtils;

@CrossOrigin(origins = "*")
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @GetMapping(value = {"/home/login", "/home/login"})
    public ResponseEntity loginUserAccount(@RequestParam("email") String email, @RequestParam("password") String password){
        return new ResponseEntity<>(DtoUtils.convertToDto(loginService.loginUserAccount(email, password)), HttpStatus.OK);
    }
}
