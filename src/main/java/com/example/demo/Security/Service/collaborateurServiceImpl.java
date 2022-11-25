package com.example.demo.Security.Service;

import com.example.demo.Model.Collaborateur;
import com.example.demo.Repository.CollaborateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class collaborateurServiceImpl implements collaborateurService {

    CollaborateurRepository collaborateurRepository;
    @Override
    public Collaborateur ajouterCollaborateur(Collaborateur collaborateur) {
        return null;
    }

    @Override
    public Collaborateur deleteCollaborateur(long id) {
        return null;
    }

    @Override
    public Collaborateur modifierCollaborateur(long id, Collaborateur collaborateur) {
        return null;
    }

    @Override
    public List<Collaborateur> COLLABORATEUR_LIST() {
        return collaborateurRepository.findAll();
    }
}
