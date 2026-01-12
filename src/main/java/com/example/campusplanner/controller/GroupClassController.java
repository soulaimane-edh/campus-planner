package com.example.campusplanner.controller;

import com.example.campusplanner.model.GroupClass;
import com.example.campusplanner.repository.GroupClassRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/groups")
public class GroupClassController {

    private final GroupClassRepository groupClassRepository;

    public GroupClassController(GroupClassRepository groupClassRepository) {
        this.groupClassRepository = groupClassRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("groups", groupClassRepository.findAll());
        return "groups/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("groupClass", new GroupClass());
        return "groups/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("groupClass") GroupClass groupClass, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "groups/form";
        }
        groupClassRepository.save(groupClass);
        return "redirect:/groups";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        GroupClass groupClass = groupClassRepository.findById(id).orElseThrow();
        model.addAttribute("groupClass", groupClass);
        return "groups/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("groupClass") GroupClass groupClass, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "groups/form";
        }
        groupClass.setId(id);
        groupClassRepository.save(groupClass);
        return "redirect:/groups";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        groupClassRepository.deleteById(id);
        return "redirect:/groups";
    }
}
