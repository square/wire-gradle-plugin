package com.squareup.wire.gradle

import com.google.common.base.Strings
import com.google.common.collect.Lists
import com.google.common.collect.Multimap
import com.google.common.collect.Sets
import com.squareup.wire.WireCompiler
import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/** Task to generate Java for a set of .proto files with the Wire compiler. */
@CompileStatic
class WireGeneratorTask extends DefaultTask {
  Collection<WireSourceSetExtension> configurations

  @OutputDirectory File outputDir

  @InputFiles Collection<File> getInputFiles() {
    Set<File> inputFiles = Sets.newHashSet()
    for (WireSourceSetExtension configuration : getConfigurations()) {
      for (Map.Entry<String, Collection<String>> entry : configuration.getFiles().asMap().entrySet()) {
        for (String file : entry.value) {
          inputFiles.add(new File(entry.key, file))
        }
      }
    }
    return inputFiles
  }

  @TaskAction void generate() {
    // Clear the output directory.
    File outDir = getOutputDir()
    outDir.deleteDir()
    outDir.mkdirs()

    for (WireSourceSetExtension configuration : getConfigurations()) {
      boolean noOptions = configuration.getNoOptions()
      boolean android = configuration.getAndroid()
      Collection<String> enumOptions = configuration.getEnumOptions()
      Collection<String> roots = configuration.getRoots()
      String serviceWriter = configuration.getServiceWriter()
      String registryClass = configuration.getRegistryClass()
      Collection<String> serviceWriterOpts = configuration.getServiceWriterOpts()

      Multimap<String, String> files = configuration.getFiles()
      for (Map.Entry<String, Collection<String>> entry : files.asMap().entrySet()) {
        String protoDir = entry.key
        List<String> args = Lists.newArrayList()
        args.add("--proto_path=" + project.file(protoDir).getAbsolutePath())

        for (String path : configuration.protoPaths) {
          args.add("--proto_path=" + project.file(path).getAbsolutePath())
        }

        if (noOptions) {
          args.add("--no_options")
        }
        if (android) {
          args.add("--android")
        }
        enumOptions.each { option ->
          args.add("--enum_options=" + option)
        }
        roots.each { root ->
          args.add("--roots=" + root)
        }
        if (!Strings.isNullOrEmpty(serviceWriter)) {
          args.add("--service_writer=" + serviceWriter)
        }
        if (!Strings.isNullOrEmpty(registryClass)) {
          args.add("--registry_class=" + registryClass)
        }
        serviceWriterOpts.each { serviceWriterOpt ->
          args.add("--service_writer_opt=" + serviceWriterOpt)
        }

        args.add("--java_out=" + outputDir.absolutePath)
        args.addAll(entry.value)

        try {
          WireCompiler.main(args.toArray(new String[args.size()]))
        } catch (Exception e) {
          throw new RuntimeException(
              "${e.getClass().getSimpleName()} generating Wire Java source for "
                  + "$protoDir: ${e.getMessage()}", e);
        }
      }
    }
  }
}
