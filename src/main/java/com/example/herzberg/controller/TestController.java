package com.example.herzberg.controller;

import com.example.herzberg.model.Answer;
import com.example.herzberg.model.UserResultDto;
import com.example.herzberg.repository.AnswerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
public class TestController {
    private final AnswerRepository answerRepo;

    public TestController(AnswerRepository answerRepo) {
        this.answerRepo = answerRepo;
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

    @PostMapping("/submit")
    public String submitTest(@RequestParam String username,
                             @RequestParam List<Integer> scores,
                             @RequestParam List<String> questions,
                             Model model) {

        for (int i = 0; i < questions.size(); i++) {
            Answer a = new Answer();
            a.setUsername(username);
            a.setQuestion(questions.get(i));
            a.setScore(scores.get(i));
            answerRepo.save(a);
        }

        try {
            // Кодируем имя пользователя для URL
            String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8.toString());
            return "redirect:/results/user/" + encodedUsername;
        } catch (UnsupportedEncodingException e) {
            // В случае ошибки кодирования, возвращаем без кодирования
            return "redirect:/results/user/" + username;
        }
    }

//    // список усіх користувачів
//    @GetMapping("/results")
//    public String showAllResults(Model model) {
//        List<String> users = answerRepo.findDistinctUsernames();
//        model.addAttribute("users", users);
//        return "results";
//    }
@GetMapping("/results")
public String showAllResults(Model model) {
    List<String> users = answerRepo.findDistinctUsernames();
    List<UserResultDto> results = new ArrayList<>();

    for (String username : users) {
//        List<Answer> answers = answerRepo.findByUsername(username);
        List<Answer> answers = answerRepo.findByUsernameOrderByCreatedAtDesc(username);

        UserResultDto dto = new UserResultDto();
        dto.setUsername(username);
        dto.setDateTime(answers.get(0).getTimestamp()); // предполагая, что время сохранения есть

        // Гігієнічні фактори
        dto.setFinancialMotives(answers.get(2).getScore() + answers.get(11).getScore() + answers.get(22).getScore());
        dto.setRecognition(answers.get(8).getScore() + answers.get(26).getScore());
        dto.setManagementAttitude(answers.get(5).getScore() + answers.get(13).getScore() + answers.get(20).getScore());
        dto.setTeamwork(answers.get(9).getScore() + answers.get(27).getScore());

        // Мотиваційні фактори
        dto.setResponsibility(answers.get(0).getScore() + answers.get(21).getScore());
        dto.setCareer(answers.get(6).getScore() + answers.get(23).getScore());
        dto.setAchievements(answers.get(12).getScore() + answers.get(18).getScore());
        dto.setWorkContent(answers.get(4).getScore() + answers.get(15).getScore() + answers.get(16).getScore() + answers.get(19).getScore());

        results.add(dto);
    }

    // Сортируем результаты по дате (новые сверху)
    results.sort((r1, r2) -> r2.getDateTime().compareTo(r1.getDateTime()));

    model.addAttribute("results", results);
    return "results";
}

    // детальні відповіді для одного користувача
    @GetMapping("/results/user/{username}")
    public String showUserResult(@PathVariable String username, Model model) {
        List<Answer> answers = answerRepo.findByUsername(username);

        // групування за факторами Герцберга
        Map<String, Integer> hygiene = new LinkedHashMap<>();
        Map<String, Integer> motivation = new LinkedHashMap<>();

        hygiene.put("Фінансові мотиви", answers.get(2).getScore() + answers.get(11).getScore() + answers.get(22).getScore());
        hygiene.put("Суспільне визнання", answers.get(8).getScore() + answers.get(26).getScore());
        hygiene.put("Ставлення з керівництвом", answers.get(5).getScore() + answers.get(13).getScore() + answers.get(20).getScore());
        hygiene.put("Співпраця в колективі", answers.get(9).getScore() + answers.get(27).getScore());

        motivation.put("Відповідальність роботи", answers.get(0).getScore() + answers.get(21).getScore());
        motivation.put("Кар'єра, просування по службі", answers.get(6).getScore() + answers.get(23).getScore());
        motivation.put("Досягнення особистого успіху", answers.get(12).getScore() + answers.get(18).getScore());
        motivation.put("Зміст роботи", answers.get(4).getScore() + answers.get(15).getScore() + answers.get(16).getScore() + answers.get(19).getScore());

        model.addAttribute("username", username);
        model.addAttribute("answers", answers);
        model.addAttribute("hygieneLabels", hygiene.keySet());
        model.addAttribute("hygieneScores", hygiene.values());
        model.addAttribute("motivationLabels", motivation.keySet());
        model.addAttribute("motivationScores", motivation.values());

        return "result";
    }
}
