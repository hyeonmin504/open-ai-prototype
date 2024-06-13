package openai.demo.openai.domain;

import jakarta.persistence.*;
import lombok.Data;
import openai.demo.openai.domain.domainenum.GenerateType;
import openai.demo.openai.domain.domainenum.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * 사용자한테 받은 storyboard를 gpt를 통해 해당 엔티티로 정제한 다음 저장
 */
@Entity
@Data
public class StoryBoard {

    @Id @GeneratedValue
    private Long id;
    private String promptKor;
    private String title;
    private String style;
    @Enumerated(value = EnumType.STRING)
    private GenerateType generateType;
    @Enumerated(value = EnumType.STRING)
    private Genre genre;
    private Integer wishCutCount;
    @OneToOne
    private Character character;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<SceneFormat> sceneFormat = new ArrayList<>();

    public void setCharacter(Character character) {
        this.character = character;
    }

    public StoryBoard() {
    }
}

