/*
 * Copyright 2016-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.task.launcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Glenn Renfro
 */
public class TaskLaunchRequestTests {

	private static final String URI = "https://myURI";

	private static final String APP_NAME = "MY_APP_NAME";

	@Test
	public void testEquals() {
		List<String> args = new ArrayList<>();
		Map<String, String> map = new HashMap<>();
		args.add("foo");
		map.put("bar", "baz");

		TaskLaunchRequest request = new TaskLaunchRequest(URI, Collections.EMPTY_LIST, Collections.EMPTY_MAP,
				Collections.EMPTY_MAP, null);
		TaskLaunchRequest request2 = new TaskLaunchRequest(URI, Collections.EMPTY_LIST, Collections.EMPTY_MAP,
				Collections.EMPTY_MAP, null);
		assertThat(request.equals(null)).isFalse();
		assertThat(request.equals("nope")).isFalse();
		assertThat(request.equals(request)).isTrue();
		assertThat(request.equals(request2)).isTrue();
		TaskLaunchRequest requestDiff = new TaskLaunchRequest("https://oops", Collections.EMPTY_LIST,
				Collections.EMPTY_MAP, Collections.EMPTY_MAP, null);
		assertThat(request.equals(requestDiff)).isFalse();

		requestDiff = new TaskLaunchRequest(URI, args, Collections.EMPTY_MAP, Collections.EMPTY_MAP, null);
		assertThat(request.equals(requestDiff)).isFalse();

		requestDiff = new TaskLaunchRequest(URI, null, null, null, null);
		assertThat(request.equals(requestDiff)).isTrue();

		requestDiff = new TaskLaunchRequest(URI, Collections.EMPTY_LIST, map, Collections.EMPTY_MAP, null);
		assertThat(request.equals(requestDiff)).isFalse();

		requestDiff = new TaskLaunchRequest(URI, Collections.EMPTY_LIST, Collections.EMPTY_MAP, map, null);
		assertThat(request.equals(requestDiff)).isFalse();

		assertThat(request.hashCode()).isEqualTo(request.hashCode());

	}

	@Test
	public void testApplicationName() {
		TaskLaunchRequest request = new TaskLaunchRequest(URI, Collections.EMPTY_LIST, Collections.EMPTY_MAP,
				Collections.EMPTY_MAP, null);
		assertThat(request.getApplicationName().startsWith("Task-")).isTrue();

		request = new TaskLaunchRequest(URI, Collections.EMPTY_LIST, Collections.EMPTY_MAP, Collections.EMPTY_MAP,
				APP_NAME);
		assertThat(request.getApplicationName()).isEqualTo(APP_NAME);
	}

}
