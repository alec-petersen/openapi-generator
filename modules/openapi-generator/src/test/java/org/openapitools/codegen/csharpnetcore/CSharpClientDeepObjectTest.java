/*
 * Copyright 2018 OpenAPI-Generator Contributors (https://openapi-generator.tech)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openapitools.codegen.csharpnetcore;

import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.ClientOptInput;
import org.openapitools.codegen.CodegenConstants;
import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.languages.CSharpClientCodegen;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openapitools.codegen.TestUtils.assertFileContains;

public class CSharpClientDeepObjectTest {

    @Test
    public void deepObject() throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();
        String outputPath = output.getAbsolutePath().replace('\\', '/');

        OpenAPI openAPI = new OpenAPIParser()
                .readLocation("src/test/resources/3_0/deepobject.yaml", null, new ParseOptions()).getOpenAPI();

        CSharpClientCodegen codegen = new CSharpClientCodegen();
        codegen.setLibrary("restsharp");
        codegen.setOutputDir(output.getAbsolutePath());

        ClientOptInput input = new ClientOptInput();
        input.openAPI(openAPI);
        input.config(codegen);

        DefaultGenerator generator = new DefaultGenerator();

        generator.setGeneratorPropertyDefault(CodegenConstants.MODELS, "true");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_TESTS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_DOCS, "false");
        generator.setGeneratorPropertyDefault(CodegenConstants.APIS, "true");
        generator.setGeneratorPropertyDefault(CodegenConstants.SUPPORTING_FILES, "false");
        generator.opts(input).generate();
        assertFileContains(Paths.get(outputPath + "/src/Org.OpenAPITools/Api/DefaultApi.cs"),
                "options[id]", "options[name]", "options[category]", "options[tags]",
                "options[status]", "options[photoUrls]",
                "inputOptions[a]", "inputOptions[b]", "inputOptions[c]");
        String content = new String(Files.readAllBytes(Paths.get(outputPath + "/src/Org.OpenAPITools/Api/DefaultApi.cs")), StandardCharsets.UTF_8);
        int counter = StringUtils.countMatches(content, "inputOptions[a]");
        assertEquals(2, counter);
    }
}
