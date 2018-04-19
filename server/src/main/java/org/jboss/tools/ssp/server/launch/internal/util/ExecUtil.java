package org.jboss.tools.ssp.server.launch.internal.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.IProcessFactory;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.RuntimeProcess;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

public class ExecUtil {
	public static final int ERROR = 125;
	public static final String DebugPlugin_0="Exception occurred executing command line.";


	/**
	 * Convenience method that performs a runtime exec on the given command line
	 * in the context of the specified working directory, and returns the
	 * resulting process. If the current runtime does not support the
	 * specification of a working directory, the status handler for error code
	 * <code>ERR_WORKING_DIRECTORY_NOT_SUPPORTED</code> is queried to see if the
	 * exec should be re-executed without specifying a working directory.
	 *
	 * @param cmdLine the command line
	 * @param workingDirectory the working directory, or <code>null</code>
	 * @return the resulting process or <code>null</code> if the exec is
	 *  canceled
	 * @exception CoreException if the exec fails
	 * @see Runtime
	 *
	 * @since 2.1
	 */
	public static Process exec(String[] cmdLine, File workingDirectory) throws CoreException {
		return exec(cmdLine, workingDirectory, null);
	}

	/**
	 * Convenience method that performs a runtime exec on the given command line
	 * in the context of the specified working directory, and returns the
	 * resulting process. If the current runtime does not support the
	 * specification of a working directory, the status handler for error code
	 * <code>ERR_WORKING_DIRECTORY_NOT_SUPPORTED</code> is queried to see if the
	 * exec should be re-executed without specifying a working directory.
	 *
	 * @param cmdLine the command line
	 * @param workingDirectory the working directory, or <code>null</code>
	 * @param envp the environment variables set in the process, or <code>null</code>
	 * @return the resulting process or <code>null</code> if the exec is
	 *  canceled
	 * @exception CoreException if the exec fails
	 * @see Runtime
	 *
	 * @since 3.0
	 */
	public static Process exec(String[] cmdLine, File workingDirectory, String[] envp) throws CoreException {
		Process p= null;
		try {
			if (workingDirectory == null) {
				p= Runtime.getRuntime().exec(cmdLine, envp);
			} else {
				p= Runtime.getRuntime().exec(cmdLine, envp, workingDirectory);
			}
		} catch (IOException e) {
		    Status status = new Status(IStatus.ERROR, DebugPlugin.getUniqueIdentifier(), ERROR, DebugPlugin_0, e);
		    throw new CoreException(status);
		} 
		return p;
	}

	

	public static IProcess newProcess(ILaunch launch, Process p, String label) {
		return newProcess(launch, p, label, null);
	}	

	public static IProcess newProcess(ILaunch launch, Process p, String label, Map<String, String> attributes) {
		IProcessFactory fact = getProcessFactory(launch, p, label, attributes);
		if( fact != null ) {
			return fact.newProcess(launch, p, label, attributes);
		}
		return new RuntimeProcess(launch, p, label, attributes);
	}
	
	protected static IProcessFactory getProcessFactory(ILaunch launch, Process p, String label, Map<String, String> attributes) {
		return null;
	}
	
	
}
