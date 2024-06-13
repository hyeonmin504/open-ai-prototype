package openai.demo.openai.controller.form.dalle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponseB64JsonForm {
    private String created;
    private List<ImageData> data = new ArrayList<>();
    private String message;

    public ImageResponseB64JsonForm(String message) {
        this.message = message;
    }

    public ImageResponseB64JsonForm(String created, List<ImageData> data) {
        this.created = created;
        this.data = data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageData {
        private String b64_json;
    }
}
