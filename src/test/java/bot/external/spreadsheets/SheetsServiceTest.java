package bot.external.spreadsheets;

import bot.app.utils.data.questions.BaseQuestion;
import bot.app.utils.data.questions.ChooseQuestion;
import bot.app.utils.data.questions.SliderQuestion;
import data.TestQuestionData;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class SheetsServiceTest {

    SheetsService sheetsService;
    SpreadSheetConfig spreadSheetConfig;

    @BeforeEach
    void before() {
        sheetsService = spy(SheetsService.class);
        spreadSheetConfig = SpreadSheetConfig.BaseQuestions;
    }

    @Test
    void getQuestions_with_correct_data() {
        setCells(List.of(
                TestQuestionData.BASIC_CHOOSE_QUESTION_DATA,
                TestQuestionData.BASIC_SLIDER_QUESTION_DATA
        ));

        assertDoesNotThrow(() -> {
            List<BaseQuestion<?>> result = sheetsService.getQuestions(spreadSheetConfig);
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(ChooseQuestion.class, result.get(0).getClass());
            assertEquals(SliderQuestion.class, result.get(1).getClass());
        });
    }

    @Test
    void getQuestions_with_incorrect_data() {
        var incorrectData = new ArrayList<>(TestQuestionData.BASIC_SLIDER_QUESTION_DATA);
        incorrectData.set(1, "WRONG_DATA");

        setCells(List.of(
                TestQuestionData.BASIC_CHOOSE_QUESTION_DATA,
                incorrectData
        ));

        assertDoesNotThrow(() -> {
            List<BaseQuestion<?>> result = sheetsService.getQuestions(spreadSheetConfig);
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(ChooseQuestion.class, result.get(0).getClass());
        });
    }

    @SneakyThrows
    void setCells(List<List<Object>> rows) {
        doReturn(rows).when(sheetsService).cells(any());
    }
}