package br.com.sicredi.coopvote.architecture;

import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.springframework.stereotype.Service;

@AnalyzeClasses(
    packages = "br.com.sicredi.coopvote",
    importOptions = {ImportOption.DoNotIncludeTests.class, ImportOption.DoNotIncludeJars.class})
public class ServiceTest {

  @ArchTest
  public static final ArchRule serviceImpl =
      ArchRuleDefinition.classes()
          .that()
          .arePublic()
          .and()
          .haveSimpleNameEndingWith("ServiceImpl")
          .and()
          .doNotHaveModifier(JavaModifier.ABSTRACT)
          .should()
          .beAnnotatedWith(Service.class)
          .andShould()
          .resideInAPackage("..service..");
}
