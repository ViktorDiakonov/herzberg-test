package com.example.herzberg.controller;

import com.example.herzberg.model.Answer;
import com.example.herzberg.model.ScoreCalculator;
import com.example.herzberg.model.UserResultDto;
import com.example.herzberg.repository.AnswerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                "25. Я отримую зворотній зв’язок про результати моєї праці",
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

        // Кодируем имя пользователя для URL
        String urlUsername = username.replaceAll("\\s+", "-");
        String encodedUsername = URLEncoder.encode(urlUsername, StandardCharsets.UTF_8);
        return "redirect:/results/user/" + encodedUsername;
    }

//    @GetMapping("/results/user/{username}")
//    public String showUserResult(@PathVariable String username, Model model) {
//        String dbUsername = username.replace("-", " ");
//        List<Answer> answers = answerRepo.findByUsernameOrderByCreatedAtDesc(dbUsername);
//
//        if (answers.isEmpty()) {
//            return "redirect:/results";
//        }
//
//        // Берем только последний тест
//        List<Answer> lastTest = answers.size() >= 28 ? answers.subList(0, 28) : answers;
//
//        Map<String, Integer> hygiene = new LinkedHashMap<>();
//        Map<String, Integer> motivation = new LinkedHashMap<>();
//
//        // Используем lastTest вместо answers
//        hygiene.put("Фінансові мотиви", lastTest.get(2).getScore() + lastTest.get(11).getScore() + lastTest.get(22).getScore());
//        hygiene.put("Суспільне визнання", lastTest.get(8).getScore() + lastTest.get(26).getScore());
//        hygiene.put("Ставлення з керівництвом", lastTest.get(5).getScore() + lastTest.get(13).getScore() + lastTest.get(20).getScore());
//        hygiene.put("Співпраця в колективі", lastTest.get(9).getScore() + lastTest.get(27).getScore());
//
//        motivation.put("Відповідальність роботи", lastTest.get(0).getScore() + lastTest.get(21).getScore());
//        motivation.put("Кар'єра, просування по службі", lastTest.get(6).getScore() + lastTest.get(23).getScore());
//        motivation.put("Досягнення особистого успіху", lastTest.get(12).getScore() + lastTest.get(18).getScore());
//        motivation.put("Зміст роботи", lastTest.get(4).getScore() + lastTest.get(15).getScore() + lastTest.get(16).getScore() + lastTest.get(19).getScore());
//
//        model.addAttribute("username", dbUsername);
//        model.addAttribute("answers", lastTest);
//        model.addAttribute("hygieneLabels", hygiene.keySet());
//        model.addAttribute("hygieneScores", hygiene.values());
//        model.addAttribute("motivationLabels", motivation.keySet());
//        model.addAttribute("motivationScores", motivation.values());
//
//        return "result";
//    }
@GetMapping("/results/user/{username}")
public String showUserResult(@PathVariable String username, Model model) {
    String dbUsername = username.replace("-", " ");
    List<Answer> answers = answerRepo.findByUsernameOrderByCreatedAtDesc(dbUsername);

    if (answers.isEmpty()) {
        return "redirect:/results";
    }

    List<Answer> lastTest = answers.size() >= 28 ? answers.subList(0, 28) : answers;

    Map<String, Integer> hygiene = new LinkedHashMap<>();
    Map<String, Integer> motivation = new LinkedHashMap<>();

    // Гігієнічні фактори (в процентах)
    hygiene.put("Фінансові мотиви", calculatePercentage(
            lastTest.get(2).getScore() + lastTest.get(11).getScore() + lastTest.get(22).getScore(),
            15 // максимум 15 баллов (3 вопроса × 5)
    ));

    hygiene.put("Суспільне визнання", calculatePercentage(
            lastTest.get(8).getScore() + lastTest.get(26).getScore(),
            10 // максимум 10 баллов (2 вопроса × 5)
    ));

    hygiene.put("Ставлення з керівництвом", calculatePercentage(
            lastTest.get(5).getScore() + lastTest.get(13).getScore() + lastTest.get(20).getScore(),
            15 // максимум 15 баллов (3 вопроса × 5)
    ));

    hygiene.put("Співпраця в колективі", calculatePercentage(
            lastTest.get(9).getScore() + lastTest.get(27).getScore(),
            10 // максимум 10 баллов (2 вопроса × 5)
    ));

    // Мотиваційні фактори (в процентах)
    motivation.put("Відповідальність роботи", calculatePercentage(
            lastTest.get(0).getScore() + lastTest.get(21).getScore(),
            10 // максимум 10 баллов (2 вопроса × 5)
    ));

    motivation.put("Кар'єра, просування по службі", calculatePercentage(
            lastTest.get(6).getScore() + lastTest.get(23).getScore(),
            10 // максимум 10 баллов (2 вопроса × 5)
    ));

    motivation.put("Досягнення особистого успіху", calculatePercentage(
            lastTest.get(12).getScore() + lastTest.get(18).getScore(),
            10 // максимум 10 баллов (2 вопроса × 5)
    ));

    motivation.put("Зміст роботи", calculatePercentage(
            lastTest.get(4).getScore() + lastTest.get(15).getScore() + lastTest.get(16).getScore() + lastTest.get(19).getScore(),
            20 // максимум 20 баллов (4 вопроса × 5)
    ));

    model.addAttribute("username", dbUsername);
    model.addAttribute("answers", lastTest);
    model.addAttribute("hygieneLabels", hygiene.keySet());
    model.addAttribute("hygieneScores", hygiene.values());
    model.addAttribute("motivationLabels", motivation.keySet());
    model.addAttribute("motivationScores", motivation.values());

    return "result";
}

//@GetMapping("/results")
//public String showAllResults(Model model) {
//    List<String> users = answerRepo.findDistinctUsernames();
//    List<UserResultDto> results = new ArrayList<>();
//
//    for (String username : users) {
//        List<Answer> answers = answerRepo.findByUsernameOrderByCreatedAtDesc(username);
//
//        // Берем только последний тест (первые 28 ответов)
//        if (answers.size() >= 28) {
//            List<Answer> lastTest = answers.subList(0, 28); // берем ровно 28 ответов
//
//            UserResultDto dto = new UserResultDto();
//            dto.setUsername(username);
//            dto.setDateTime(lastTest.get(0).getCreatedAt());
//
//            // Гігієнічні фактори - используем lastTest вместо answers

    /// /            dto.setFinancialMotives(lastTest.get(2).getScore() + lastTest.get(11).getScore() + lastTest.get(22).getScore());
//            int financialMotivesRaw = lastTest.get(2).getScore() + lastTest.get(11).getScore() + lastTest.get(22).getScore();
//            int financialMotivesPercent = ScoreCalculator.calculatePercentage(financialMotivesRaw, 15);
//            dto.setFinancialMotives(financialMotivesPercent);
//            dto.setRecognition(lastTest.get(8).getScore() + lastTest.get(26).getScore());
//            dto.setManagementAttitude(lastTest.get(5).getScore() + lastTest.get(13).getScore() + lastTest.get(20).getScore());
//            dto.setTeamwork(lastTest.get(9).getScore() + lastTest.get(27).getScore());
//
//            // Мотиваційні факторы - используем lastTest вместо answers
//            dto.setResponsibility(lastTest.get(0).getScore() + lastTest.get(21).getScore());
//            dto.setCareer(lastTest.get(6).getScore() + lastTest.get(23).getScore());
//            dto.setAchievements(lastTest.get(12).getScore() + lastTest.get(18).getScore());
//            dto.setWorkContent(lastTest.get(4).getScore() + lastTest.get(15).getScore() + lastTest.get(16).getScore() + lastTest.get(19).getScore());
//
//            results.add(dto);
//        }
//    }
//
//    results.sort((r1, r2) -> r2.getDateTime().compareTo(r1.getDateTime()));
//    model.addAttribute("results", results);
//    return "results";
//}
// Метод для расчета процентов
    private int calculatePercentage(int actualScore, int maxScore) {
        int minScore = maxScore / 5; // минимальный балл (все ответы = 1)

        if (actualScore <= minScore) return 0;
        if (actualScore >= maxScore) return 100;

        return (int) Math.round(((double) (actualScore - minScore) / (maxScore - minScore)) * 100);
    }

    @GetMapping("/results")
    public String showAllResults(Model model) {
        List<String> users = answerRepo.findDistinctUsernames();
        List<UserResultDto> results = new ArrayList<>();

        for (String username : users) {
            List<Answer> answers = answerRepo.findByUsernameOrderByCreatedAtDesc(username);

            if (answers.size() >= 28) {
                List<Answer> lastTest = answers.subList(0, 28);

                UserResultDto dto = new UserResultDto();
                dto.setUsername(username);
                dto.setDateTime(lastTest.get(0).getCreatedAt());

                // Гігієнічні фактори (в процентах)
                dto.setFinancialMotives(calculatePercentage(
                        lastTest.get(2).getScore() + lastTest.get(11).getScore() + lastTest.get(22).getScore(), 15));
                dto.setRecognition(calculatePercentage(
                        lastTest.get(8).getScore() + lastTest.get(26).getScore(), 10));
                dto.setManagementAttitude(calculatePercentage(
                        lastTest.get(5).getScore() + lastTest.get(13).getScore() + lastTest.get(20).getScore(), 15));
                dto.setTeamwork(calculatePercentage(
                        lastTest.get(9).getScore() + lastTest.get(27).getScore(), 10));

                // Мотиваційні фактори (в процентах)
                dto.setResponsibility(calculatePercentage(
                        lastTest.get(0).getScore() + lastTest.get(21).getScore(), 10));
                dto.setCareer(calculatePercentage(
                        lastTest.get(6).getScore() + lastTest.get(23).getScore(), 10));
                dto.setAchievements(calculatePercentage(
                        lastTest.get(12).getScore() + lastTest.get(18).getScore(), 10));
                dto.setWorkContent(calculatePercentage(
                        lastTest.get(4).getScore() + lastTest.get(15).getScore() + lastTest.get(16).getScore() + lastTest.get(19).getScore(), 20));

                results.add(dto);
            }
        }

        results.sort((r1, r2) -> r2.getDateTime().compareTo(r1.getDateTime()));
        model.addAttribute("results", results);
        return "results";
    }
}
