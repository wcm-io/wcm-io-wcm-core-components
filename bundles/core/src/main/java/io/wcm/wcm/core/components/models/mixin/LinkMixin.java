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
package io.wcm.wcm.core.components.models.mixin;

import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.annotation.versioning.ConsumerType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.wcm.handler.link.Link;

/**
 * Adds wcm.io Link support to model interface.
 */
@ConsumerType
public interface LinkMixin {

  /**
   * Get wcm.io Link handler object
   * @return Link
   */
  @JsonIgnore
  @NotNull
  Link getLinkObject();

  /**
   * Returns true if the link was resolved successful.
   * @return Link is valid
   */
  @JsonProperty("wcmio:linkValid")
  default boolean isLinkValid() {
    return getLinkObject().isValid();
  }

  /**
   * Resolved link URL.
   * @return Link URL
   */
  @JsonProperty("wcmio:linkURL")
  default @Nullable String getLinkURL() {
    return getLinkObject().getUrl();
  }

  /**
   * Returns a map of attributes which can be applied to a HTML anchor element.
   * @return Anchor attributes
   */
  @JsonProperty("wcmio:linkAttributes")
  default @Nullable Map<String, String> getLinkAttributes() {
    return getLinkObject().getAnchorAttributes();
  }

}
