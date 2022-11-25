package com.example.demo.Controller;

import com.example.demo.Model.Collaborateur;
import com.example.demo.Model.ERole;
import com.example.demo.Model.Role;
import com.example.demo.Payload.Request.SignupRequest;
import com.example.demo.Payload.Response.MessageResponse;
import com.example.demo.Repository.CollaborateurRepository;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Security.Service.collaborateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/collaborateur")
public class CollaborateurController {



    @Autowired
    collaborateurService collaborateurService;
    @Autowired
    CollaborateurRepository collaborateurRepository;
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;

    public CollaborateurController(com.example.demo.Security.Service.collaborateurService collaborateurService) {
        this.collaborateurService = collaborateurService;
    }

    @GetMapping("/u")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Collaborateur> getallCollaborateur() {

        return collaborateurRepository.findAll();
    }

    @PostMapping("/creerCol")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (collaborateurRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (collaborateurRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Collaborateur user = new Collaborateur(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER);
                    //.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {switch (role) {
                        case "admin":
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
                                    //.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);

                            break;
                        default:
                            Role userRole = roleRepository.findByName(ERole.ROLE_USER);
                                    //.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(userRole);
                    } });
        }

        user.setRoles(roles);
        collaborateurRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    //METHODE PERMETTANT DE METTRE A JOUR LES INFORMATION D'UN COLLABORATEUR
    @PutMapping("/updateCollaborateur/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Collaborateur updateCollaborateur(@Valid @RequestBody SignupRequest signUpRequest,@PathVariable("id") Long id){
        //Log.info("MODIFICATION D'UN COLLABORATEUR");


        return  collaborateurRepository.findById(id).map(
                signUpRequest1 ->{
                    signUpRequest1.setUsername(signUpRequest.getUsername());
                    signUpRequest1.setPassword(encoder.encode(signUpRequest.getPassword()));
                    signUpRequest1.setEmail(signUpRequest.getEmail());
                    Set<String> strRoles = signUpRequest.getRole();
                    Set<Role> roles = new HashSet<>();
                    strRoles.forEach(role -> {switch (role) {
                        case "admin":
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
                                    //.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);

                            break;
                        default:
                            Role userRole = roleRepository.findByName(ERole.ROLE_USER);
                                    //.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(userRole);
                    } });
                    signUpRequest1.setRoles(roles);
                    return collaborateurRepository.save(signUpRequest1);
                }
        ).orElseThrow(() -> new RuntimeException("Collaborateur non trouvéé"));
        //return collaborateurService.UpdateCollaborateur(id, user);

    }

    //METHODE PERMETTANT DE SUPPRIMER LE COLLABORATEUR
    @DeleteMapping("/deleteCollaborateur/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCollaborateur(@PathVariable("id") Long id){
        //Log.info("SUPPRESSION D'UN COLLABORATEUR");
        collaborateurRepository.deleteById(id);
        return "utilisateur supprimer";
    }

}
