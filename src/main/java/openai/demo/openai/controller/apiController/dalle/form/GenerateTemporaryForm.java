package openai.demo.openai.controller.apiController.dalle.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenerateTemporaryForm {
    /**
     * {
     *   "title": "title",
     *   "prompt_kor": "프롬프트 한국어 버전 최대 4000자.",
     *   "style": "한국 웹툰",
     *   "generate_type": "간편",
     *   "genre": ["판타지", "로맨스"],
     *   "wish_cut_count": 10,
     *   "character": [
     *     {
     *       "character_prompt": "캐릭터 설명 프롬프트",
     *       "name": "John",
     *       "gender": "Male"
     *     },
     *     {
     *       "character_prompt": "캐릭터 설명 프롬프트2",
     *       "name": "dalle",
     *       "gender": "FeMale"
     *     }
     *   ]
     * }
     */

    private String title;
    private String promptKor;
    private String style;
    private String generateType;
    private List<String> genre;
    private int wishCutCnt;
    private List<CharacterForm> character;

    public enum GenderForm {
        MALE,FEMALE
    }

    @Data
    @AllArgsConstructor
    public static class CharacterForm {
        private String characterPrompt;
        private String name;
        private GenderForm gender;
    }
}
