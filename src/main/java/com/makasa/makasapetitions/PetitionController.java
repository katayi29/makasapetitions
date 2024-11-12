package com.makasa.makasapetitions;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PetitionController {
    private List<Petition> petitions = new ArrayList<>();

    // Sample data
    public PetitionController() {
        petitions.add(new Petition(1L, "Save the Forest", "Protect the local forest from deforestation."));
        petitions.add(new Petition(2L, "Clean the River", "Organize a community cleanup for our river."));
    }

    @GetMapping("/petitions")
    public String viewPetitions(Model model) {
        model.addAttribute("petitions", petitions);
        return "viewPetitions";
    }

    @GetMapping("/petition/{id}")
    public String viewPetition(@PathVariable Long id, Model model) {
        Optional<Petition> petition = petitions.stream().filter(p -> p.getId().equals(id)).findFirst();
        petition.ifPresent(value -> model.addAttribute("petition", value));
        return "viewPetition";
    }

    @GetMapping("/petition/new")
    public String createPetitionForm(Model model) {
        model.addAttribute("petition", new Petition(null, "", ""));
        return "createPetition";
    }

    @PostMapping("/petition/new")
    public String createPetition(@ModelAttribute Petition petition) {
        petition.setId((long) (petitions.size() + 1));
        petitions.add(petition);
        return "redirect:/petitions";
    }

    @GetMapping("/petition/search")
    public String searchPetitionForm() {
        return "searchPetition";
    }

    @PostMapping("/petition/search")
    public String searchPetition(@RequestParam String title, Model model) {
        List<Petition> results = petitions.stream()
            .filter(p -> p.getTitle().toLowerCase().contains(title.toLowerCase()))
            .toList();
        model.addAttribute("results", results);
        return "searchResults";
    }
}
