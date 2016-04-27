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

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.apache.sling.resourceresolver.impl.mapping.MapEntries;
import org.apache.sling.resourceresolver.impl.mapping.Mapping;
import org.osgi.service.component.ComponentContext;

/**
 *
 * @author mikolaj.manski
 */
@Service(value = {MappingProvider.class, DefaultMappingProviderService.class})
@Component(metatype = true)
public class DefaultMappingProviderService implements MappingProvider {

    @Property(value = {"/:/", "/content/:/", "/system/docroot/:/"},
            label = "URL Mappings",
            description = "List of mappings to apply to paths. Incoming mappings are "
            + "applied to request paths to map to resource paths, "
            + "outgoing mappings are applied to map resource paths to paths used on subsequent "
            + "requests. Form is <internalPathPrefix><op><externalPathPrefix> where <op> is "
            + "\">\" for incoming mappings, \"<\" for outgoing mappings and \":\" for mappings "
            + "applied in both directions. Mappings are applied in configuration order by "
            + "comparing and replacing URL prefixes. Note: The use of \"-\" as the <op> value "
            + "indicating a mapping in both directions is deprecated.")
    private static final String PROP_MAPPING = "resource.resolver.mapping";

    @Property(value = MapEntries.DEFAULT_MAP_ROOT,
            label = "Mapping Location",
            description = "The path to the root of the configuration to setup and configure "
            + "the ResourceResolver mapping. The default value is /etc/map.")
    private static final String PROP_MAP_LOCATION = "resource.resolver.map.location";

    @Property(boolValue = true,
            label = "Allow Direct Mapping",
            description = "Whether to add a direct URL mapping to the front of the mapping list.")
    private static final String PROP_ALLOW_DIRECT = "resource.resolver.allowDirect";

    private boolean allowDirect;

    private ComponentContext componentContext;

    private String mapRoot;

    @Override
    public Mapping[] getMappings() {
        final Dictionary<?, ?> properties = componentContext.getProperties();
        final List<Mapping> maps = new ArrayList<Mapping>();
        final String[] mappingList = (String[]) properties.get(PROP_MAPPING);
        for (int i = 0; mappingList != null && i < mappingList.length; i++) {
            maps.add(new Mapping(mappingList[i]));
        }
        final Mapping[] tmp = maps.toArray(new Mapping[maps.size()]);

        // check whether direct mappings are allowed
        final Boolean directProp = (Boolean) properties.get(PROP_ALLOW_DIRECT);
        allowDirect = (directProp != null) ? directProp.booleanValue() : true;
        if (allowDirect) {
            final Mapping[] tmp2 = new Mapping[tmp.length + 1];
            tmp2[0] = Mapping.DIRECT;
            System.arraycopy(tmp, 0, tmp2, 1, tmp.length);
            return tmp2;
        } else {
            return tmp;
        }
    }

    @Override
    public String getIdentifier() {
        return "default";
    }

    @Activate
    protected void activate(final ComponentContext componentContext) {
        this.componentContext = componentContext;
        final Dictionary<?, ?> properties = componentContext.getProperties();
        mapRoot = PropertiesUtil.toString(properties.get(PROP_MAP_LOCATION), MapEntries.DEFAULT_MAP_ROOT);
    }

    @Override
    public String getMapRoot() {
        return mapRoot;
    }

    @Deactivate
    protected void deactivate() {
        this.componentContext = null;
    }

}
