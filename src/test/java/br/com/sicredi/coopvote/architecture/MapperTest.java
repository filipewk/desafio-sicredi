package br.com.sicredi.coopvote.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.mapstruct.Mapper;

@AnalyzeClasses(
    packages = "br.com.sicredi.coopvote",
    importOptions = {ImportOption.DoNotIncludeTests.class, ImportOption.DoNotIncludeJars.class})
public class MapperTest {

  @ArchTest
  public static final ArchRule mapperClasses =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Mapper")
          .and()
          .areAnnotatedWith(Mapper.class)
          .should()
          .resideInAPackage("..mapper..");
}
