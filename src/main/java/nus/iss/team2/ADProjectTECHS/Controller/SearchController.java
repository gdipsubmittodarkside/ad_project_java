package nus.iss.team2.ADProjectTECHS.Controller;

import lombok.RequiredArgsConstructor;
import nus.iss.team2.ADProjectTECHS.Model.Enums.SearchType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import nus.iss.team2.ADProjectTECHS.Model.Query;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {

    // TESTING WEB CLIENT
    // @Autowired
    // WebClient client;
    
    // @GetMapping("")
    // public void findEmployee(){
        
    //     Flux<Employee> empListFlux = client.get()
    //         .uri("allcourses")
    //         .accept(MediaType.APPLICATION_JSON)
    //         .retrieve()
    //         .bodyToFlux(Employee.class);

    //     List<Employee> empList = empListFlux.collectList().block();

    //     Mono<Employee> emp = client.get()
    //         .uri("/employee/1")
    //         .accept(MediaType.APPLICATION_JSON)
    //         .retrieve()
    //         .bodyToMono(Employee.class);
    //     Employee employee = emp.block();

    //     System.out.println(employee);
    //     empList.stream().forEach(System.out::println);
    // }


    @GetMapping(value = {"/search","/","home"})
    public String Search(Model model){
        model.addAttribute("query", new Query());
        return "search";
    }

    @PostMapping("/search")
    public String SearchRedirect(@ModelAttribute Query query,
                                 RedirectAttributes redirectAttributes){

        SearchType searchType = query.getSearchType();
        String input = query.getQueryString().toLowerCase();

        if (searchType.equals(SearchType.JOBS)) {
            redirectAttributes.addAttribute("jobTitleEntered", input);
            return "redirect:/skills/searchByJobTitle";
        } else if (searchType.equals(SearchType.COURSES)){
            redirectAttributes.addAttribute("courseTitleEntered", input);
            return "redirect:/courses/searchByCourseTitle";
        }

        return "redirect:/search";

    }
    
}
