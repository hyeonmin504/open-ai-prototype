package openai.demo.openai.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import openai.demo.openai.domain.domainenum.GenerateType;
import openai.demo.openai.domain.domainenum.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * 사용자한테 받은 storyboard를 gpt를 통해 해당 엔티티로 정제한 다음 저장
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoryBoard {

    @Id @GeneratedValue
    private Long id;
    private String promptKor;
    private String title;
    private String style;
    @Enumerated(value = EnumType.STRING)
    private GenerateType generateType;
    private String genre;
    private Integer wishCutCount;
    @OneToMany(mappedBy = "storyBoard")
    private List<Character> character = new ArrayList<>();
    @OneToMany(mappedBy = "storyBoard")
    private List<SceneFormat> sceneFormats = new ArrayList<>();
}

