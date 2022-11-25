package com.example.demo.Security.Service;

import com.example.demo.Model.Collaborateur;
import com.example.demo.Repository.CollaborateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface collaborateurService {

    Collaborateur ajouterCollaborateur(Collaborateur collaborateur);

    Collaborateur deleteCollaborateur(long id);

    Collaborateur modifierCollaborateur(long id, Collaborateur collaborateur);

    List<Collaborateur> COLLABORATEUR_LIST();
}
