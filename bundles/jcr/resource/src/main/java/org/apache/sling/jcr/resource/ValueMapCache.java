/*
 * Copyright 2015 The Apache Software Foundation.
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
package org.apache.sling.jcr.resource;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.sling.jcr.resource.internal.helper.JcrPropertyMapCacheEntry;

/**
 *
 * @author mikolaj.manski
 */
public class ValueMapCache {

	private final Map<String, JcrPropertyMapCacheEntry> cache;

	private final Map<String, Object> valueCache;

	public ValueMapCache() {
		this.cache = new LinkedHashMap<String, JcrPropertyMapCacheEntry>();
		this.valueCache = new HashMap<String, Object>();
	}

	public Map<String, JcrPropertyMapCacheEntry> getCache() {
		return cache;
	}

	public Map<String, Object> getValueCache() {
		return valueCache;
	}

}
