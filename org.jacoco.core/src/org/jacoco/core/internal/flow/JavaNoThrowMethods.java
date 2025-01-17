/*******************************************************************************
 * Copyright (c) 2009, 2021 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Fabian Meumertzheim - initial implementation
 *
 *******************************************************************************/
package org.jacoco.core.internal.flow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public final class JavaNoThrowMethods {
	public static boolean isTesting = false;

	public static boolean shouldInstrumentMethodInsn(String owner, String name,
			String descriptor) {
		if (owner.startsWith("com/code_intelligence/jazzer/") && !isTesting) {
			return false;
		}
		// We only collect no-throw information for the Java standard library.
		if (!owner.startsWith("java/")) {
			return true;
		}
		return !LIST.contains(owner + '#' + name + '#' + descriptor);
	}

	private static final String DATA_FILE_NAME = "java_no_throw_methods_list.dat";
	private static final String DATA_FILE_RESOURCE_PATH = JavaNoThrowMethods.class
			.getPackage().getName().replace('.', '/') + '/' + DATA_FILE_NAME;
	private static final Set<String> LIST = readJavaNoThrowMethods();

	private static Set<String> readJavaNoThrowMethods() {
		// If we successfully appended the agent JAR to the bootstrap class
		// loader path in Agent, the
		// classLoader property of JavaNoThrowMethods returns null and we have
		// to use the system class
		// loader instead.
		ClassLoader classLoader = JavaNoThrowMethods.class.getClassLoader();
		if (classLoader == null) {
			classLoader = ClassLoader.getSystemClassLoader();
		}
		InputStream resourceStream = classLoader
				.getResourceAsStream(DATA_FILE_RESOURCE_PATH);
		if (resourceStream == null) {
			throw new IllegalStateException(String.format(
					"No-throw method signatures not found at resource path: %s%n",
					DATA_FILE_RESOURCE_PATH));
		}
		BufferedReader resourceReader = new BufferedReader(
				new InputStreamReader(resourceStream));
		try {
			Set<String> list = new HashSet<String>();
			String line;
			while ((line = resourceReader.readLine()) != null) {
				list.add(line);
			}
			return list;
		} catch (IOException e) {
			throw new IllegalStateException(
					"Failed to load no-throw method list");
		} finally {
			try {
				resourceStream.close();
			} catch (IOException ignored) {
			}
			try {
				resourceReader.close();
			} catch (IOException ignored) {
			}
		}
	}
}
