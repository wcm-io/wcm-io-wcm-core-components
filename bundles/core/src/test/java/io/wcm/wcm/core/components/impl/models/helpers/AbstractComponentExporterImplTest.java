/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.wcm.wcm.core.components.impl.models.helpers;

import static io.wcm.samples.core.testcontext.AppAemContext.CONTENT_ROOT;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.wcm.api.Page;

import io.wcm.samples.core.testcontext.AppAemContext;
import io.wcm.sling.commons.adapter.AdaptTo;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class AbstractComponentExporterImplTest {

  private static final String RESOURCE_TYPE = "app1/components/comp1";

  private final AemContext context = AppAemContext.newAemContext();

  private Page page;

  @BeforeEach
  void setUp() {
    context.addModelsForClasses(DummyComponentExporter.class);
    page = context.pageManager().getPage(CONTENT_ROOT);
  }

  @Test
  void testGetExportedType() {
    context.currentResource(context.create().resource(page, "resource1",
        PROPERTY_RESOURCE_TYPE, RESOURCE_TYPE));

    DummyComponentExporter underTest = AdaptTo.notNull(context.request(), DummyComponentExporter.class);

    assertEquals(RESOURCE_TYPE, underTest.getExportedType());
  }

  @Model(adaptables = SlingHttpServletRequest.class)
  public static class DummyComponentExporter extends AbstractComponentExporterImpl {
    // impl from super class
  }

}
