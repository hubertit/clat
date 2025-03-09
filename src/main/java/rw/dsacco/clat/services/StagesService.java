package rw.dsacco.clat.services;

import rw.dsacco.clat.models.Stages;
import rw.dsacco.clat.models.Products;
import rw.dsacco.clat.repositories.StagesRepository;
import rw.dsacco.clat.repositories.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StagesService {

    @Autowired
    private StagesRepository stagesRepository;

    @Autowired
    private ProductsRepository productsRepository;

    public Stages createStage(Stages stage, Long productId) {
        Optional<Products> productOptional = productsRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new RuntimeException("Product not found");
        }
        stage.setProduct(productOptional.get());
        return stagesRepository.save(stage);
    }

    public List<Stages> getAllStages() {
        return stagesRepository.findAll();
    }

    public List<Stages> getStagesByProductId(Long productId) {
        return stagesRepository.findByProductId(productId);
    }

    public Optional<Stages> getStageByCode(String code) {
        return stagesRepository.findByCode(code);
    }

    public Stages updateStage(Stages stage) {
        return stagesRepository.save(stage);
    }

    public void deleteStage(Long id) {
        stagesRepository.deleteById(id);
    }
}
