package openai.demo.openai.controller.apiController.dalle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import openai.demo.openai.controller.apiController.dalle.form.GenerateTemporaryForm;
import openai.demo.openai.domain.SceneFormat;
import openai.demo.openai.domain.StoryBoard;
import openai.demo.openai.repository.StoryBoardRepository;
import openai.demo.openai.service.SceneFormatService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/DE")
public class ImageRequestController {

    private final SceneFormatService sceneFormatService;
    private final StoryBoardRepository storyBoardRepository;

    @PostMapping("/imageRequest")
    public Result ImageRequest(@RequestBody GenerateTemporaryForm form) {

        // gpt 를 통해 sceneFormat 을 생성 했다고 가정
        Long storyId = sceneFormatService.saveStoryBoard(form);

        StoryBoard storyBoard = storyBoardRepository.findById(storyId).orElseThrow();

        List<SceneFormat> sceneFormat = storyBoard.getSceneFormats();

        List<SceneFormatForm> sceneFormatForms = new ArrayList<>();

        try {
            for (SceneFormat format : sceneFormat) {
                SceneFormatForm sceneFormatForm = new SceneFormatForm(format.getId(),format.getImage().getId(),
                        format.getImage().getImageUrl(),format.getBackground(),format.getDescription(),format.getDialogue());
                sceneFormatForms.add(sceneFormatForm);
            }

            StoryBoardForm storyBoardForm = new StoryBoardForm(storyBoard.getId(),sceneFormatForms);

            String message = "Scene data retrieved successfully";

            return new Result(HttpStatus.OK,storyBoardForm,message);
        } catch (Exception e) {
            return new Result(HttpStatus.METHOD_NOT_ALLOWED, null, e.getMessage());
        }

    }

    /**
     * {
     *   "code": 200,
     *   "data": {
     *   	"story_id": "qwe12ewqe2eqwe2"
     *     "scene_format": [
     *       {
     * 	      "scene_id": "qwe1qwee2qe2"
     *         "scene_seq": 1,
     *         "image_id": "qwe1wqe12"
     *         "image_url": "http://example.com/image1.png",
     *         "background": "배경 설명",
     *         "description": "내용",
     *         "dialogue": "달리: 달리의 대사"
     *       },
     *       {
     * 	      "scene_id": "12eqw12qw21q"
     *         "scene_seq": 2,
     *         "image_id": "qwe1wqe13"
     *         "image_url": "http://example.com/image2.png",
     *         "background": "배경 설명",
     *         "description": "내용",
     *         "dialogue": "존: 존의 대사"
     *       }
     * 			...
     *     ]
     *   },
     *   "msg": "Scene data retrieved successfully"
     * }
     * @param <T>
     */
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private HttpStatus code;
        private T data;
        private String message;
    }

    @Data
    @AllArgsConstructor
    static class StoryBoardForm {
        private Long storyId;
        private List<SceneFormatForm> sceneFormatForms;
    }

    /**
     * "scene_id": "qwe1qwee2qe2"
     *         "scene_seq": 1,
     *         "image_id": "qwe1wqe12"
     *         "image_url": "http://example.com/image1.png",
     *         "background": "배경 설명",
     *         "description": "내용",
     *         "dialogue": "달리: 달리의 대사"
     */
    @Data
    @AllArgsConstructor
    static class SceneFormatForm {
        private Long sceneId;
        private Long imageId;
        private String imageUrl;
        private String background;
        private String description;
        private String dialogue;
    }
}
