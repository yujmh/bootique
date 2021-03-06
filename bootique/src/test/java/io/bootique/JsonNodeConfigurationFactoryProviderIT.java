/*
 * Licensed to ObjectStyle LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ObjectStyle LLC licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.bootique;

import com.fasterxml.jackson.databind.JsonNode;
import io.bootique.cli.Cli;
import io.bootique.config.ConfigurationSource;
import io.bootique.di.Key;
import io.bootique.di.TypeLiteral;
import io.bootique.env.Environment;
import io.bootique.jackson.JacksonService;
import io.bootique.meta.application.OptionMetadata;
import io.bootique.resource.ResourceFactory;
import io.bootique.unit.BQInternalTestFactory;
import io.bootique.unit.BQInternalWebServerTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonNodeConfigurationFactoryProviderIT {

    @RegisterExtension
    public BQInternalWebServerTestFactory wsTestFactory = new BQInternalWebServerTestFactory();

    @RegisterExtension
    public BQInternalTestFactory testFactory = new BQInternalTestFactory();

    @BeforeEach
    public void startWebserver() {
        wsTestFactory.app("--server").resourceUrl(new ResourceFactory("classpath:io/bootique")).createRuntime();
    }

    @Test
    public void testLoadConfiguration_NoConfig() {

        BQRuntime runtime = testFactory.app().createRuntime();

        JsonNodeConfigurationFactoryProvider provider = new JsonNodeConfigurationFactoryProvider(
                runtime.getInstance(ConfigurationSource.class), runtime.getInstance(Environment.class),
                runtime.getInstance(JacksonService.class), runtime.getBootLogger(),
                runtime.getInstance(Key.get(new TypeLiteral<Set<OptionMetadata>>() {
                })),
                Collections.emptySet(),
                Collections.emptySet(),
                runtime.getInstance(Cli.class));

        JsonNode config = provider.loadConfiguration(Collections.emptyMap());
        assertEquals("{}", config.toString());
    }

    @Test
    public void testLoadConfiguration_Yaml() {

        BQRuntime runtime = testFactory.app("--config=http://127.0.0.1:12025/test1.yml").createRuntime();

        JsonNodeConfigurationFactoryProvider provider = new JsonNodeConfigurationFactoryProvider(
                runtime.getInstance(ConfigurationSource.class), runtime.getInstance(Environment.class),
                runtime.getInstance(JacksonService.class), runtime.getBootLogger(),
                runtime.getInstance(Key.get(new TypeLiteral<Set<OptionMetadata>>() {
                })),
                Collections.emptySet(),
                Collections.emptySet(),
                runtime.getInstance(Cli.class));

        JsonNode config = provider.loadConfiguration(Collections.emptyMap());
        assertEquals("{\"a\":\"b\"}", config.toString());
    }

    @Test
    public void testLoadConfiguration_Json() {

        BQRuntime runtime = testFactory.app("--config=http://127.0.0.1:12025/test1.json").createRuntime();

        JsonNodeConfigurationFactoryProvider provider = new JsonNodeConfigurationFactoryProvider(
                runtime.getInstance(ConfigurationSource.class), runtime.getInstance(Environment.class),
                runtime.getInstance(JacksonService.class), runtime.getBootLogger(),
                runtime.getInstance(Key.get(new TypeLiteral<Set<OptionMetadata>>() {
                })),
                Collections.emptySet(),
                Collections.emptySet(),
                runtime.getInstance(Cli.class));

        JsonNode config = provider.loadConfiguration(Collections.emptyMap());
        assertEquals("{\"x\":1}", config.toString());
    }

    @Test
    public void testLoadConfiguration_JsonYaml() {

        BQRuntime runtime = testFactory.app("--config=http://127.0.0.1:12025/test1.json",
                "--config=http://127.0.0.1:12025/test1.yml").createRuntime();

        JsonNodeConfigurationFactoryProvider provider = new JsonNodeConfigurationFactoryProvider(
                runtime.getInstance(ConfigurationSource.class), runtime.getInstance(Environment.class),
                runtime.getInstance(JacksonService.class), runtime.getBootLogger(),
                runtime.getInstance(Key.get(new TypeLiteral<Set<OptionMetadata>>() {
                })),
                Collections.emptySet(),
                Collections.emptySet(),
                runtime.getInstance(Cli.class));

        JsonNode config = provider.loadConfiguration(Collections.emptyMap());
        assertEquals("{\"x\":1,\"a\":\"b\"}", config.toString());
    }

}
