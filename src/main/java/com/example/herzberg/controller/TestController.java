package com.example.herzberg.controller;

import com.example.herzberg.model.Answer;
import com.example.herzberg.repository.AnswerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TestController {
    private final AnswerRepository repo;

    public TestController(AnswerRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/")
    public String showTest(Model model) {
        String[] questions = {
                "1. Робота приносить мені задоволення",
                "2. Я відчуваю себе цінним у колективі",
                "3. Моя зарплата мене мотивує",
                "4. Умови праці мене влаштовують",
                "5. Мені цікаво виконувати свої обов’язки",
                "6. Я відчуваю справедливе ставлення керівництва",
                "7. Я маю можливість для професійного зростання",
                "8. Мені зрозумілі цілі та завдання роботи",
                "9. Я відчуваю визнання моїх досягнень",
                "10. Взаємини з колегами позитивні",
                "11. Робота дає мені впевненість у завтрашньому дні",
                "12. Заробітна плата відповідає моїм очікуванням",
                "13. Моя робота корисна для інших",
                "14. Керівник підтримує мене у складних ситуаціях",
                "15. Умови праці зручні та безпечні",
                "16. Я маю свободу приймати рішення у своїй роботі",
                "17. У мене є можливість навчатися та підвищувати кваліфікацію",
                "18. Я відчуваю, що мене поважають на роботі",
                "19. Робота дозволяє реалізовувати мої здібності",
                "20. Я відчуваю стабільність у своїй посаді",
                "21. Взаємини з керівником доброзичливі",
                "22. Я відчуваю особисту відповідальність за результати роботи",
                "23. Заробітна плата стимулює мене працювати краще",
                "24. Моя робота має перспективи кар’єрного росту",
                "25. Я отримую зворотний зв’язок про результати моєї праці",
                "26. Робочий графік мене влаштовує",
                "27. Мені приємно, коли мою роботу визнають",
                "28. Я маю достатньо ресурсів для якісного виконання роботи"
        };
        model.addAttribute("questions", questions);
        return "index";
    }


//    @PostMapping("/submit")
//    public String submitTest(@RequestParam String username,
//                             @RequestParam List<Integer> scores,
//                             @RequestParam List<String> questions,
//                             Model model) {
//        for (int i = 0; i < questions.size(); i++) {
//            Answer a = new Answer();
//            a.setUsername(username);
//            a.setQuestion(questions.get(i));
//            a.setScore(scores.get(i));
//            repo.save(a);
//        }
//        model.addAttribute("username", username);
//        model.addAttribute("count", questions.size());
//        return "result";
//    }
@PostMapping("/submit")
public String submitTest(@RequestParam String username,
                         @RequestParam List<Integer> scores,
                         @RequestParam List<String> questions,
                         Model model) {

    int motivationSum = 0;
    int hygieneSum = 0;
    int motivationCount = 0;
    int hygieneCount = 0;

    for (int i = 0; i < questions.size(); i++) {
        Answer a = new Answer();
        a.setUsername(username);
        a.setQuestion(questions.get(i));
        a.setScore(scores.get(i));
        repo.save(a);

        // Розподіл по групах факторів (умовно, базуючись на методиці Герцберга)
        int qNum = i + 1;
        if (qNum == 1 || qNum == 5 || qNum == 7 || qNum == 9 || qNum == 13 ||
                qNum == 16 || qNum == 17 || qNum == 19 || qNum == 22 || qNum == 24 ||
                qNum == 25 || qNum == 27) {
            motivationSum += scores.get(i);
            motivationCount++;
        } else {
            hygieneSum += scores.get(i);
            hygieneCount++;
        }
    }

    double motivationAvg = (motivationCount > 0) ? (double) motivationSum / motivationCount : 0;
    double hygieneAvg = (hygieneCount > 0) ? (double) hygieneSum / hygieneCount : 0;

    model.addAttribute("username", username);
    model.addAttribute("count", questions.size());
    model.addAttribute("motivationAvg", motivationAvg);
    model.addAttribute("hygieneAvg", hygieneAvg);

    return "result";
}

}
