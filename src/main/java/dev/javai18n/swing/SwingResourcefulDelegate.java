/*
 * Copyright 2026 Clyde Gerber
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.javai18n.swing;

import java.util.Locale;
import java.util.function.Consumer;
import dev.javai18n.core.Localizable.LocaleEvent;
import dev.javai18n.core.Localizable.LocaleEventListener;
import dev.javai18n.core.Resource;
import dev.javai18n.core.ResourcefulDelegate;
import javax.swing.SwingUtilities;

/**
 * A Swing-specific wrapper around {@link ResourcefulDelegate} that adds locale-setting,
 * property update callbacks, and EDT dispatch via {@link SwingUtilities#invokeLater}.
 *
 * <p>This class centralizes the Swing-specific orchestration that all {@code Resourceful}
 * Swing components share: setting the component's locale, updating locale-specific properties,
 * and ensuring that locale event processing occurs on the Event Dispatch Thread.</p>
 */
class SwingResourcefulDelegate implements LocaleEventListener
{
    /**
     * The core delegate that handles resource ownership and listener registration.
     */
    private final ResourcefulDelegate delegate;

    /**
     * A callback that sets the locale on the owning Swing component.
     */
    private final Consumer<Locale> setLocale;

    /**
     * A callback that updates locale-specific properties on the owning Swing component.
     */
    private final Runnable updateCallback;

    /**
     * Construct a SwingResourcefulDelegate.
     *
     * @param resource       The initial Resource for the owning component.
     * @param setLocale      A callback that sets the locale on the owning component
     *                       (e.g. {@code this::setLocale}).
     * @param updateCallback A callback invoked to update locale-specific properties on the owning
     *                       component (e.g. {@code this::updateLocaleSpecificValues}).
     */
    SwingResourcefulDelegate(Resource resource, Consumer<Locale> setLocale, Runnable updateCallback)
    {
        this.setLocale = setLocale;
        this.updateCallback = updateCallback;
        this.delegate = new ResourcefulDelegate(resource, this);
    }

    /**
     * Initialize the owning component by setting its locale, updating locale-specific properties,
     * and registering the delegate as a {@link LocaleEventListener} on the resource's source.
     */
    void initialize()
    {
        setLocale.accept(delegate.getResource().getSource().getBundleLocale());
        updateCallback.run();
        delegate.registerListener();
    }

    /**
     * Process a LocaleEvent by scheduling a locale update and property refresh on the EDT.
     *
     * @param event The LocaleEvent that has been raised.
     */
    @Override
    public void processLocaleEvent(LocaleEvent event)
    {
        SwingUtilities.invokeLater(() ->
        {
            setLocale.accept(event.getLocalizableSource().getBundleLocale());
            updateCallback.run();
        });
    }

    /**
     * Get the Resource holding locale-specific values for the owning component.
     *
     * @return The Resource holding locale-specific values for the owning component.
     */
    Resource getResource()
    {
        return delegate.getResource();
    }

    /**
     * Set the Resource holding locale-specific values for the owning component.
     * Delegates to the core {@link ResourcefulDelegate} for listener management,
     * then calls the update callback to refresh the component's properties.
     *
     * @param resource The new Resource holding locale-specific values for the owning component.
     */
    void setResource(Resource resource)
    {
        delegate.setResource(resource);
        SwingUtilities.invokeLater(() ->
        {
            setLocale.accept(delegate.getResource().getSource().getBundleLocale());
            updateCallback.run();
        });
    }

    /**
     * Unregister this delegate as a {@link dev.javai18n.core.Localizable.LocaleEventListener} on the
     * resource's source. Call this when the owning component is being permanently discarded so that it
     * is no longer retained by the source's listener list.
     */
    void dispose()
    {
        delegate.getResource().getSource().removeLocaleEventListener(delegate);
    }
}
