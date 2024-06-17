package openai.demo.openai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openai.demo.openai.controller.apiController.dalle.form.GenerateTemporaryForm;
import openai.demo.openai.domain.Character;
import openai.demo.openai.domain.Image;
import openai.demo.openai.domain.SceneFormat;
import openai.demo.openai.domain.StoryBoard;
import openai.demo.openai.domain.domainenum.Gender;
import openai.demo.openai.domain.domainenum.GenerateType;
import openai.demo.openai.repository.SceneFormatRepository;
import openai.demo.openai.repository.StoryBoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static openai.demo.openai.controller.apiController.dalle.form.GenerateTemporaryForm.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SceneFormatService {

    private final SceneFormatRepository sceneFormatRepository;
    private final StoryBoardRepository storyBoardRepository;

    public void saveImage(SceneFormat sceneFormat) {
        SceneFormat savedFormat = sceneFormatRepository.save(sceneFormat);
    }

    /**
     * 스토리 보드를 폼 형태로 받으면
     * 데이터 -> gpt를 통해 장면 분할 -> openAi 이미지 생성을 위한 scenePromptKor 생성 -> DeepL을 통해 영어로 번역
     * -> 달리를 통해 scenePromptEn 생성
     * @param form -> 사용자에게 받은 데이터 폼
     * @return storyBoard id 전달
     */
    @Transactional
    public Long saveStoryBoard(GenerateTemporaryForm form) {

        List<Character> character = convertCharacter(form.getCharacter());

        StoryBoard storyBoard = convertStoryBoard(form, character);

        StoryBoard savedStoryBoard = storyBoardRepository.save(storyBoard);

        /**
         * promptKor -> 장면 분할 -> 장면 분할된 프롬프트를 세부 분할 (내용, 대화, 배경)
         *                      -> 장면 분할된 프롬프트를 달리를 위한 영어 프롬프트로 번역
         */
        // 장면 분할 -> description, dialogue, background 추출
        List<SceneFormat> formatsDivide = convertToDivide(savedStoryBoard.getPromptKor(),savedStoryBoard);

        for (SceneFormat sceneFormat : formatsDivide) {
            log.info("sceneFormat={}",sceneFormat);
        }
        // description + background + 내부 로직 => openAi으로 보낼 scenePromptEn 생성
        combineToPromptAndTransEnglish(formatsDivide);

        return savedStoryBoard.getId();
    }

    /**
     * promptKorean을 장면 별로 분할
     * @param promptKor 전체 스토리보드(한국어)
     * @return List<SceneFormat>
     */
    @Transactional
    private List<SceneFormat> convertToDivide(String promptKor,StoryBoard storyBoard) {
        log.info("convertToDivide start");
        String promptGpt = "1. Scene 1\nDialogue: Hello, how are you?\nBackground: A busy city street\nDescription: A bustling city street with people walking and cars driving by. The main character stands in the middle of the scene, looking around.\n\n" +
                "2. Scene 2\nDialogue: I'm fine, thank you. And you?\nBackground: A quiet park\nDescription: The main character sits on a bench in a quiet park, with trees and flowers surrounding them. The sun is shining brightly.\n\n" +
                "3. Scene 3\nDialogue: I'm doing great, thanks for asking.\nBackground: A cozy café\nDescription: The main character is sitting at a table in a cozy café, sipping on a cup of coffee. The atmosphere is warm and inviting.\n\n" +
                "4. Scene 4\nDialogue: What are your plans for today?\nBackground: A modern office\nDescription: The main character is in a modern office, working on a computer. The office is well-organized and has large windows with a view of the city.\n\n" +
                "5. Scene 5\nDialogue: I have a meeting later, but I'm free now.\nBackground: A library\nDescription: The main character is in a quiet library, surrounded by bookshelves filled with books. They are reading a book at a table.";

        String[] scenePromptGpt = promptGpt.trim().split("\n\n");

        List<SceneFormat> sceneFormats = new ArrayList<>();

        Pattern pattern = Pattern.compile("(\\d+)\\. Scene \\d+\\s+Dialogue: (.*?)\\s+Background: (.*?)\\s+Description: (.*)", Pattern.DOTALL);

        for (String rawScene : scenePromptGpt) {
            log.info("rawScene={}",rawScene);
            Matcher matcher = pattern.matcher(rawScene);
            if (matcher.find()) {
                String sceneNumber = matcher.group(1);
                String dialogue = matcher.group(2).trim();
                String background = matcher.group(3).trim();
                String description = matcher.group(4).trim();
                int sceneSequence = Integer.parseInt(sceneNumber);

                SceneFormat sceneFormat = SceneFormat.createFormat(sceneSequence, description, dialogue, background, storyBoard);

                SceneFormat savedFormat = sceneFormatRepository.save(sceneFormat);
                log.info("saveFormat={}",savedFormat);

                sceneFormats.add(savedFormat);
                storyBoard.getSceneFormats().add(savedFormat);
                log.info("sceneFormat={}",sceneFormats.getClass());
            }
        }

        return sceneFormats;
    }

    /**
     * gpt api를 통해서 scenePromptEn 생성하고 저장
     * @param sceneFormats 기존 promptFormats
     * @return List<SceneFormat>
     */
    @Transactional
    private void combineToPromptAndTransEnglish(List<SceneFormat> sceneFormats) {
        log.info("combineToPromptAndTransEnglish start");
        String promptKor;

        for (SceneFormat sceneFormat : sceneFormats) {
            log.info("iter SceneFormat start");
            //내부 로직을 통해 promptEn 생성
            promptKor = sceneFormat.getDescription() + sceneFormat.getBackground();

            String scenePromptEn = convertToPromptEnglish(promptKor);

            sceneFormat.createScenePromptEn(scenePromptEn);

            //달리 로직을 통해 image 를 가져온다


            Image image = new Image("url",sceneFormat);
        }
    }

    /**
     * PromptKor을 english로 변환한 후에 sceneFormat.PromptEn에 저장
     * @param scenePromptKorVer promptKorean 을 장면 별로 분할한 format
     * @return List<SceneFormat>
     */
    @Transactional
    private String convertToPromptEnglish(String scenePromptKorVer) {

        // 영어로 변환 api 사용
        String ScenePromptEnglishVer = scenePromptKorVer;

        return ScenePromptEnglishVer;
    }

    /**
     * storyBoard build
     * @param form
     * @param character
     * @return
     */
    private StoryBoard convertStoryBoard(GenerateTemporaryForm form, List<Character> character) {
        log.info("convertStoryBoard start");
        return StoryBoard.builder()
                .title(form.getTitle())
                .promptKor(form.getPromptKor())
                .style(form.getStyle())
                .generateType(convertToGenerateTypeEnum(form.getGenerateType()))
                .genre(convertToGenre(form.getGenre()))
                .wishCutCount(form.getWishCutCnt())
                .character(character)
                .sceneFormats(new ArrayList<>())
                .build();
    }

    /**
     * Character build
     * @param form
     * @return
     */
    private List<Character> convertCharacter(List<CharacterForm> form) {
        log.info("convertCharacter start");
        List<Character> characters = new ArrayList<>();
        for (CharacterForm characterForm : form) {
            log.info("Character build={}",characterForm.getName());
            characters.add(Character.builder()
                    .characterPrompt(characterForm.getCharacterPrompt())
                    .gender(convertToGenderEnum(characterForm.getGender()))
                    .name(characterForm.getName())
                    .build());
        }
        log.info("convertCharacter end");
        return characters;
    }

    /**
     * GenreForms -> Genre 변환
     * @param genreForms
     * @return
     */
    private String convertToGenre(List<String> genreForms) {
        return String.join(",", genreForms);
    }

    /**
     * GenerateTypeForm -> GenerateType
     * @param generateType
     * @return
     */
    private GenerateType convertToGenerateTypeEnum(String generateType) {
        return GenerateType.valueOf(generateType);
    }

    /**
     * GenderForm -> Gender
     * @param gender
     * @return
     */
    private Gender convertToGenderEnum(GenderForm gender) {
        return Gender.valueOf(gender.name());
    }
}
