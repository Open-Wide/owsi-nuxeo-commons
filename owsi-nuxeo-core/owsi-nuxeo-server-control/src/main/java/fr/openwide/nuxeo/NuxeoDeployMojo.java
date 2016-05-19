package fr.openwide.nuxeo;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo( name = "deploy", defaultPhase = LifecyclePhase.PROCESS_SOURCES )
public class NuxeoDeployMojo extends AbstractMojo {

	@Parameter( defaultValue = "${marketplace.package.name}", required=true)
	private String MARKETPLACE_PACKAGE_NAME;

	@Parameter( defaultValue = "${marketplace.project.path}", required=true)
	private String MARKETPLACE_PROJECT_PATH;

	@Parameter( defaultValue = "${nuxeo.path}", required=true)
	private String NUXEO_PATH;


	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("package name : " + MARKETPLACE_PACKAGE_NAME);
		getLog().info("marketplace project path : " + MARKETPLACE_PROJECT_PATH);
		getLog().info("nuxeo path : " +  NUXEO_PATH);
		String nuxeoctlPath = "";
		try {
			File f = new File(nuxeoctlPath);
			if (!f.exists()){
				getLog().info("File doesn't exist");
			}

			if (!f.canWrite()){
				//Dirty ?
				Runtime.getRuntime().exec("chmod +x " + nuxeoctlPath);
			}

			getLog().info("Uninstall package \"" + MARKETPLACE_PACKAGE_NAME + "\"");
			Runtime.getRuntime().exec(nuxeoctlPath + " --accept=true --nodeps mp-remove " + MARKETPLACE_PACKAGE_NAME);

			getLog().info("Install new marketplace package");
			//			Runtime.getRuntime().exec(nuxeoctlPath + " --accept=true --nodeps mp-install " );
			//			      runCommand('Install new marketplace package', 
			//			        nuxeoctlPath + ' --accept=true --nodeps mp-install ' + Dir[MARKETPLACE_PROJECT_PATH + '/target/' + MARKETPLACE_PROJECT_PATH.split('/').last + '-*.zip'][0])
		}
		catch ( IOException e )
		{
			//TODO
			throw new MojoExecutionException( "Error ...");
		}

	}
}