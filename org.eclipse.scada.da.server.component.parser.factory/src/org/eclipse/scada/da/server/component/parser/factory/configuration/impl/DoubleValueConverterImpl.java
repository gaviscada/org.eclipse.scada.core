/**
 * Copyright (c) 2014 IBH SYSTEMS GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBH SYSTEMS GmbH - initial API and implementation
 */
package org.eclipse.scada.da.server.component.parser.factory.configuration.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.scada.base.extractor.convert.ValueConverter;
import org.eclipse.scada.da.server.component.parser.factory.configuration.DoubleValueConverter;
import org.eclipse.scada.da.server.component.parser.factory.configuration.ParserPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Double Value Converter</b></em>
 * '.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class DoubleValueConverterImpl extends MinimalEObjectImpl.Container implements DoubleValueConverter
{
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DoubleValueConverterImpl ()
    {
        super ();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass ()
    {
        return ParserPackage.Literals.DOUBLE_VALUE_CONVERTER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public ValueConverter createConverter ()
    {
        return new org.eclipse.scada.base.extractor.convert.DoubleValueConverter ();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eInvoke ( int operationID, EList<?> arguments ) throws InvocationTargetException
    {
        switch ( operationID )
        {
            case ParserPackage.DOUBLE_VALUE_CONVERTER___CREATE_CONVERTER:
                return createConverter ();
        }
        return super.eInvoke ( operationID, arguments );
    }

} //DoubleValueConverterImpl
