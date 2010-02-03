/*
 * Copyright (c) 2007, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.jboss.maven.plugins.docbook.support;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.project.MavenProject;
import org.jboss.maven.util.file.DirectoryCopier;

/**
 * Defines resources-like processing for the jdocbook-style packaging.  Handles
 * files such as XSLT, fonts, images and css, moving them to specific locations
 * within the output directory for inclusion in the final package.
 *
 * @goal resources
 * @phase process-resources
 * @requiresDependencyResolution
 *
 * @author Steve Ebersole
 */
public class ResourceMojo extends AbstractMojo {

	/**
	 * The directory containing the XSLT sources.
	 *
	 * @parameter expression="${basedir}/src/main/xslt"
	 */
	protected File xsltSourceDirectory;

	/**
	 * The directory containing fonts to be included in package.
	 *
	 * @parameter expression="${basedir}/src/main/fonts"
	 */
	protected File fontSourceDirectory;

	/**
	 * The directory containing images to be included in package.
	 *
	 * @parameter expression="${basedir}/src/main/images"
	 */
	protected File imagesSourceDirectory;

	/**
	 * The directory containing css to be included in package.
	 *
	 * @parameter expression="${basedir}/src/main/css"
	 */
	protected File cssSourceDirectory;

	/**
	 * The directory from which packaging is staged.
	 *
	 * @parameter expression="${project.build.outputDirectory}"
	 */
	protected File outputDirectory;

	/**
	 * INTERNAL : The project being built
	 *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

	/**
	 * INTERNAL : The configured artifact handler for the 'jdocbook-style' packaging
	 *
	 * @parameter expression="${component.org.apache.maven.artifact.handler.ArtifactHandler#jdocbook-style}"
	 * @required
	 * @readonly
	 */
	protected ArtifactHandler artifactHandler;


	public void execute() throws MojoExecutionException, MojoFailureException {
		copySource( xsltSourceDirectory, new File( outputDirectory, "xslt" ) );
		copySource( fontSourceDirectory, new File( outputDirectory, "fonts" ) );
		copySource( imagesSourceDirectory, new File( outputDirectory, "images" ) );
		copySource( cssSourceDirectory, new File( outputDirectory, "css" ) );

		project.getArtifact().setArtifactHandler( artifactHandler );
	}

	private void copySource(File sourceDirectory, File targetDirectory) throws MojoExecutionException {
		getLog().info( "attempting to copy directory : " + sourceDirectory.getAbsolutePath() );
		try {
			new DirectoryCopier( sourceDirectory ).copyTo( targetDirectory );
		}
		catch ( IOException e ) {
			throw new MojoExecutionException( "Unable to copy directory [" + sourceDirectory + "]", e );
		}
	}
}