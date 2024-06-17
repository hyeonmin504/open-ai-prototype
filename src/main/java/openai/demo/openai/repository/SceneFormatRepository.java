package openai.demo.openai.repository;

import openai.demo.openai.domain.SceneFormat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SceneFormatRepository extends JpaRepository<SceneFormat,Long> {
}
