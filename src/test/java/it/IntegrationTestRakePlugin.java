package it;

import com.atlassian.bamboo.pageobjects.BambooTestedProduct;
import com.atlassian.bamboo.pageobjects.pages.global.BambooDashboardPage;
import com.atlassian.bamboo.testutils.TestBuildDetails;
import com.atlassian.bamboo.webdriver.page.CreatePlanPage;
import com.atlassian.pageobjects.TestedProductFactory;
import com.atlassian.webdriver.AtlassianWebDriver;
import junit.framework.TestCase;
import org.openqa.selenium.By;

import static junit.framework.Assert.assertTrue;

/**
 * Basic tests for the rake plugin and supporting classes.
 */
public class IntegrationTestRakePlugin extends TestCase {

    final BambooTestedProduct product = TestedProductFactory.create(BambooTestedProduct.class);

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

    public void assertTextPresent(String text) {
        assertTrue("Should contain '" + text + "'", getWebDriver().getDriver().getPageSource().contains(text));
    }

    private AtlassianWebDriver getWebDriver() {
        return product.getTester().getDriver();
    }
}
