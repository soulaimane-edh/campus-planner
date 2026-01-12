package com.example.campusplanner.controller;

import com.example.campusplanner.controller.SessionRequest;
import com.example.campusplanner.model.GroupClass;
import com.example.campusplanner.model.ModuleCourse;
import com.example.campusplanner.model.SessionSlot;
import com.example.campusplanner.model.Teacher;
import com.example.campusplanner.repository.GroupClassRepository;
import com.example.campusplanner.repository.ModuleCourseRepository;
import com.example.campusplanner.repository.SessionSlotRepository;
import com.example.campusplanner.repository.TeacherRepository;
import com.example.campusplanner.service.TimetableService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/sessions")
public class SessionController {

    private final TimetableService timetableService;
    private final ModuleCourseRepository moduleCourseRepository;
    private final TeacherRepository teacherRepository;
    private final GroupClassRepository groupClassRepository;
    private final SessionSlotRepository sessionSlotRepository;

    public SessionController(TimetableService timetableService, ModuleCourseRepository moduleCourseRepository, TeacherRepository teacherRepository, GroupClassRepository groupClassRepository, SessionSlotRepository sessionSlotRepository) {
        this.timetableService = timetableService;
        this.moduleCourseRepository = moduleCourseRepository;
        this.teacherRepository = teacherRepository;
        this.groupClassRepository = groupClassRepository;
        this.sessionSlotRepository = sessionSlotRepository;
    }

    @GetMapping
    public String list(Model model) {
        fillLists(model);
        model.addAttribute("sessionRequest", new SessionRequest());
        return "sessions/list";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("sessionRequest") SessionRequest sessionRequest,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            fillLists(model);
            model.addAttribute("error", "Merci de remplir tous les champs obligatoires");
            return "sessions/list";
        }

        ModuleCourse module = moduleCourseRepository.findById(sessionRequest.getModuleId()).orElseThrow();
        Teacher teacher = teacherRepository.findById(sessionRequest.getTeacherId()).orElseThrow();
        GroupClass group = groupClassRepository.findById(sessionRequest.getGroupId()).orElseThrow();

        SessionSlot slot = new SessionSlot();
        slot.setModuleCourse(module);
        slot.setTeacher(teacher);
        slot.setGroupClass(group);
        slot.setRoom(sessionRequest.getRoom());
        slot.setSessionType(sessionRequest.getSessionType());
        slot.setStartTime(sessionRequest.getStartTime());
        slot.setEndTime(sessionRequest.getEndTime());

        try {
            timetableService.schedule(slot);
            return "redirect:/sessions";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            fillLists(model);
            model.addAttribute("sessionRequest", sessionRequest);
            return "sessions/list";
        }
    }

    private void fillLists(Model model) {
        model.addAttribute("sessions", sessionSlotRepository.findAll(org.springframework.data.domain.Sort.by("startTime")));
        model.addAttribute("modules", moduleCourseRepository.findAll());
        model.addAttribute("teachers", teacherRepository.findAll());
        model.addAttribute("groups", groupClassRepository.findAll());
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        sessionSlotRepository.deleteById(id);
        return "redirect:/sessions";
    }
}
