package openai.demo.openai.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class SceneData {

    @Id @GeneratedValue
    private Long id;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private SceneFormat sceneFormat;
    private String model;
    private String imageSize;
    private String responseFormat;
    private int imageCutCnt;

    public SceneData(SceneFormat sceneFormat, String model, String imageSize, String responseFormat, int imageCutCnt) {
        this.sceneFormat = sceneFormat;
        this.model = model;
        this.imageSize = imageSize;
        this.responseFormat = responseFormat;
        this.imageCutCnt = imageCutCnt;
    }
}
