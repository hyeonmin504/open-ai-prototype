package openai.demo.openai.repository;

import openai.demo.openai.domain.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character,Long> {
}
