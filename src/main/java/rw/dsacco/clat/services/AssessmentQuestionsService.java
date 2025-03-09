package rw.dsacco.clat.services;

import rw.dsacco.clat.models.AssessmentQuestions;
import rw.dsacco.clat.models.Activities;
import rw.dsacco.clat.repositories.AssessmentQuestionsRepository;
import rw.dsacco.clat.repositories.ActivitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssessmentQuestionsService {

    @Autowired
    private AssessmentQuestionsRepository questionsRepository;

    @Autowired
    private ActivitiesRepository activitiesRepository;

    public AssessmentQuestions createQuestion(AssessmentQuestions question, String activityCode) {
        Optional<Activities> activityOptional = activitiesRepository.findByCode(activityCode);
        if (activityOptional.isEmpty()) {
            throw new RuntimeException("Activity not found");
        }
        question.setActivity(activityOptional.get());
        return questionsRepository.save(question);
    }

    public List<AssessmentQuestions> getAllQuestions() {
        return questionsRepository.findAll();
    }

    public List<AssessmentQuestions> getQuestionsByActivityCode(String activityCode) {
        return questionsRepository.findByActivityCode(activityCode);
    }

    public Optional<AssessmentQuestions> getQuestionByCode(String code) {
        return questionsRepository.findByCode(code);
    }

    public List<AssessmentQuestions> searchQuestions(String keyword) {
        return questionsRepository.searchQuestions(keyword);
    }

    public void deleteQuestion(Long id) {
        questionsRepository.deleteById(id);
    }
}
