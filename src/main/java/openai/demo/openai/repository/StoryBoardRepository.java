package openai.demo.openai.repository;

import openai.demo.openai.domain.StoryBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryBoardRepository extends JpaRepository<StoryBoard,Long> {
}
