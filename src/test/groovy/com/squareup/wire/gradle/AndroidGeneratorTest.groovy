package com.squareup.wire.gradle

import groovy.transform.CompileStatic
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static org.assertj.core.api.Assertions.assertThat

@CompileStatic
class AndroidGeneratorTest {
  public @Rule TemporaryFolder projectDir = new TemporaryFolder()

  @Test void testGenerateAndCompileAndroid() {
    final classpath = ClassPaths.plugin()
    projectDir.newFile("build.gradle") << GradleFiles.androidWithSupportAnnotations(ClassPaths.string(classpath))
    BuildFiles.makeHierarchy(projectDir, "src", "main", "AndroidManifest.xml") << BuildFiles.manifest()
    BuildFiles.makeHierarchy(projectDir, "src", "main", "proto", "Attachments.proto") << BuildFiles.protoFile()

    GradleRunner.create()
        .withPluginClasspath(classpath)
        .withProjectDir(projectDir.root)
        .withArguments("compileDebugJava")
        .build()

    final genDir = new File(projectDir.root, "build/generated/source/proto/debug/${BuildFiles.PACKAGE.replace('.', '/')}")
    assertThat(genDir).isDirectory()

    assertThat(genDir.listFiles().length).isGreaterThan(0)

    final javaSrcs = genDir.listFiles({ File f -> f.name.endsWith(".java") } as FileFilter).collect { File f -> f.text }
    assertThat(javaSrcs)
        .isNotEmpty()
        .areAtLeastOne { String s -> s.contains("@NonNull") || s.contains("@Nullable") }
  }
}
