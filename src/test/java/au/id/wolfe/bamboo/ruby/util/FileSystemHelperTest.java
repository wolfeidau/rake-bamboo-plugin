package au.id.wolfe.bamboo.ruby.util;

import org.junit.Assert;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * 
 */
public class FileSystemHelperTest {
	
    private FileSystemHelper fileSystemHelper = new FileSystemHelper();
    
	@Test
	public void testResolve() throws Exception {

		String userHome = fileSystemHelper.getUserHome();
		Assert.assertNotNull(userHome);
		
		assertThat(fileSystemHelper.resolve("~/.ssh"), equalTo(userHome + "/.ssh"));
        assertThat(fileSystemHelper.resolve("~\\.ssh"), equalTo(userHome + "\\.ssh"));
				
        assertResolveSamePath("/var/log/xyz.log");
		assertResolveSamePath( "\\var\\log\\xyz.log");
        assertResolveSamePath( "C:\\temp\\log\\xyz.log");
	}
	
	private void assertResolveSamePath(String absolutePath){
        assertThat(fileSystemHelper.resolve(absolutePath), equalTo(absolutePath));	    
	}
}
