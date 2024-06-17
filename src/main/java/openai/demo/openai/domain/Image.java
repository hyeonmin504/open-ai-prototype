package openai.demo.openai.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Image {

    @Id @GeneratedValue
    private Long id;

    private String imageUrl;

    @OneToOne
    private SceneFormat sceneFormat;

    public Image(String imageUrl, SceneFormat sceneFormat) {
        this.imageUrl = imageUrl;
        setSceneFormat(sceneFormat);
    }

    public void setSceneFormat(SceneFormat sceneFormat) {
        this.sceneFormat = sceneFormat;
        sceneFormat.setImage(this);
    }
}
