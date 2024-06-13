package openai.demo.openai.domain;

import jakarta.persistence.*;

@Entity
public class SceneFormat {
    @Id @GeneratedValue
    private Long id;

    private String description;
    private String scenePromptEnglish;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private StoryBoard storyBoard;
    private Integer imageSequence;
    private String dialogue;
    private String background;

}
