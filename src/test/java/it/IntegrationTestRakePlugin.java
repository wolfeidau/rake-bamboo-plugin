//package it;
//
//import com.atlassian.bamboo.pageobjects.BambooTestedProduct;
//import com.atlassian.bamboo.pageobjects.pages.global.BambooDashboardPage;
//import com.atlassian.pageobjects.TestedProductFactory;
//import com.atlassian.webdriver.AtlassianWebDriver;
//import junit.framework.TestCase;
//import org.openqa.selenium.By;
//
//
///**
// * Basic tests for the rake plugin and supporting classes.
// */
//public class IntegrationTestRakePlugin extends TestCase {
//
//    final BambooTestedProduct product = TestedProductFactory.create(BambooTestedProduct.class);
//
//    public void testAutoDetectionOfRubies() throws Exception {
//
//        product.gotoLoginPage().loginAsSysAdmin(BambooDashboardPage.class);
//
//        product.gotoAdminHomePage();
//
//        getWebDriver().findElement(By.id("serverCapabilities")).click();
//
//        getWebDriver().waitUntilElementIsLocated(By.id("updateDefaultsCapabilities"));
//
//        getWebDriver().findElement(By.id("updateDefaultsCapabilities")).click();
//
//        getWebDriver().waitUntilElementIsLocated(By.id("updateDefaultsCapabilities"));
//
//        assertTextPresent("1.8.7-p358@default");
//    }
//
//    public void assertTextPresent(String text) {
//        assertTrue("Should contain '" + text + "'", getWebDriver().getDriver().getPageSource().contains(text));
//    }
//
//    private AtlassianWebDriver getWebDriver() {
//        return product.getTester().getDriver();
//    }
//}
