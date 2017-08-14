/*******************************************************************************
 * Copyright (c) 2011 Google, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Google, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.wb.swt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A special abstract preference page to host field editors and eny other controls
 * with flexible layout.
 * <p>
 * Subclasses must implement the <code>createPageContents</code>.
 * </p>
 * 
 * <p>
 * This class may be freely distributed as part of any application or plugin.
 * </p>
 * 
 * @version $Revision: 1.1 $
 * @author scheglov_ke
 */
public abstract class FieldLayoutPreferencePage extends PreferencePage implements IPropertyChangeListener {
	/**
	 * The field editors.
	 */
	private List<FieldEditor> m_fields = new ArrayList<FieldEditor>();
	/** 
	 * The first invalid field editor, or <code>null</code>
	 * if all field editors are valid.
	 */
	private FieldEditor m_invalidFieldEditor;
	////////////////////////////////////////////////////////////////////////////
	//
	// Constructors
	//
	////////////////////////////////////////////////////////////////////////////
	/**
	 * Creates a new field editor preference page with an empty title, and no image.
	 */
	protected FieldLayoutPreferencePage() {
		// Create a new field editor preference page with an empty title, and no image
	}
	/**
	 * Creates a new field editor preference page with the given title, but no image.
	 *
	 * @param title the title of this preference page
	 */
	protected FieldLayoutPreferencePage(String title) {
		super(title);
	}
	/**
	 * Creates a new field editor preference page with the given image, and style.
	 *
	 * @param title the title of this preference page
	 * @param image the image for this preference page, or <code>null</code> if none
	 */
	protected FieldLayoutPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}
	////////////////////////////////////////////////////////////////////////////
	//
	// Fields
	//
	////////////////////////////////////////////////////////////////////////////
	/**
	 * Adds the given field editor to this page.
	 *
	 * @param editor the field editor
	 */
	protected void addField(FieldEditor editor) {
		m_fields.add(editor);
	}
	/**
	 * Recomputes the page's error state by calling <code>isValid</code> for
	 * every field editor.
	 */
	protected void checkState() {
		boolean valid = true;
		m_invalidFieldEditor = null;
		// The state can only be set to true if all
		// field editors contain a valid value. So we must check them all
		if (m_fields != null) {
			int size = m_fields.size();
			for (int i = 0; i < size; i++) {
				FieldEditor editor = (FieldEditor) m_fields.get(i);
				valid = valid && editor.isValid();
				if (!valid) {
					m_invalidFieldEditor = editor;
					break;
				}
			}
		}
		setValid(valid);
	}
	/* (non-Javadoc)
	 * Method declared on PreferencePage.
	 */
	protected Control createContents(Composite parent) {
		Control contens = createPageContents(parent);
		initialize();
		checkState();
		return contens;
	}
	/**
	 * Creates and returns the SWT control for the customized body 
	 * of this preference page under the given parent composite.
	 * <p>
	 * This framework method must be implemented by concrete subclasses. Any
	 * subclass returning a <code>Composite</code> object whose <code>Layout</code>
	 * has default margins (for example, a <code>GridLayout</code>) are expected to
	 * set the margins of this <code>Layout</code> to 0 pixels. 
	 * </p>
	 *
	 * @param parent the parent composite
	 * @return the new control
	 */
	protected abstract Control createPageContents(Composite parent);
	/**	
	 * The field editor preference page implementation of an <code>IDialogPage</code>
	 * method disposes of this page's controls and images.
	 * Subclasses may override to release their own allocated SWT
	 * resources, but must call <code>super.dispose</code>.
	 */
	public void dispose() {
		super.dispose();
		if (m_fields != null) {
			Iterator<FieldEditor> I = m_fields.iterator();
			while (I.hasNext()) {
				FieldEditor editor = I.next();
				editor.setPage(null);
				editor.setPropertyChangeListener(null);
				editor.setPreferenceStore(null);
			}
		}
	}
	/**
	 * Initializes all field editors.
	 */
	protected void initialize() {
		if (m_fields != null) {
			Iterator<FieldEditor> I = m_fields.iterator();
			while (I.hasNext()) {
				FieldEditor editor = I.next();
				editor.setPage(null);
				editor.setPropertyChangeListener(this);
				editor.setPreferenceStore(getPreferenceStore());
				editor.load();
			}
		}
	}
	/**	
	 * The field editor preference page implementation of a <code>PreferencePage</code>
	 * method loads all the field editors with their default values.
	 */
	protected void performDefaults() {
		if (m_fields != null) {
			Iterator<FieldEditor> I = m_fields.iterator();
			while (I.hasNext()) {
				FieldEditor editor = I.next();
				editor.loadDefault();
			}
		}
		// Force a recalculation of my error state.
		checkState();
		super.performDefaults();
	}
	/** 
	 * The field editor preference page implementation of this 
	 * <code>PreferencePage</code> method saves all field editors by
	 * calling <code>FieldEditor.store</code>. Note that this method
	 * does not save the preference store itself; it just stores the
	 * values back into the preference store.
	 *
	 * @see FieldEditor#store()
	 */
	public boolean performOk() {
		if (m_fields != null) {
			Iterator<FieldEditor> I = m_fields.iterator();
			while (I.hasNext()) {
				FieldEditor editor = I.next();
				editor.store();
			}
		}
		return true;
	}
	/**
	 * The field editor preference page implementation of this <code>IPreferencePage</code>
	 * (and <code>IPropertyChangeListener</code>) method intercepts <code>IS_VALID</code> 
	 * events but passes other events on to its superclass.
	 */
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(FieldEditor.IS_VALID)) {
			boolean newValue = ((Boolean) event.getNewValue()).booleanValue();
			// If the new value is true then we must check all field editors.
			// If it is false, then the page is invalid in any case.
			if (newValue) {
				checkState();
			} else {
				m_invalidFieldEditor = (FieldEditor) event.getSource();
				setValid(newValue);
			}
		}
	}
	/* (non-Javadoc)
	 * Method declared on IDialog.
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible && m_invalidFieldEditor != null) {
			m_invalidFieldEditor.setFocus();
		}
	}
}