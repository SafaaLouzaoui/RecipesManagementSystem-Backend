package com.gestion.recettes.controllers;

import com.gestion.recettes.dto.ProfileDto;
import com.gestion.recettes.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin()
@RequestMapping("api/v1/profiles")
public class ProfileWeb {


    @Autowired
    private ProfileService profileService;

    @PostMapping(path = "/creer")
    public ProfileDto add(@RequestBody ProfileDto profileDto)
    {
        return profileService.creer(profileDto);
    }

    @GetMapping(path = "/lire/{id}")
    public ResponseEntity<ProfileDto> lire(@PathVariable("id") Long id)
    {
        ProfileDto profileDto = profileService.lire(id);
        if(profileDto != null){
            return ResponseEntity.ok(profileDto);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/lireTous")
    public List<ProfileDto> lireTous()
    {
        List<ProfileDto> tousProfiles = profileService.lireTous();
        return tousProfiles;
    }

    @PutMapping(path = "/modifier/{id}")
    public ProfileDto modifier(@PathVariable("id") Long id, @RequestBody ProfileDto profileDto)
    {
        return profileService.modifier(id, profileDto);
    }

    @DeleteMapping(path = "/supprimer/{id}")
    public Boolean deleteProfile(@PathVariable (value ="id") Long id)
    {
        Boolean profileSupprime = profileService.supprimer(id);
        return profileSupprime;
    }

}
