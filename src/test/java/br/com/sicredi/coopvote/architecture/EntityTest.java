package br.com.sicredi.coopvote.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import jakarta.persistence.Entity;

@AnalyzeClasses(
    packages = "br.com.sicredi.coopvote",
    importOptions = {ImportOption.DoNotIncludeTests.class, ImportOption.DoNotIncludeJars.class})
public class EntityTest {

  @ArchTest
  public static final ArchRule domainClassesShouldBeEntities =
      ArchRuleDefinition.classes()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .beAnnotatedWith(Entity.class);
}
