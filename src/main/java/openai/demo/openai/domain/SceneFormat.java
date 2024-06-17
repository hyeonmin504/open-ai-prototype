package openai.demo.openai.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Slf4j
public class SceneFormat {
    @Id @GeneratedValue
    private Long id;
    private String description;
    private String scenePromptEnglish;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private StoryBoard storyBoard;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Image image;
    private int sceneSequence;
    private String dialogue;
    private String background;

    public void setFormatDetails(String description, String dialogue, String background) {
        this.description = description;
        this.dialogue = dialogue;
        this.background = background;
    }

    public void createScenePromptEn(String scenePromptEnglish) {
        this.scenePromptEnglish = scenePromptEnglish;
    }

    protected SceneFormat(int sceneSequence, String description, String dialogue, String background, StoryBoard storyBoard) {
        this.sceneSequence = sceneSequence;
        this.description = description;
        this.dialogue = dialogue;
        this.background = background;
        this.storyBoard = storyBoard;
    }

    public static SceneFormat createFormat(int sceneSequence, String description, String dialogue, String background,StoryBoard storyBoard) {
        log.info("createFormat");
        return new SceneFormat(sceneSequence, description, dialogue, background, storyBoard);
    }

    public SceneFormat(String scenePromptEnglish, int sceneSequence) {
        this.scenePromptEnglish = scenePromptEnglish;
        this.sceneSequence = sceneSequence;
    }

    protected void setStoryBoard(StoryBoard storyBoard){
        this.storyBoard = storyBoard;
        storyBoard.getSceneFormats().add(this);
    }
}
