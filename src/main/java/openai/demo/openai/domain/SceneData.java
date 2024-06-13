package openai.demo.openai.domain;

import jakarta.persistence.*;

@Entity
public class SceneData {

    @Id @GeneratedValue
    private Long id;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private SceneFormat sceneFormat;
    private String model;
    private String imageSize;
}
