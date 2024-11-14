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

    // Initialize with some sample data
    public PetitionController() {
        petitions.add(new Petition(1L, "Save the Forest", "Protect the local forest from deforestation."));
        petitions.add(new Petition(2L, "Clean the River", "Organize a community cleanup for our river."));
    }

    // Display list of all petitions
    @GetMapping("/petitions")
    public String viewPetitions(Model model) {
        model.addAttribute("petitions", petitions);
        return "viewPetitions";  // Ensure this matches the name of your template
    }

    // View a specific petition by its ID
    @GetMapping("/petition/{id}")
    public String viewPetition(@PathVariable Long id, Model model) {
        Optional<Petition> petition = petitions.stream().filter(p -> p.getId().equals(id)).findFirst();
        petition.ifPresent(value -> model.addAttribute("petition", value));
        return "viewPetition";  // Ensure this matches the name of your template
    }

    @PostMapping("/petition/{id}/sign")
    public String signPetition(@PathVariable Long id, @RequestParam String name, @RequestParam String email) {
        Optional<Petition> petition = petitions.stream()
            .filter(p -> p.getId().equals(id))
            .findFirst();
        petition.ifPresent(p -> {
            // Logic to sign the petition (store the name/email of the signer)
            // You can also create a separate List<Signature> to store signers' details
            // For now, just print out the name and email
            System.out.println("Signed by: " + name + " with email: " + email);
        });
        return "redirect:/petitions"; // Redirect to the list of petitions after signing
    }

    // Form to create a new petition
    @GetMapping("/petition/new")
    public String createPetitionForm(Model model) {
        model.addAttribute("petition", new Petition(null, "", ""));
        return "createPetition";
    }


    // Handle form submission for creating a new petition
    @PostMapping("/petition/new")
    public String createPetition(@ModelAttribute Petition petition) {
        petition.setId((long) (petitions.size() + 1));  // Set the ID for the new petition
        petitions.add(petition);
        return "redirect:/petitions";
    }

    // Form to search for petitions by title
    @GetMapping("/petition/search")
    public String searchPetitionForm() {
        return "searchPetition";  // Ensure this matches the name of your template
    }

    // Handle form submission for searching petitions
    @PostMapping("/petition/search")
    public String searchPetition(@RequestParam String title, Model model) {
        List<Petition> results = petitions.stream()
            .filter(p -> p.getTitle().toLowerCase().contains(title.toLowerCase()))
            .toList();
        model.addAttribute("results", results);
        return "searchResults";  // Ensure this matches the name of your template
    }
}
