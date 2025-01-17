/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.logging.log4j.changelog;

import java.nio.file.Path;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.changelog.util.FileUtils;

public final class ChangelogFiles {

    private ChangelogFiles() {}

    public static Path unreleasedDirectory(final Path changelogDirectory, final int versionMajor) {
        final String filename = String.format(".%d.x.x", versionMajor);
        return changelogDirectory.resolve(filename);
    }

    public static Set<Integer> unreleasedDirectoryVersionMajors(final Path changelogDirectory) {
        return FileUtils.findAdjacentFiles(changelogDirectory, false, paths -> paths
                .flatMap(path -> {

                    // Only select directories matching with the `^\.(\d+)\.x\.x$` pattern
                    final Pattern versionPattern = Pattern.compile("^\\.(\\d+)\\.x\\.x$");
                    final Matcher versionMatcher = versionPattern.matcher(path.getFileName().toString());
                    if (!versionMatcher.matches()) {
                        return Stream.empty();
                    }
                    final String versionMajorString = versionMatcher.group(1);
                    final int versionMajor = Integer.parseInt(versionMajorString);
                    return Stream.of(versionMajor);

                })
                .collect(Collectors.toSet()));
    }

    public static Path indexTemplateFile(final Path changelogDirectory) {
        return changelogDirectory.resolve(".index.adoc.ftl");
    }

    public static String indexTemplateFile(final Path changelogDirectory, final Path baseDir) {
        return baseDir.relativize(indexTemplateFile(changelogDirectory)).toString().replaceAll("\\\\", "/");
    }

    public static Path releaseDirectory(final Path changelogDirectory, final String releaseVersion) {
        return changelogDirectory.resolve(releaseVersion);
    }

    public static Path releaseXmlFile(final Path releaseDirectory) {
        return releaseDirectory.resolve(".release.xml");
    }

    public static Path releaseChangelogTemplateFile(final Path releaseDirectory) {
        return releaseDirectory.resolve(".changelog.adoc.ftl");
    }

    public static String releaseChangelogTemplateFile(final Path releaseDirectory, final Path baseDir) {
        return baseDir.relativize(releaseChangelogTemplateFile(releaseDirectory)).toString().replaceAll("\\\\", "/");
    }
}
