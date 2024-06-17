package openai.demo.openai.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * "appearance": {
 *         "age": "late teenager",      -나이
 *         "expression": "mischievous", -외모
 *         "hair_color": "brown",       -머리색
 *         "eye_color": "blue",         -눈동자 색
 *         "build": "athletic"          -체격
 *       },
 */
@Embeddable
@Data
public class Appearance {

    private String age;
    private String expression;
    private String hairColor;
    private String EyeColor;
    private String build;

    public Appearance() {
    }

    public Appearance(String age, String expression, String hairColor, String eyeColor, String build) {
        this.age = age;
        this.expression = expression;
        this.hairColor = hairColor;
        EyeColor = eyeColor;
        this.build = build;
    }
}
