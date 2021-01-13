package com.techbase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import data.ResetDatabaseTestExecutionListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;

/**
 * @author nguyentanh
 */
@TestExecutionListeners(mergeMode =
        TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS,
        listeners = {ResetDatabaseTestExecutionListener.class}
)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MyApplication.class)
@ActiveProfiles("test")
public abstract class MyApplicationTests {
    ObjectMapper objectMapper = new ObjectMapper();

    protected String mapToJson(Object obj) throws JsonProcessingException {

        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(obj);
    }

    protected JsonNode getJsonNode(String jsonFormat) throws JsonProcessingException {
        return objectMapper.readTree(jsonFormat);
    }


}
