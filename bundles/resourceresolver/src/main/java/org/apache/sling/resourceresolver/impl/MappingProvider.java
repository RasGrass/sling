/*
 * Copyright 2016 The Apache Software Foundation.
 *
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
 */
package org.apache.sling.resourceresolver.impl;

import aQute.bnd.annotation.ProviderType;
import org.apache.sling.resourceresolver.impl.mapping.Mapping;

/**
 *
 * @author mikolaj.manski
 */
@ProviderType
public interface MappingProvider {

    Mapping[] getMappings();

    String getIdentifier();

    String getMapRoot();
}
