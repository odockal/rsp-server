/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc. Distributed under license by Red Hat, Inc.
 * All rights reserved. This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors: Red Hat, Inc.
 ******************************************************************************/
package org.jboss.tools.rsp.server.minishift.discovery;

import org.jboss.tools.rsp.server.spi.discovery.IServerBeanTypeProvider;
import org.jboss.tools.rsp.server.spi.discovery.ServerBeanType;

public class MinishiftBeanTypeProvider implements IServerBeanTypeProvider {

	ServerBeanType minishift12PlusType = new MinishiftBeanType();
	ServerBeanType cdk3xType = new CDKBeanType();
	ServerBeanType crc1xType = new CRCBeanType();
	
	@Override
	public ServerBeanType[] getServerBeanTypes() {
		return new ServerBeanType[] {minishift12PlusType, cdk3xType, crc1xType};
	}
}
