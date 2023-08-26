package br.com.sicredi.coopvote.architecture;

import br.com.sicredi.coopvote.controller.BaseController;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(
    packages = "br.com.sicredi.coopvote",
    importOptions = {ImportOption.DoNotIncludeTests.class, ImportOption.DoNotIncludeJars.class})
public class ControllerTest {

  @ArchTest
  public static final ArchRule controllerImpl =
      ArchRuleDefinition.classes()
          .that()
          .arePublic()
          .and()
          .haveSimpleNameEndingWith("ControllerImpl")
          .and()
          .doNotHaveModifier(JavaModifier.ABSTRACT)
          .should()
          .beAnnotatedWith(RestController.class)
          .andShould()
          .resideInAPackage("..controller..")
          .andShould()
          .beAssignableTo(BaseController.class);
}
