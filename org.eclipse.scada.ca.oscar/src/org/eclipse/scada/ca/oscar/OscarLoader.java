/*******************************************************************************
 * Copyright (c) 2012, 2015 TH4 SYSTEMS GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     TH4 SYSTEMS GmbH - initial API and implementation
 *     Jens Reimann - additional work
 *     IBH SYSTEMS GmbH - add Reader based loader
 *******************************************************************************/
package org.eclipse.scada.ca.oscar;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class OscarLoader
{
    private final Map<String, Map<String, Map<String, String>>> data;

    private final Map<String, Set<String>> ignoreFields;

    public OscarLoader ( final InputStream stream ) throws Exception
    {
        final ZipInputStream zstream = new ZipInputStream ( stream );

        Map<String, Map<String, Map<String, String>>> data = null;
        Map<String, Set<String>> ignoreFields = null;

        ZipEntry entry;
        while ( ( entry = zstream.getNextEntry () ) != null )
        {
            if ( "data.json".equals ( entry.getName () ) )
            {
                data = loadJsonData ( zstream );
            }
            else if ( "ignoreFields.json".equals ( entry.getName () ) )
            {
                ignoreFields = loadIgnoreData ( zstream );
            }
        }

        this.data = data;
        this.ignoreFields = ignoreFields;
    }

    public OscarLoader ( final File file ) throws Exception
    {
        final ZipFile zfile = new ZipFile ( file );
        try
        {
            this.data = loadData ( zfile );
            this.ignoreFields = loadIgnoreData ( zfile );
        }
        finally
        {
            zfile.close ();
        }
    }

    private static Map<String, Map<String, Map<String, String>>> loadData ( final ZipFile zfile ) throws IOException, Exception
    {
        final ZipEntry entry = zfile.getEntry ( "data.json" ); //$NON-NLS-1$
        if ( entry == null )
        {
            throw new IllegalArgumentException ( Messages.getString ( "OscarLoader.InvalidFileType" ) ); //$NON-NLS-1$
        }
        final InputStream stream = zfile.getInputStream ( entry );
        try
        {
            return loadJsonData ( stream );
        }
        finally
        {
            stream.close ();
        }
    }

    private static Map<String, Set<String>> loadIgnoreData ( final ZipFile zfile ) throws IOException, Exception
    {
        final ZipEntry entry = zfile.getEntry ( "ignoreFields.json" ); //$NON-NLS-1$
        if ( entry == null )
        {
            return null;
        }
        final InputStream stream = zfile.getInputStream ( entry );
        try
        {
            return loadIgnoreData ( stream );
        }
        finally
        {
            stream.close ();
        }
    }

    public Map<String, Map<String, Map<String, String>>> getData ()
    {
        return this.data;
    }

    public Map<String, Set<String>> getIgnoreFields ()
    {
        return this.ignoreFields;
    }

    public static Map<String, Set<String>> loadIgnoreData ( final InputStream stream ) throws Exception
    {
        final Gson g = new GsonBuilder ().create ();
        final BufferedReader reader = new BufferedReader ( new InputStreamReader ( stream, "UTF-8" ) ); //$NON-NLS-1$
        return g.fromJson ( reader, new TypeToken<Map<String, Set<String>>> () {}.getType () );
    }

    public static Map<String, Map<String, Map<String, String>>> loadJsonData ( final InputStream stream ) throws Exception
    {
        return loadJsonData ( new BufferedReader ( new InputStreamReader ( stream, "UTF-8" ) ) ); //$NON-NLS-1$
    }

    public static Map<String, Map<String, Map<String, String>>> loadJsonData ( final Reader reader )
    {
        final Gson g = new GsonBuilder ().create ();
        return g.fromJson ( reader, new TypeToken<Map<String, Map<String, Map<String, String>>>> () {}.getType () );
    }

    /**
     * The oscar file suffix excluding the dot
     */
    public static final String OSCAR_SUFFIX = "oscar"; //$NON-NLS-1$

    /**
     * the oscar file suffix including the dot
     */
    public static final String OSCAR_DOT_SUFFIX = ".oscar"; //$NON-NLS-1$

    public static boolean isOscar ( final File file )
    {
        final String fileName = file.getName ().toLowerCase ();
        return fileName.endsWith ( OSCAR_DOT_SUFFIX );
    }

    /**
     * Put all configuration instances into the target map
     * <p>
     * If a configuration with the same ID already exists is will be overwritten
     * completely. Single properties of a configuration instance will not get
     * merged.
     * </p>
     *
     * @param target
     *            the map to put the data into
     * @param data
     *            the data to put, must not be {@code null}
     * @return the number of configurations processed
     */
    public static int putAll ( final Map<String, Map<String, Map<String, String>>> target, final Map<String, Map<String, Map<String, String>>> data )
    {
        int count = 0;

        for ( final Map.Entry<String, Map<String, Map<String, String>>> factoryEntry : data.entrySet () )
        {
            final String factoryId = factoryEntry.getKey ();

            Map<String, Map<String, String>> factory = target.get ( factoryId );
            if ( factory == null )
            {
                factory = new HashMap<> ( factoryEntry.getValue ().size () );
                target.put ( factoryId, factory );
            }

            for ( final Map.Entry<String, Map<String, String>> cfgEntry : factoryEntry.getValue ().entrySet () )
            {
                final String cfgId = cfgEntry.getKey ();
                factory.put ( cfgId, new HashMap<> ( cfgEntry.getValue () ) );
                count++;
            }
        }

        return count;
    }
}
