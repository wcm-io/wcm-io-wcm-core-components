/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2021 wcm.io
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

import static com.adobe.cq.wcm.core.components.models.List.PN_TEASER_DELEGATE;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.wcm.core.components.models.ListItem;
import com.adobe.cq.wcm.core.components.models.datalayer.PageData;
import com.adobe.cq.wcm.core.components.models.datalayer.builder.DataLayerBuilder;
import com.adobe.cq.wcm.core.components.util.ComponentUtils;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.components.Component;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.wcm.handler.link.Link;
import io.wcm.wcm.core.components.impl.link.LinkWrapper;
import io.wcm.wcm.core.components.impl.util.CoreResourceWrapper;

/**
 * {@link ListItem} implementation for page links.
 */
public class PageListItemV2Impl extends AbstractListItemImpl implements ListItem {

  private final Page page;
  protected final LinkWrapper link;
  private final Component parentComponent;
  private Resource teaserResource;

  private static final Logger log = LoggerFactory.getLogger(PageListItemV2Impl.class);

  /**
   * @param page Page
   * @param link Link
   * @param parentId Parent ID
   * @param parentComponent The component that contains this list item
   */
  public PageListItemV2Impl(@NotNull Page page, @NotNull Link link,
      @Nullable String parentId, @Nullable Component parentComponent) {
    super(page.getContentResource(), parentId, parentComponent);
    this.page = page;
    this.link = new LinkWrapper(link);
    this.parentComponent = parentComponent;
  }

  @Override
  public @Nullable com.adobe.cq.wcm.core.components.commons.link.Link getLink() {
    return link.orNull();
  }


  @Override
  @Deprecated
  @JsonIgnore
  public String getURL() {
    return link.getURL();
  }

  @Override
  public String getTitle() {
    String title = page.getNavigationTitle();
    if (title == null) {
      title = page.getPageTitle();
    }
    if (title == null) {
      title = page.getTitle();
    }
    if (title == null) {
      title = page.getName();
    }
    return title;
  }

  @Override
  public String getDescription() {
    return page.getDescription();
  }

  @Override
  public Calendar getLastModified() {
    return page.getLastModified();
  }

  @Override
  public String getPath() {
    return page.getPath();
  }

  @Override
  @JsonIgnore
  public String getName() {
    return page.getName();
  }

  @Override
  public @Nullable Resource getTeaserResource() {
    if (teaserResource == null && parentComponent != null) {
      String delegateResourceType = parentComponent.getProperties().get(PN_TEASER_DELEGATE, String.class);
      if (StringUtils.isEmpty(delegateResourceType)) {
        log.error("In order for list rendering delegation to work correctly you need to set up the teaserDelegate property on" +
            " the {} component; its value has to point to the resource type of a teaser component.", parentComponent.getPath());
      }
      else {
        // TODO: pass in propertes from list component; refactory code!
        Map<String, String> overriddenProperties = new HashMap<>();
        List<String> hiddenProperties = new ArrayList<>();

        Resource resourceToBeWrapped = ComponentUtils.getFeaturedImage(page);
        if (resourceToBeWrapped != null) {
          // use the page featured image and inherit properties from the page item
          overriddenProperties.put(JcrConstants.JCR_TITLE, this.getTitle());
          /*
          if (showDescription) {
            overriddenProperties.put(JcrConstants.JCR_DESCRIPTION, this.getDescription());
          }
          if (linkItems) {
            overriddenProperties.put(ImageResource.PN_LINK_URL, this.getPath());
          }
          */
        }
        else {
          // use the page content node and inherit properties from the page item
          resourceToBeWrapped = page.getContentResource();
          if (resourceToBeWrapped == null) {
            return null;
          }
          /*
          if (!showDescription) {
            hiddenProperties.add(JcrConstants.JCR_DESCRIPTION);
          }
          if (linkItems) {
            overriddenProperties.put(ImageResource.PN_LINK_URL, this.getPath());
          }
          */
        }
        teaserResource = new CoreResourceWrapper(resourceToBeWrapped, delegateResourceType, hiddenProperties, overriddenProperties);
      }
    }
    return teaserResource;
  }

  // --- data layer ---

  @Override
  protected @NotNull PageData getComponentData() {
    return DataLayerBuilder.extending(super.getComponentData()).asPage()
        .withTitle(this::getTitle)
        .withLinkUrl(() -> link.getURL())
        .build();
  }

}
