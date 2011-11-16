package it;

import com.atlassian.bamboo.pageobjects.BambooTestedProduct;
import com.atlassian.bamboo.pageobjects.pages.global.BambooDashboardPage;
import com.atlassian.bamboo.testutils.AcceptanceTestHelper;
import com.atlassian.bamboo.testutils.TestBuildDetails;
import com.atlassian.bamboo.webdriver.page.CreatePlanPage;
import com.atlassian.bamboo.webdriver.page.config.TaskConfigurationPage;
import com.atlassian.pageobjects.TestedProductFactory;
import com.atlassian.webdriver.AtlassianWebDriver;
import junit.framework.TestCase;
import org.openqa.selenium.By;

import java.util.Properties;

public class IntegrationTestMyPlugin extends TestCase {

    final BambooTestedProduct product = TestedProductFactory.create(BambooTestedProduct.class);

    final Properties bambooLanguageProperties = AcceptanceTestHelper.loadProperties("com/atlassian/bamboo/ww2/BambooActionSupport.properties");

    public void testAutoDetectionOfRubies() throws Exception {

        product.gotoLoginPage().loginAsSysAdmin(BambooDashboardPage.class);
        TestBuildDetails plan = product.visit(CreatePlanPage.class).createNewPassingPlan();

        product.gotoAdminHomePage();

        getWebDriver().findElement(By.id("serverCapabilities")).click();

        getWebDriver().waitUntilElementIsLocated(By.linkText("Automatically detect server capabilities"));

        getWebDriver().findElement(By.linkText("Automatically detect server capabilities")).click();

        getWebDriver().waitUntilElementIsLocated(By.linkText("Automatically detect server capabilities"));

        assertTextPresent("ruby-1.9.3-p0@default");
    }

    /**
     * Currently using this test to build a basic plan without clicking, need to solidify my plugin and
     * come back to this.
     */
    public void testAddingRakeTask() throws Exception {

        product.gotoLoginPage().loginAsSysAdmin(BambooDashboardPage.class);

        TestBuildDetails plan = product.visit(CreatePlanPage.class).createNewPassingPlan();

        TaskConfigurationPage taskConfigPage = product.visit(TaskConfigurationPage.class, plan.getDefaultJob());
    }

    private AtlassianWebDriver getWebDriver() {
        return product.getTester().getDriver();
    }

    public void assertTextPresent(String text) {
        assertTrue("Should contain '" + text + "'", getWebDriver().getDriver().getPageSource().contains(text));
    }
}

