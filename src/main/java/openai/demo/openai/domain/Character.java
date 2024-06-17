package openai.demo.openai.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import openai.demo.openai.domain.domainenum.Gender;

/**
 * {
 *   "characters": [
 *     {
 *       "name": "James",
 *       "description": "A late teenager with a mischievous expression. He has brown hair, blue eyes, and an athletic build. He wears a school uniform.",
 *       "appearance": {
 *         "age": "late teenager",
 *         "expression": "mischievous",
 *         "hair_color": "brown",
 *         "eye_color": "blue",
 *         "build": "athletic"
 *       },
 *       "clothing": "school uniform"
 *     }
 *   ],
 *   "session_id": "1234567890abcdef"
 * }
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Character {

    @Id @GeneratedValue
    private Long id;
    private String clothes;

    private String personality;
    @Embedded()
    private Appearance appearance;

    private String characterPrompt;
    @Embedded
    private Gender gender;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private StoryBoard storyBoard;

    public Character(String characterPrompt, Gender gender, String name) {
        this.characterPrompt = characterPrompt;
        this.gender = gender;
        this.name = name;
    }

    public Character(String name, String clothes, Appearance appearance, String personality, String characterPrompt, Gender gender) {
        this.clothes = clothes;
        this.personality = personality;
        this.appearance = appearance;
        this.characterPrompt = characterPrompt;
        this.gender = gender;
        this.name = name;
    }

    public void setStoryBoard(StoryBoard storyBoard) {
        this.storyBoard = storyBoard;
        storyBoard.getCharacter().add(this);
    }
}
