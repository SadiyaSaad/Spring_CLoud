/*
 * Copyright 2016 the original author or authors.
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

package org.springframework.cloud.task.launcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.deployer.spi.task.LaunchState;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.task.launcher.configuration.TaskConfiguration;
import org.springframework.cloud.task.launcher.util.TaskLauncherSinkApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TaskLauncherSinkApplication.class, TaskConfiguration.class} )
public class TaskLauncherSinkTests {

	private final static String TASK_NAME_PREFIX = "Task-";

	private final static String APP_NAME = "MY_APP_NAME";

	private final static String PARAM1 = "FOO";

	private final static String PARAM2 = "BAR";

	private final static String VALID_URL = "maven://org.springframework.cloud.task.app:"
			+ "timestamp-task:jar:1.0.1.RELEASE";

	private final static String INVALID_URL = "maven://not.real.group:"
			+ "invalid:jar:1.0.0.BUILD-SNAPSHOT";

	private Map<String, String> properties;

	private final static String DEFAULT_STATUS = "test_status";

	@Autowired
	private ApplicationContext context;

	@Autowired
	private Sink sink;

	@Before
	public void setup() {
		properties = new HashMap<>();
		properties.put("server.port", "0");
	}

	@Test
	public void testSuccessWithParams() {
		List<String> commandLineArgs = new ArrayList<>();
		commandLineArgs.add(PARAM1);
		commandLineArgs.add(PARAM2);

		TaskConfiguration.TestTaskLauncher testTaskLauncher =
				launchTask(VALID_URL, commandLineArgs, null);

		assertEquals(LaunchState.complete, testTaskLauncher.status(DEFAULT_STATUS).getState());
		assertEquals(2, testTaskLauncher.getCommandlineArguments().size());
		assertEquals(PARAM1, testTaskLauncher.getCommandlineArguments().get(0));
		assertEquals(PARAM2, testTaskLauncher.getCommandlineArguments().get(1));
		assertTrue(testTaskLauncher.getApplicationName().startsWith(TASK_NAME_PREFIX));
	}

	@Test
	public void testSuccessWithAppName() {
		TaskConfiguration.TestTaskLauncher testTaskLauncher =
				launchTask(VALID_URL, null, APP_NAME);

		assertEquals(LaunchState.complete, testTaskLauncher.status(DEFAULT_STATUS).getState());
		assertEquals(0, testTaskLauncher.getCommandlineArguments().size());
		assertEquals(APP_NAME, testTaskLauncher.getApplicationName());
	}

	@Test
	public void testSuccessNoParams() {
		TaskConfiguration.TestTaskLauncher testTaskLauncher =
				launchTask(VALID_URL, null, null);
		assertEquals(LaunchState.complete, testTaskLauncher.status(DEFAULT_STATUS).getState());
		assertEquals(0, testTaskLauncher.getCommandlineArguments().size());
		assertTrue(testTaskLauncher.getApplicationName().startsWith(TASK_NAME_PREFIX));
	}

	@Test
	public void testInvalidJar() {
		TaskConfiguration.TestTaskLauncher testTaskLauncher = launchTask(
				INVALID_URL, null, APP_NAME);
		verifySuccessWithAppName(testTaskLauncher);
	}
	private void verifySuccessWithAppName(TaskConfiguration.TestTaskLauncher testTaskLauncher) {
		assertEquals(LaunchState.complete, testTaskLauncher.status(DEFAULT_STATUS).getState());
		assertEquals(0, testTaskLauncher.getCommandlineArguments().size());
		assertEquals(APP_NAME, testTaskLauncher.getApplicationName());
	}

	@Test
	public void testNoRun() {
		TaskConfiguration.TestTaskLauncher testTaskLauncher =
				 context.getBean(TaskConfiguration.TestTaskLauncher.class);

		assertEquals(LaunchState.unknown, testTaskLauncher.status(DEFAULT_STATUS).getState());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNoTaskLauncher() {
		TaskLauncherSink sink = new TaskLauncherSink();
		sink.taskLauncherSink(new TaskLaunchRequest(VALID_URL, null, properties,
				null, null));
	}

	private TaskConfiguration.TestTaskLauncher launchTask(String artifactURL,
			List<String> commandLineArgs, String applicationName) {
		TaskConfiguration.TestTaskLauncher testTaskLauncher =
				context.getBean(TaskConfiguration.TestTaskLauncher.class);

		TaskLaunchRequest request = new TaskLaunchRequest(artifactURL,
				commandLineArgs, properties, null, applicationName);
		GenericMessage<TaskLaunchRequest> message = new GenericMessage<>(request);
		this.sink.input().send(message);
		return testTaskLauncher;
	}
}
