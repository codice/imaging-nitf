/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codice.imaging.nitf.nifi.processors.nitf;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import org.apache.nifi.util.MockFlowFile;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.Before;
import org.junit.Test;

public class NitfProcessorTest {

  private TestRunner testRunner;

  @Before
  public void init() {
    testRunner = TestRunners.newTestRunner(NitfProcessor.class);
  }

  @Test
  public void testProcessor() {
    testRunner.enqueue(getClass().getClassLoader().getResourceAsStream("i_3001a.ntf"));
    testRunner.run();
    List<MockFlowFile> flowFiles =
        testRunner.getFlowFilesForRelationship(NitfProcessor.REL_SUCCESS);
    assertThat(flowFiles.size(), is(1));
    assertThat(
        flowFiles.get(0).getAttribute("nitf.file.FTITLE"),
        is("Checks an uncompressed 1024x1024 8 bit mono image with GEOcentric data. Airfield"));
    assertThat(
        flowFiles
            .get(0)
            .getAttribute("nitf.image.image-coordinates")
            .matches(
                "^POLYGON \\(\\(85 32.98\\d*, 85.00\\d* 32.98\\d*, 85.00\\d* 32.98\\d*, 85 32.98\\d*, 85 32.98\\d*\\)\\)"),
        is(true));
  }
}
