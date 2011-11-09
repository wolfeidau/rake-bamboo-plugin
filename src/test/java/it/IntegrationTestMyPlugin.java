package it;

import com.atlassian.bamboo.pageobjects.BambooTestedProduct;
import com.atlassian.bamboo.pageobjects.pages.global.BambooDashboardPage;
import com.atlassian.bamboo.testutils.AcceptanceTestHelper;
import com.atlassian.bamboo.testutils.TestBuildDetails;
import com.atlassian.bamboo.webdriver.page.CreatePlanPage;
import com.atlassian.pageobjects.TestedProductFactory;
import junit.framework.TestCase;

import java.util.Properties;

public class IntegrationTestMyPlugin extends TestCase {

    final BambooTestedProduct product = TestedProductFactory.create(BambooTestedProduct.class);

    final Properties bambooLanguageProperties = AcceptanceTestHelper.loadProperties("com/atlassian/bamboo/ww2/BambooActionSupport.properties");


    /**
     * Currently using this test to build a basic plan without clicking, need to solidify my plugin and
     * come back to this.
     */
    public void testAutoDetectionOfRubies() throws Exception {

        product.gotoLoginPage().loginAsSysAdmin(BambooDashboardPage.class);
        TestBuildDetails plan = product.visit(CreatePlanPage.class).createNewPassingPlan();


    }
}

