package openai.demo.openai.controller.form.dalle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageRequestForm {
    private String prompt;
    private String style;
    private String quality;
    private String size;
    private int n;
    private String responseFormat;
}

