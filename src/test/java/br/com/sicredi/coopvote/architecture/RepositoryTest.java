package br.com.sicredi.coopvote.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(
    packages = "br.com.sicredi.coopvote",
    importOptions = {ImportOption.DoNotIncludeTests.class, ImportOption.DoNotIncludeJars.class})
public class RepositoryTest {

  @ArchTest
  public static final ArchRule repositoryClasses =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Repository")
          .should()
          .beAnnotatedWith(Repository.class)
          .andShould()
          .resideInAPackage("..repository..");
}
