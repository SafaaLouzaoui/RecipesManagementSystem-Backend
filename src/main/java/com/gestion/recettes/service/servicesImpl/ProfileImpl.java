package com.gestion.recettes.service.servicesImpl;

import com.gestion.recettes.dto.ProfileDto;
import com.gestion.recettes.entities.Profile;
import com.gestion.recettes.repos.ProfileRepo;
import com.gestion.recettes.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileImpl implements ProfileService{

    @Autowired
    private ProfileRepo profileRepo;

    @Override
    public ProfileDto creer(ProfileDto profileDto) {
        Profile profile = convertToProfile(profileDto);
        profileRepo.save(profile);
        return convertToProfileDto(profile);
    }

    @Override
    public ProfileDto lire(Long id) {
        Optional<Profile> optionalProfile = profileRepo.findById(id);

        if(optionalProfile.isPresent()){

            Profile profile = optionalProfile.get();
            return convertToProfileDto(profile);
        }else{
            System.out.println("Ce Profile n'existe pas");
            return null;
        }
    }

    @Override
    public List<ProfileDto> lireTous() {
        List<Profile> getProfiles = profileRepo.findAll();
        return convertToProfileDtoList(getProfiles);
    }

    @Override
    public ProfileDto modifier(Long id,ProfileDto profileDto) {
        if(profileRepo.existsById(id)){
            Profile profile = profileRepo.getById(id);

            profile.setCode(profileDto.getCode());
            profile.setDescription(profileDto.getDescription());
            Profile updatedProfile = profileRepo.save(profile);
            return convertToProfileDto(updatedProfile);
        }else {
            System.out.println("Ce profile n'existe pas");
        }
        return profileDto;
    }

    @Override
    public Boolean supprimer(Long id) {
        if(profileRepo.existsById(id)){
            profileRepo.deleteById(id);
        }else{
            System.out.println("Ce profile n'existe pas");
        }
        return true;
    }

    public static ProfileDto convertToProfileDto(Profile profile) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setId(profile.getId());

        profileDto.setCode(profile.getCode());
        profileDto.setDescription(profile.getDescription());
        return profileDto;
    }

    public static Profile convertToProfile(ProfileDto profileDto) {
        Profile profile = new Profile();
        profile.setId(profileDto.getId());
        profile.setCode(profileDto.getCode());
        profile.setDescription(profileDto.getDescription());
        return profile;
    }

    public static List<ProfileDto> convertToProfileDtoList(List<Profile> profiles){
        List<ProfileDto> profileDtos = new ArrayList<>();
        for(Profile profile: profiles){
            profileDtos.add(convertToProfileDto(profile));
        }
        return profileDtos;
    }

}
