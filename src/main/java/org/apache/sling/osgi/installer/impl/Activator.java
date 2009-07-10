/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.osgi.installer.impl;

import java.util.Hashtable;

import org.apache.sling.osgi.installer.JcrInstallException;
import org.apache.sling.osgi.installer.OsgiController;
import org.apache.sling.osgi.installer.OsgiControllerServices;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.log.LogService;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.startlevel.StartLevel;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

    private static String PACKAGE_ADMIN_NAME = PackageAdmin.class.getName();
    private static String START_LEVEL_NAME = StartLevel.class.getName();
    private static String LOG_SERVICE_NAME = LogService.class.getName();

    private ServiceTracker startLevelTracker;
    private ServiceTracker packageAdminTracker;
    private ServiceTracker logServiceTracker;
    private OsgiControllerImpl osgiControllerService;
    private ServiceRegistration osgiControllerServiceReg;
    private EventsCounterImpl eventsCounter;
    private ServiceRegistration eventsCounterServiceReg;

    /**
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        this.startLevelTracker = new ServiceTracker(context, START_LEVEL_NAME, null);
        this.packageAdminTracker = new ServiceTracker(context, PACKAGE_ADMIN_NAME, null);
        this.logServiceTracker = new ServiceTracker(context, LOG_SERVICE_NAME, null);
        this.startLevelTracker.open();
        this.packageAdminTracker.open();
        this.logServiceTracker.open();

        // register OsgiController service        
        {
            final Hashtable<String, String> props = new Hashtable<String, String>();
            props.put(Constants.SERVICE_DESCRIPTION, "Apache Sling Install Controller Service");
            props.put(Constants.SERVICE_VENDOR, "The Apache Software Foundation");
            
            // Assume PackageAdmin is available before this bundle is started.
            // That's the case when using Felix OSGi, not sure about other frameworks.
            this.osgiControllerService = new OsgiControllerImpl(context,
                    (PackageAdmin)checkNotNull(this.packageAdminTracker.getService(), "PackageAdmin"),
                    logServiceTracker);
            final String [] serviceInterfaces = {
                    OsgiController.class.getName(),
                    OsgiControllerServices.class.getName()
            };
            osgiControllerServiceReg = context.registerService(serviceInterfaces, osgiControllerService, props);
        }
        
        // register EventsCounter service        
        {
            final Hashtable<String, String> props = new Hashtable<String, String>();
            props.put(Constants.SERVICE_DESCRIPTION, "Apache Sling EventsCounter Service");
            props.put(Constants.SERVICE_VENDOR, "The Apache Software Foundation");
            
            this.eventsCounter = new EventsCounterImpl(context);
            final String [] serviceInterfaces = {
                    EventsCounter.class.getName()
            };
            eventsCounterServiceReg = context.registerService(serviceInterfaces, eventsCounter, props);
        }
    }
    
    /** Complain if value is null */
    static Object checkNotNull(Object value, String what) throws JcrInstallException {
    	if(value == null) {
    		throw new JcrInstallException(what + " is null, cannot activate");
    	}
    	return value;
    }

    /**
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        if( this.eventsCounterServiceReg != null) {
            this.eventsCounterServiceReg.unregister();
            this.eventsCounterServiceReg = null;
        }
        if( this.eventsCounter != null) {
            this.eventsCounter.deactivate();
            this.eventsCounter = null;
        }
        if ( this.osgiControllerServiceReg != null ) {
            this.osgiControllerServiceReg.unregister();
            this.osgiControllerServiceReg = null;
        }
        if ( this.osgiControllerService != null ) {
            this.osgiControllerService.deactivate();
            this.osgiControllerService = null;
        }
        if ( this.startLevelTracker != null ) {
            this.startLevelTracker.close();
            this.startLevelTracker = null;
        }
        if ( this.packageAdminTracker != null ) {
            this.packageAdminTracker.close();
            this.packageAdminTracker = null;
        }
        if ( this.logServiceTracker != null ) {
            this.logServiceTracker.close();
            this.logServiceTracker = null;
        }
    }
}
