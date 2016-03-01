package com.squareup.wire.gradle

import groovy.transform.CompileStatic
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static org.assertj.core.api.Assertions.assertThat

@CompileStatic
class BasicIntegrationTest {
  public @Rule TemporaryFolder projectDir = new TemporaryFolder()

  @Test void testGenerateAndCompileAndroid() {
    final classpath = ClassPaths.plugin()
    projectDir.newFile("build.gradle") << GradleFiles.androidWithRuntime(ClassPaths.string(classpath))
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
  }

  @Test void testGenerateAndCompileJava() {
    final classpath = ClassPaths.plugin()
    projectDir.newFile("build.gradle") << GradleFiles.javaWithRuntime(ClassPaths.string(classpath))
    BuildFiles.makeHierarchy(projectDir, "src", "main", "proto", "Attachments.proto") << BuildFiles.protoFile()

    GradleRunner.create()
        .withPluginClasspath(classpath)
        .withProjectDir(projectDir.root)
        .withArguments("compileJava")
        .build()

    final genDir = new File(projectDir.root, "build/generated/source/proto")
    assertThat(genDir).isDirectory()
    assertThat(genDir.listFiles().length).isGreaterThan(0)
  }

  @Test void testGenerateJava() {
    final classpath = ClassPaths.plugin()
    projectDir.newFile("build.gradle") << GradleFiles.emptyJava(ClassPaths.string(classpath))
    BuildFiles.makeHierarchy(projectDir, "src", "main", "proto", "Attachments.proto") << BuildFiles.protoFile()

    GradleRunner.create()
        .withPluginClasspath(classpath)
        .withProjectDir(projectDir.root)
        .withArguments("generateWireProtos")
        .build()

    final genDir = new File(projectDir.root, "build/generated/source/proto")
    assertThat(genDir).isDirectory()
    assertThat(genDir.listFiles().length).isGreaterThan(0)
  }
}
