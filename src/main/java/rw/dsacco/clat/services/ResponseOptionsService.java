package rw.dsacco.clat.services;

import rw.dsacco.clat.models.ResponseOptions;
import rw.dsacco.clat.models.AssessmentQuestions;
import rw.dsacco.clat.repositories.ResponseOptionsRepository;
import rw.dsacco.clat.repositories.AssessmentQuestionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResponseOptionsService {

    @Autowired
    private ResponseOptionsRepository optionsRepository;

    @Autowired
    private AssessmentQuestionsRepository questionsRepository;

    public ResponseOptions createOption(ResponseOptions option, String questionCode) {
        Optional<AssessmentQuestions> questionOptional = questionsRepository.findByCode(questionCode);
        if (questionOptional.isEmpty()) {
            throw new IllegalArgumentException("Question with code " + questionCode + " not found.");
        }
        option.setQuestion(questionOptional.get());
        return optionsRepository.save(option);
    }

    public List<ResponseOptions> getAllOptions() {
        return optionsRepository.findAll();
    }

    public List<ResponseOptions> getOptionsByQuestionCode(String questionCode) {
        return optionsRepository.findByQuestionCode(questionCode);
    }

    public Optional<ResponseOptions> getOptionByCode(String code) {
        return optionsRepository.findByCode(code);
    }


    public List<ResponseOptions> searchOptions(String keyword) {
        return optionsRepository.searchOptions(keyword);
    }

    public void deleteOption(Long id) {
        optionsRepository.deleteById(id);
    }

    public ResponseOptions updateOption(ResponseOptions option) {
        return optionsRepository.save(option);
    }

}
